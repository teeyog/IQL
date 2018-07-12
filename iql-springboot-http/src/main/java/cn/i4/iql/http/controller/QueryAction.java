package cn.i4.iql.http.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.i4.iql.domain.Bean;
import cn.i4.iql.http.bean.BaseBean;
import cn.i4.iql.http.bean.IqlExcution;
import cn.i4.iql.http.bean.SaveIql;
import cn.i4.iql.http.handler.HDFSHandler;
import cn.i4.iql.http.service.IqlExcutionRepository;
import cn.i4.iql.http.service.SaveIqlRepository;
import cn.i4.iql.http.util.DataUtil;
import cn.i4.iql.http.util.HdfsUtils;
import cn.i4.iql.http.util.HttpUtils;
import cn.i4.iql.utils.ShellUtils;
import cn.i4.iql.utils.ZkUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class QueryAction {

    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private IqlExcutionRepository iqlExcutionRepository;
    @Autowired
    private SaveIqlRepository saveIqlRepository;

    /**
     * 执行一个IQL
     *
     * @param iql
     * @param descrption
     * @return
     */
    @PostMapping(value = "/query")
    public JSONObject execution(@RequestParam("iql") String iql,
                                @RequestParam(value = "variables") String variables,
                                @RequestParam(value = "code", required = false, defaultValue = "") String code,
                                @RequestParam(value = "descrption", required = false, defaultValue = "") String descrption) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", false);
        if (iql.trim().equals("")) {
            resultObj.put("errorMessage", "iql can't be empty...");
            return resultObj;
        }
        Seq<String> validEngines = ZkUtils.getValidChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            resultObj.put("errorMessage", "当前未有可用的执行引擎...");
            return resultObj;
        } else {
            System.out.println("Query:" + validEngines.head());
            String[] engineInfoAndActorname = validEngines.head().split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future1 = Patterns.ask(selection, new Bean.Iql(code, iql, variables), timeout);
                String result1 = Await.result(future1, timeout.duration()).toString();
                resultObj = JSON.parseObject(result1);
                resultObj.put("isSuccess", true);
            } catch (Exception e) {
                resultObj.put("errorMessage", e.getMessage());
            }
            return resultObj;
        }
    }

    /**
     * 获取结果
     */
    @PostMapping(value = "/getresult")
    public JSONObject getResult(String engineInfoAndGroupId) {
        JSONObject resultObj = new JSONObject();
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfoAndGroupId.split("_")[0]);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("errorMessage", "当前未有可用的执行引擎...");
            return resultObj;
        } else {
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future1 = Patterns.ask(selection, new Bean.GetBatchResult(engineInfoAndGroupId), timeout);
                String result1 = Await.result(future1, timeout.duration()).toString();
                resultObj = JSON.parseObject(result1);
            } catch (Exception e) {
                resultObj.put("errorMessage", e.getMessage());
            }
            if (!resultObj.getString("status").equals("RUNNING")) {
                iqlExcutionRepository.save(new IqlExcution(resultObj.getString("iql"), resultObj.getString("code"), resultObj.getTimestamp("startTime"),
                        Long.valueOf(resultObj.getOrDefault("takeTime", "0").toString()), resultObj.getBoolean("isSuccess"),
                        resultObj.getOrDefault("hdfsPath", "").toString(), "", resultObj.getOrDefault("errorMessage", "").toString(),
                        resultObj.getOrDefault("schema", "").toString(), resultObj.getString("variables")));
                if (resultObj.get("hdfsPath") != null && resultObj.get("hdfsPath").toString().length() > 0) {
                    resultObj.put("data", HdfsUtils.readFileToString(resultObj.get("hdfsPath").toString()));
                    resultObj.put("schema", resultObj.getOrDefault("schema", "").toString());
                }
                return resultObj;
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getResult(engineInfoAndGroupId);
            }
        }
    }

    @PostMapping(value = "/query2")
    public JSONObject execution(@RequestParam("iql") String iql) throws Exception {
        HashMap pramMap = new HashMap<String, String>();
        pramMap.put("iql", iql);
        pramMap.put("variables", "[]");
        String postResult = HttpUtils.post("http://192.168.1.233:8888/query", pramMap, 600, "utf-8");
        JSONObject jsonObject = JSON.parseObject(postResult);
        if (jsonObject.getBoolean("isSuccess")) {
            HashMap pramMap2 = new HashMap<String, String>();
            pramMap2.put("engineInfoAndGroupId", jsonObject.getString("engineInfoAndGroupId"));
            String postResult2 = HttpUtils.post("http://192.168.1.233:8888/getresult", pramMap2, 60000, "utf-8");
            return JSON.parseObject(postResult2);
        } else {
            return jsonObject;
        }
    }


    /**
     * 或者hive元数据
     */
    @RequestMapping(value = "/getActiveStreams", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getActtiveStreams() {
        JSONArray validStreams = new JSONArray();
        List<String> validEngines = JavaConversions.seqAsJavaList(ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath()));
        if (validEngines.size() == 0) {
            return null;
        } else {
            HashMap<String, String> validEnginesMap = new HashMap<>();
            validEngines.forEach(e -> validEnginesMap.put(e.split("_")[0], e.split("_")[1]));
            validEnginesMap.forEach((k, v) -> {
                ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + k + "/user/" + v);
                try {
                    Timeout timeout = new Timeout(Duration.create(2, "s"));
                    Future<Object> future = Patterns.ask(selection, new Bean.GetActiveStream(), timeout);
                    validStreams.addAll(JSON.parseArray(Await.result(future, timeout.duration()).toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return validStreams;
        }
    }

    /**
     * 根据历史查询加载结果
     */
    @PostMapping(value = "/stopStreamJob")
    public JSONObject stopStreamJob(String engineInfo, String name, String uid) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", true);
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfo);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("errorMessage", "stop query fail,no valid engine...");
            return resultObj;
        } else {
            System.out.println("StopStreamJob:" + validEngineByEngineInfo);
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
//                selection.tell(new Bean.StopSreamJob(engineInfo + "_" + name + "_" + uid), ActorRef.noSender());
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future = Patterns.ask(selection, new Bean.StopSreamJob(engineInfo + "_" + name + "_" + uid), timeout);
                resultObj.put("isSuccess", Boolean.valueOf(Await.result(future, timeout.duration()).toString()));
            } catch (Exception e) {
                resultObj.put("errorMessage", e.getMessage());
                resultObj.put("isSuccess", false);
            }
            return resultObj;
        }
    }


    /**
     * 根据历史查询加载结果
     */
    @PostMapping(value = "/loadresult")
    public JSONObject loadResult(String hdfsPath, String schema) {
        JSONObject resultObj = new JSONObject();
        try {
            resultObj.put("data", HdfsUtils.readFileToString(hdfsPath));
            resultObj.put("schema", schema);
            resultObj.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultObj.put("isSuccess", false);
            resultObj.put("errorMessage", e.getMessage());
        }
        return resultObj;
    }

    /**
     * 删除一个job
     *
     * @return
     */
    @PostMapping(value = "/stopquery")
    public JSONObject stopBatchJob(String engineInfoAndGroupId) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", true);
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfoAndGroupId.split("_")[0]);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("errorMessage", "stop query fail,no valid engine...");
            return resultObj;
        } else {
            System.out.println("StopQuery:" + validEngineByEngineInfo);
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
//				selection.tell(new Bean.CancelJob(Integer.valueOf(engineInfoAndGroupId.split("_")[1])),ActorRef.noSender());
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future = Patterns.ask(selection, new Bean.CancelJob(Integer.valueOf(engineInfoAndGroupId.split("_")[1])), timeout);
                resultObj.put("isSuccess", Boolean.valueOf(Await.result(future, timeout.duration()).toString()));
            } catch (Exception e) {
                resultObj.put("errorMessage", e.getMessage());
                resultObj.put("isSuccess", false);
            }
            return resultObj;
        }
    }

    @RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
    public void fileDownload(HttpServletResponse response, String hdfsPath, String schema, String sql, String fileType) throws Exception {
        if ("json".equals(fileType)) {
            HDFSHandler.downloadJSON(hdfsPath, response);
        } else if ("csv".equals(fileType)) {
            HDFSHandler.downloadCSV(hdfsPath, schema, response);
        } else {
            HDFSHandler.downloadExcel(hdfsPath, schema, response);
        }
    }

    /**
     * 或者hive元数据
     */
    @RequestMapping(value = "/hiveMetadata", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray hiveMetadata() {
        JSONArray resultArray = null;
        Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            return null;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future1 = Patterns.ask(selection, new Bean.HiveCatalog(), timeout);
                String result1 = Await.result(future1, timeout.duration()).toString();
                resultArray = JSON.parseArray(result1);
            } catch (Exception e) {
                return null;
            }
            return resultArray;
        }
    }

    /**
     * 或者hive元数据
     */
    @RequestMapping(value = "/autoCompletehiveMetadata", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject autoCompleteHiveMetadata() {
        JSONObject resultArray = null;
        Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            return null;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future = Patterns.ask(selection, new Bean.HiveCatalogWithAutoComplete(), timeout);
                String result = Await.result(future, timeout.duration()).toString();
                resultArray = JSON.parseObject(result);
            } catch (Exception e) {
                return null;
            }
            return resultArray;
        }
    }

    /**
     * 获取历史所有查询
     *
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getHistoryExcution(BaseBean vo) {
        List<IqlExcution> iqlExcutions = iqlExcutionRepository.findByIqlLike(vo.getSearch());
        JSONArray rows = new JSONArray();
        JSONObject res = new JSONObject();
        for (IqlExcution e : iqlExcutions) {
            rows.add(e.toJSON());
        }
        DataUtil.sort(rows, "startTime", "desc");
        res.put("total", rows.size());
        res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
        return res;
    }

    /**
     * 跟新一个IQL
     *
     * @return
     */
    @RequestMapping(value = "/saveiql", method = RequestMethod.POST)
    @ResponseBody
    public String updateIql(@RequestParam(value = "id", required = false, defaultValue = "") String id,
                            @RequestParam(value = "iql", required = false, defaultValue = "") String iql,
                            @RequestParam(value = "code", required = false, defaultValue = "") String code,
                            @RequestParam(value = "name", required = false, defaultValue = "defaultName") String name,
                            @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        if (id.equals("")) {
            saveIqlRepository.save(new SaveIql(iql, code, name, description, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        } else {
            saveIqlRepository.updateOne(iql, code, name, description, new Timestamp(System.currentTimeMillis()), Integer.valueOf(id));
        }
        return "success";
    }

    /**
     * 删除一个IQL
     *
     * @return
     */
    @RequestMapping(value = "/deleteiql", method = RequestMethod.POST)
    @ResponseBody
    public void deleteIql(@RequestParam(value = "id") String id) {
        saveIqlRepository.delete(Long.valueOf(id));
    }

    /**
     * 删除一个历史查询IQL
     *
     * @return
     */
    @RequestMapping(value = "/deletehistoryiql", method = RequestMethod.POST)
    @ResponseBody
    public void deleteHistoryIql(@RequestParam(value = "id") String id) {
        iqlExcutionRepository.delete(Long.valueOf(id));
    }

    /**
     * 获取所有保存的iql
     *
     * @param vo
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/iqls", method = RequestMethod.GET)
    public JSONObject getIqlList(BaseBean vo) throws IOException {
        JSONObject res = new JSONObject();
        List<SaveIql> iqls = saveIqlRepository.findAll();
        JSONArray rows = new JSONArray();
        for (SaveIql e : iqls) {
            rows.add(e.toJSON());
        }
        res.put("total", rows.size());
        res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
        return res;
    }

    /**
     * 获取指定节点上可用的actor
     *
     * @param engineInfo
     * @return
     */
    private String getValidEngineByEngineInfo(String engineInfo) {
        Seq<String> validEngines = ZkUtils.getChildrenFilter(zkClient, ZkUtils.validEnginePath(), engineInfo);
        return validEngines.size() == 0 ? null : validEngines.head();
    }


    @RequestMapping(value = "/appnames", method = RequestMethod.GET)
    public JSONArray getAppName() throws IOException {
        JSONArray array = new JSONArray();
        HashMap<String, String> yarnJobs = ShellUtils.getYarnJobs();
        for (Map.Entry<String, String> entry : yarnJobs.entrySet()) {
            array.add(entry.getValue() + "[" + entry.getKey() + "]");
        }
//		array.add("Monitor[application_1526662189137_0054]");
//		array.add("appnappnameappnameappnameame[appid]");
        return array;
    }

    @RequestMapping(value = "/executors", method = RequestMethod.GET)
    public JSONObject getExecutorIdsByAppName(String appname) throws IOException {
        JSONObject rObj = new JSONObject();
        JSONArray array = new JSONArray();
        String appid = appname.split("\\[")[1].split("\\]")[0];
        rObj.put("isSuccess", true);
        try {
            HashMap<String, String> executorInfo = ShellUtils.getJobInfo().get(appid);
            for (Map.Entry<String, String> entry : executorInfo.entrySet()) {
                JSONObject executorObj = new JSONObject();
                executorObj.put("executor", entry.getKey());
                executorObj.put("host", entry.getValue());
                array.add(executorObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
            rObj.put("isSuccess", false);
        }
//		JSONObject obj = new JSONObject();
//		obj.put("executor","container_1526662189137_0054_01_000005");
//		obj.put("host","dsj03");
//		array.add(obj);
        rObj.put("executors", array);
        return rObj;
    }

    /**
     * @return
     */
    @PostMapping(value = "/formatSql")
    public JSONObject formatSql(String iql) {
        String formatedIql = SQLUtils.formatHive(iql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iql", formatedIql);
        return jsonObject;
    }

}
