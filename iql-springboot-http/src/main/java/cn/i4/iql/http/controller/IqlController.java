//package cn.i4.iql.http.controller;
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSelection;
//import akka.actor.ActorSystem;
//import cn.i4.iql.domain.Bean;
//import cn.i4.iql.http.bean.primary.IqlExcution;
//import cn.i4.iql.http.bean.primary.SaveIql;
//import cn.i4.iql.http.bean.secondary.COLUMNS;
//import cn.i4.iql.http.bean.secondary.DBS;
//import cn.i4.iql.http.bean.secondary.TBLS;
//import cn.i4.iql.http.handler.HDFSHandler;
//import cn.i4.iql.http.service.primary.IqlExcutionRepository;
//import cn.i4.iql.http.service.primary.SaveIqlRepository;
//import cn.i4.iql.http.service.secondary.ColumnsRepository;
//import cn.i4.iql.http.service.secondary.DbsRepository;
//import cn.i4.iql.http.service.secondary.TablsRepository;
//import cn.i4.iql.http.util.ExportUtil;
//import cn.i4.iql.http.util.HdfsUtils;
//import cn.i4.iql.utils.ZkUtils;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.typesafe.config.Config;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.I0Itec.zkclient.ZkClient;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import scala.concurrent.Await;
//import scala.concurrent.Future;
//import akka.pattern.Patterns;
//import akka.util.Timeout;
//import scala.concurrent.duration.Duration;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Controller
//public class IqlController {
//
//    @Autowired
//    private ActorSystem actorSystem;
//    @Autowired
//    private ZkClient zkClient;
//    @Autowired
//	private IqlExcutionRepository iqlExcutionRepository;
//    @Autowired
//    private DbsRepository dbsRepository;
//    @Autowired
//    private TablsRepository tblsRepository;
//    @Autowired
//    private ColumnsRepository columnsRepository;
//    @Autowired
//    private SaveIqlRepository saveIqlRepository;
//    @Autowired
//    private Config config;
//
//    /**
//     * 执行一个IQL
//     * @param iql
//     * @param code
//     * @param descrption
//     * @return
//     */
//    @PostMapping(value="/")
//    @ResponseBody
//    public String execution(@RequestParam("iql") String iql,
//                            @RequestParam(value="code",required=false,defaultValue="") String code,
//                            @RequestParam(value="descrption",required=false,defaultValue="") String descrption) {
//        Timestamp startTime = null;
//        Bean.IQLEngine iqlEngine = selectValidEngine();
//        if(iqlEngine == null){
//            return "当前未有可用的Spark执行引擎...";
//        }else {
//            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/" + iqlEngine.name());
//            JSONObject resultObj = null;
//            try {
//                startTime = new Timestamp(System.currentTimeMillis());
//                Timeout timeout = new Timeout(Duration.create(1, "hours"));
//                Future<Object> future1 = Patterns.ask(selection, new Bean.Iql(code,iql,iqlEngine.engineId()), timeout);
//                String result1 = Await.result(future1, timeout.duration()).toString();
//                resultObj = JSON.parseObject(result1);
//                resultObj.put("startTime",startTime);
//            } catch (Exception e) {
//                resultObj.put("errorMessage",e.getMessage());
//            }
//            iqlExcutionRepository.save(new IqlExcution(iql,code,startTime,Long.valueOf(resultObj.getOrDefault("takeTime","0").toString()), resultObj.getBoolean("isSuccess"),
//                    resultObj.getOrDefault("hdfsPath","").toString(),descrption,resultObj.getOrDefault("errorMessage","").toString(),
//                    resultObj.getOrDefault("schema","").toString()));
//
//            if(resultObj.get("hdfsPath") != null && resultObj.get("hdfsPath").toString().length() > 0){
//                resultObj.put("data",HdfsUtils.readFileToString(resultObj.get("hdfsPath").toString()));
//                resultObj.put("schema",resultObj.getOrDefault("schema","").toString());
//            }
//            return resultObj.toJSONString();
//        }
//    }
//
//
//    /**
//     * 下载结果文件
//     * @return
//     */
//    @RequestMapping(value = "/downloadJson", method = RequestMethod.GET)
//    @ResponseBody
//    public void downloadResult(@RequestParam("hdfsPath") String hdfsPath, HttpServletResponse response) {
//        HDFSHandler.downloadJSON(hdfsPath,response);
//
//    }
//
//    @RequestMapping(value = "/downloadCSV", method = RequestMethod.GET)
//    @ResponseBody
//    public void downloadCSV(@RequestParam("hdfsPath") String hdfsPath,@RequestParam("schema") String schema,HttpServletResponse response) {
//        HDFSHandler.downloadCSV(hdfsPath,schema,response);
//    }
//
//    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
//    @ResponseBody
//    public void downloadExcel(@RequestParam("hdfsPath") String hdfsPath,@RequestParam("schema") String schema,HttpServletResponse response) throws Exception{
//          HDFSHandler.downloadExcel(hdfsPath,schema,response);
//    }
//
//    /**
//     * 获取所有的执行引擎
//     * @return
//     */
//    @RequestMapping(value="/getAllEngine", method= RequestMethod.GET)
//    @ResponseBody
//    public String getAllEngine() {
//        List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
//        if (engines.size() == 1) return "当前未有可用的Spark执行引擎...";
//        String hostAndPort = config.getString("akka.remote.netty.tcp.hostname") + ":" + config.getString("akka.remote.netty.tcp.port");
//        JSONArray jsonArray = new JSONArray();
//        for(Bean.IQLEngine engine : engines){
//            if (!engine.engineInfo().equals(hostAndPort)) {
//                JSONObject obj = new JSONObject();
//                obj.put("engineId", engine.engineId());
//                obj.put("info", engine.engineInfo());
//                jsonArray.add(obj);
//            }
//        }
//        return jsonArray.toJSONString();
//    }
//
//    /**
//     * 停止一个执行引擎
//     * @param engineId
//     * @return
//     */
//    @RequestMapping(value="/stop", method= RequestMethod.POST)
//    @ResponseBody
//    public String stopIQLEngine(@RequestParam("engineId") int engineId) {
//        List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
//        if (engines.size() == 1) return "当前没有运行的IQL执行引擎";
//        Bean.IQLEngine stopEngine = null;
//        for (Bean.IQLEngine engine : engines) {
//            if (engine.engineId() == engineId) {
//                stopEngine = engine;
//            }
//        }
//        for(int index = 1;index <= 3;index ++){
//            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + stopEngine.engineInfo() + "/user/actor" + index);
//            if(shakeHands(selection)){
//               selection.tell(new Bean.StopIQL(), ActorRef.noSender());
//                return "已发送Stop命令!";
//            }
//        }
//        return "未找到可用的actor连接";
//    }
//
//    /**
//     * 获取所有的数据库
//     * @return
//     */
//    @RequestMapping(value="/databases", method= RequestMethod.GET)
//    @ResponseBody
//    public String databases() {
//        List<DBS> dbs = dbsRepository.findAll();
//        JSONArray jsonArray = new JSONArray();
//        for(DBS db : dbs){
//            JSONObject obj = new JSONObject();
//            obj.put("dbname",db.getName());
//            obj.put("dbid",db.getDbId());
//            jsonArray.add(obj);
//        }
//        return jsonArray.toJSONString();
//    }
//
//    /**
//     * 通过dbId获取对应的表
//     * @param dbId
//     * @return
//     */
//    @RequestMapping(value="/tables", method= RequestMethod.GET)
//    @ResponseBody
//    public String tables(@RequestParam("dbid") Long dbId) {
//        List<TBLS> tbs = tblsRepository.findTBLSByDbId(dbId);
//        JSONArray jsonArray = new JSONArray();
//        for(TBLS tb : tbs){
//            JSONObject obj = new JSONObject();
//            obj.put("tbname",tb.getTblName());
//            obj.put("tbid",tb.getTblId());
//            jsonArray.add(obj);
//        }
//        return jsonArray.toJSONString();
//    }
//
//    /**
//     * 通过tbid获取对应的列
//     * @param tbId
//     * @return
//     */
//    @RequestMapping(value="/columns", method= RequestMethod.GET)
//    @ResponseBody
//    public String cloumns(@RequestParam("tbid") Long tbId) {
//        List<COLUMNS> columns = columnsRepository.findCOLUMNSByCdId(tbId);
//        JSONArray jsonArray = new JSONArray();
//        for(COLUMNS cl : columns){
//            jsonArray.add(cl.getColumnName() + "(" + cl.getTypeName() + ")");
//        }
//        return jsonArray.toJSONString();
//    }
//
//
//    /**
//     * 获取历史所有查询
//     * @return
//     */
//    @RequestMapping(value="/history", method= RequestMethod.GET)
//    @ResponseBody
//    public String getHistoryExcution() {
//        List<IqlExcution> iqlExcutions = iqlExcutionRepository.findAll();
//        JSONArray jsonArray = new JSONArray();
//        for(IqlExcution e : iqlExcutions) {
//            jsonArray.add(e.toJSON());
//        }
//        return jsonArray.toJSONString();
//    }
//
//    /**
//     * 保存一个IQL
//     * @param iql
//     * @param code
//     * @param name
//     * @return
//     */
//    @RequestMapping(value="/saveiql", method= RequestMethod.POST)
//    @ResponseBody
//    public String saveIql(@RequestParam("iql") String iql,
//                          @RequestParam(value="code",required=false,defaultValue="") String code,
//                          @RequestParam(value="name") String name,
//                          @RequestParam(value="description",required=false,defaultValue="") String description) {
//        saveIqlRepository.save(new SaveIql(iql,code,name,description,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis())));
//        return "success";
//    }
//
//    /**
//     * 跟新一个IQL
//     * @param iql
//     * @param code
//     * @param name
//     * @return
//     */
//    @RequestMapping(value="/updateiql", method= RequestMethod.POST)
//    @ResponseBody
//    public String updateIql(@RequestParam("id") String id,
//                          @RequestParam(value="iql",required=false,defaultValue="") String iql,
//                          @RequestParam(value="code",required=false,defaultValue="") String code,
//                          @RequestParam(value="name",required=false,defaultValue="defaultName") String name,
//                          @RequestParam(value="description",required=false,defaultValue="") String description) {
//        saveIqlRepository.updateOne(iql,code,name,description,new Timestamp(System.currentTimeMillis()),Integer.valueOf(id));
//        return "success";
//    }
//
//    /**
//     * 获取所有保存的IQL
//     * @return
//     */
//    @RequestMapping(value="/iqls", method= RequestMethod.GET)
//    @ResponseBody
//    public String savedIql() {
//        List<SaveIql> iqls = saveIqlRepository.findAll();
//        JSONArray jsonArray = new JSONArray();
//        for(SaveIql e : iqls) {
//            jsonArray.add(e.toJSON());
//        }
//        return jsonArray.toJSONString();
//    }
//
//
//
//    /**
//     * 握手请求
//     * @param selection
//     * @return
//     */
//    private boolean shakeHands(ActorSelection selection) {
//        Boolean shakeHands = true;
//        try {
//            Timeout timeout = new Timeout(Duration.create(2, "s"));
//            Future<Object> future = Patterns.ask(selection, new Bean.ShakeHands(), timeout);
//            Object result = Await.result(future, timeout.duration());
//            if(!(result instanceof Bean.ShakeHands)) shakeHands = false;
//        } catch (Exception e) {
//            shakeHands = false;
//        }
//        return shakeHands;
//    }
//
//    /**
//     * 获取有效的执行引擎
//     * @param
//     */
//    private Bean.IQLEngine selectValidEngine() {
//        List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
//        if (engines.size() == 1) return null;
//        String hostAndPort = config.getString("akka.remote.netty.tcp.hostname") + ":" + config.getString("akka.remote.netty.tcp.port");
//        List<Bean.IQLEngine> allEngine = new ArrayList<Bean.IQLEngine>();
//        for (Bean.IQLEngine engine : engines) {
//            if (!engine.engineInfo().equals(hostAndPort)) {
//                allEngine.add(engine);
//            }
//        }
//        while (!allEngine.isEmpty()){
//            Bean.IQLEngine iqlEngine = allEngine.get(new java.util.Random().nextInt(allEngine.size()));
//            for(int index = 1;index <= 3;index ++){
//                ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/actor" + index);
//                System.out.println("尝试握手Actor:" + iqlEngine.engineInfo() + " name:actor" + index);
//                if(shakeHands(selection)){
//                    iqlEngine.name("actor" + index);
//                    return iqlEngine;
//                }else {
//                    allEngine = deleteOneFromAllEngine(allEngine,iqlEngine);
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param engines
//     * @param engine
//     * @return
//     */
//    private List<Bean.IQLEngine> deleteOneFromAllEngine(List<Bean.IQLEngine> engines,Bean.IQLEngine engine) {
//        List<Bean.IQLEngine> allEngine = new ArrayList<Bean.IQLEngine>();
//        for (Bean.IQLEngine eng : engines) {
//            if (!eng.engineInfo().equals(engine.engineInfo()) && eng.engineId() != engine.engineId()) {
//                allEngine.add(engine);
//            }
//        }
//        return allEngine;
//    }
//
//    @RequestMapping("/iql")
//    public String test(){
//        return "iql";
//    }
//
//}
