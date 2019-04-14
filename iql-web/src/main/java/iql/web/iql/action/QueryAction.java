package iql.web.iql.action;

import iql.common.domain.Bean;
import iql.common.utils.ShellUtils;
import iql.common.utils.ZkUtils;
import iql.web.bean.BaseBean;
import iql.web.bean.IqlExcution;
import iql.web.handler.HDFSHandler;
import iql.web.system.domain.User;
import iql.web.system.service.UserService;
import iql.web.util.ActorUtils;
import iql.web.util.DataUtil;
import iql.web.util.HdfsUtils;
import iql.web.iql.service.IqlExcutionRepository;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import iql.web.util.MD5Util;
import org.I0Itec.zkclient.ZkClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class QueryAction {

    @Autowired
    private ZkClient zkClient;
    @Autowired
    private IqlExcutionRepository iqlExcutionRepository;
    @Autowired
    private ActorUtils actorUtils;
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    /**
     * 执行一个IQL
     */
    @PostMapping(value = "/query")
    public JSONObject execution(HttpServletRequest request, String iql, String mode, String variables, @RequestParam(defaultValue = "") String tag) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", false);
        if (iql.trim().equals("")) {
            resultObj.put("data", "iql can't be empty...");
            return resultObj;
        }

        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            user = userService.findUserByToken(request.getSession().getAttribute("token").toString());
        }

        Seq<String> validEngines = ZkUtils.getValidChildren(zkClient, ZkUtils.validEnginePath(), tag);
        if (validEngines.size() == 0) {
            resultObj.put("data", "There is no available execution engine....");
            return resultObj;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");
            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.Iql(mode, iql, variables, user.getToken()));
            return resultObj;
        }
    }

    /**
     * 获取结果
     */
    @PostMapping(value = "/getresult")
    public JSONObject getResult(HttpServletRequest request, String engineInfoAndGroupId) {
        JSONObject resultObj = new JSONObject();
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfoAndGroupId.split("_")[0]);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("data", "当前未有可用的执行引擎...");
            return resultObj;
        } else {
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");

            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.GetBatchResult(engineInfoAndGroupId));
            if (!resultObj.getString("status").equals("RUNNING")) {
                User user = (User) request.getSession().getAttribute("user");
                String userName;
                if (null != user) {
                    userName = user.getUsername();
                } else {
                    userName = userService.findUserByToken(request.getSession().getAttribute("token").toString()).getUsername();
                }
                resultObj.put("user", userName);
                resultObj.put("success", resultObj.getBooleanValue("isSuccess"));
                iqlExcutionRepository.save(JSONObject.toJavaObject(resultObj, IqlExcution.class));
                return resultObj;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getResult(request, engineInfoAndGroupId);
            }
        }
    }

    @PostMapping(value = "/queryApi")
    public JSONObject execution(HttpServletRequest request, String iql, @RequestParam(required = false) String tag) throws Exception {
        String postResult = Request.Post("http://localhost:8888/query").bodyForm(
                Form.form()
                        .add("iql", iql)
                        .add("variables", "[]")
                        .add("mode", "iql")
                        .add("token", request.getParameter("token"))
                        .add("tag", tag)
                        .build())
                .execute().returnContent().asString();

        JSONObject jsonObject = JSON.parseObject(postResult);
        if (jsonObject.getBoolean("isSuccess")) {
            String postResult2 = Request.Post("http://localhost:8888/getresult").bodyForm(
                    Form.form()
                            .add("engineInfoAndGroupId", jsonObject.getString("engineInfoAndGroupId"))
                            .add("token", request.getParameter("token"))
                            .build())
                    .execute().returnContent().asString();
            return JSON.parseObject(postResult2);
        } else {
            return jsonObject;
        }
    }

    /**
     * 获取活跃StreamJobs
     */
    @RequestMapping(value = "/getActiveStreams", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getActtiveStreams() {
        JSONObject res = new JSONObject();
        JSONArray validStreams = new JSONArray();
        List<String> validEngines = JavaConversions.seqAsJavaList(ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath()));
        if (validEngines.size() == 0) {
            return null;
        } else {
            HashMap<String, String> validEnginesMap = new HashMap<>();
            validEngines.forEach(e -> validEnginesMap.put(e.split("_")[0], e.split("_")[1]));
            validEnginesMap.forEach((k, v) -> {
                JSONObject resultObj = actorUtils.queryActor(k, v, new Bean.GetActiveStream());
                if ((Boolean) resultObj.get("isSuccess")) {
                    validStreams.addAll(JSON.parseArray((String) resultObj.get("data")));
                }
            });
        }
        res.put("total", validStreams.size());
        res.put("rows", validStreams);
        return res;
    }

    /**
     * 获取StreamJob状态
     */
    @RequestMapping(value = "/getStreamStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getStreamStatus(String engineInfo, String name, String uid) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", true);
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfo);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("data", "get stream staus fail,no valid engine...");
            return resultObj;
        } else {
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.StreamJobStatus(engineInfo + "_" + name + "_" + uid));
            return resultObj;
        }
    }

    /**
     * 停止StreamJob
     */
    @PostMapping(value = "/stopStreamJob")
    public JSONObject stopStreamJob(String engineInfo, String name, String uid) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", true);
        String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfo);
        if (validEngineByEngineInfo == null) {
            resultObj.put("isSuccess", false);
            resultObj.put("data", "stop query fail,no valid engine...");
            return resultObj;
        } else {
            System.out.println("StopStreamJob:" + validEngineByEngineInfo);
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.StopSreamJob(engineInfo + "_" + name + "_" + uid));
            if (resultObj.getBoolean("isSuccess")) {
                resultObj.put("isSuccess", resultObj.getBoolean("data"));
            }
            return resultObj;
        }
    }

    /**
     * 根据历史查询加载结果
     */
    @PostMapping(value = "/loadresult")
    public JSONObject loadResult(String hdfsPath, String schema, String dataType, Long id) {
        JSONObject resultObj = new JSONObject();
        try {
            if (dataType.equals("structuredData")) {
                resultObj.put("data", HdfsUtils.readFileToString(hdfsPath, env.getProperty("hdfs.uri")));
                resultObj.put("schema", schema);
                resultObj.put("isSuccess", true);
            } else if (dataType.equals("errorData")) {
                resultObj.put("data", iqlExcutionRepository.findOne(id).getData());
                resultObj.put("isSuccess", false);
            } else {
                resultObj.put("data", iqlExcutionRepository.findOne(id).getData());
                resultObj.put("isSuccess", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultObj.put("isSuccess", false);
            resultObj.put("data", e.getMessage());
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
            resultObj.put("data", "stop query fail,no valid engine...");
            return resultObj;
        } else {
            System.out.println("StopQuery:" + validEngineByEngineInfo);
            String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");

            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.CancelJob(Integer.valueOf(engineInfoAndGroupId.split("_")[1])));
            if (resultObj.getBoolean("isSuccess")) {
                resultObj.put("isSuccess", resultObj.getBoolean("data"));
            }
            return resultObj;
        }
    }

    @RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
    public void fileDownload(HttpServletResponse response, String hdfsPath, String schema, String sql, String fileType) throws Exception {
        if ("json".equals(fileType)) {
            HDFSHandler.downloadJSON(hdfsPath, response, env.getProperty("hdfs.uri"));
        } else if ("csv".equals(fileType)) {
            HDFSHandler.downloadCSV(hdfsPath, schema, response, env.getProperty("hdfs.uri"));
        } else {
            HDFSHandler.downloadExcel(hdfsPath, schema, response, env.getProperty("hdfs.uri"));
        }
    }

    /**
     * 获取hive元数据
     */
    @RequestMapping(value = "/hiveMetadata", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray hiveMetadata() {
        JSONArray resultArray = new JSONArray();
        Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            return resultArray;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");
            JSONObject resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.HiveCatalog());
            if (resultObj.getBoolean("isSuccess")) {
                resultArray = JSON.parseArray(resultObj.getString("data"));
            }
            return resultArray;
        }
    }

    /**
     * hive元数据自动补全
     */
    @RequestMapping(value = "/autoCompletehiveMetadata", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject autoCompleteHiveMetadata() {
        JSONObject resultObj = null;
        Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            return null;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");

            resultObj = actorUtils.queryActor(engineInfoAndActorname[0], engineInfoAndActorname[1],
                    new Bean.HiveCatalogWithAutoComplete());
            if (resultObj.getBoolean("isSuccess")) {
                resultObj = JSON.parseObject(resultObj.getString("data"));
            }
            return resultObj;
        }
    }

    /**
     * 获取历史所有查询
     *
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getHistoryExcution(HttpServletRequest request, BaseBean vo) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            user = userService.findUserByToken(request.getSession().getAttribute("token").toString());
        }
        List<IqlExcution> iqlExcutions = iqlExcutionRepository
                .findByUserAndIqlLike(user.getUsername(), vo.getSearch());
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
     * 删除一个历史查询IQL
     *
     * @return
     */
    @RequestMapping(value = "/deletehistoryiql", method = RequestMethod.POST)
    @ResponseBody
    public void deleteHistoryIql(String id) {
        iqlExcutionRepository.delete(Long.valueOf(id));
    }

    /**
     * 获取分享连接
     *
     * @return
     */
    @RequestMapping(value = "/shareResult", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject shareResult(String id) {
        String sign = getRealSign(id);;
        String getUrl = getUrl(id,sign);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("urlSign",getUrl);
        return jsonObject;
    }

    /**
     * 通过分享获取结果
     *
     * @return
     */
    @PostMapping(value = "/getdata")
    public JSONObject getData(String urlsign) {
        String searchURL = urlsign.substring(1);
        String id = searchURL.split("&")[0].split("=")[1];
        String sign = searchURL.split("&")[1].split("=")[1];
        String realSign = getRealSign(id);

        JSONObject resultObj = new JSONObject();
        if(realSign.equals(sign)){
            IqlExcution iqlExcution = iqlExcutionRepository.findOne(Long.valueOf(id));
            String dataType = iqlExcution.getDataType();
            try {
                if (dataType.equals("structuredData")) {
                    resultObj.put("data", HdfsUtils.readFileToString(iqlExcution.getHdfsPath(), env.getProperty("hdfs.uri")));
                    resultObj.put("schema", iqlExcution.getSchema());
                    resultObj.put("isSuccess", true);
                } else if (dataType.equals("errorData")) {
                    resultObj.put("data", iqlExcution.getData());
                    resultObj.put("isSuccess", false);
                } else {
                    resultObj.put("data", iqlExcution.getData());
                    resultObj.put("isSuccess", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultObj.put("isSuccess", false);
                resultObj.put("data", e.getMessage());
            }
        }
        return resultObj;
    }

    private String getRealSign(String jobid){
        IqlExcution iqlExcution = iqlExcutionRepository.findOne(Long.valueOf(jobid));
        String userName = iqlExcution.getUser();
        User user = new User();
        user.setUsername(userName);
        return MD5Util.getMD5String(userService.findByUsername(user).getToken());
    }



    /**
     * 功能：拼接URL
     */
    private String getUrl(String id,String sign){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        addr.getHostAddress();
        String urlSign = addr.getHostAddress()+":" + env.getProperty("server.port") + "/share?id="+id + "&sign=" + sign;
        return urlSign;
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
        rObj.put("executors", array);
        return rObj;
    }

    @PostMapping(value = "/formatSql")
    public JSONObject formatSql(String iql) {
        String formatedIql = SQLUtils.formatHive(iql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iql", formatedIql);
        return jsonObject;
    }

}
