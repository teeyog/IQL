package cn.i4.iql.http.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.i4.iql.domain.Bean;
import cn.i4.iql.http.bean.BaseBean;
import cn.i4.iql.http.bean.primary.IqlExcution;
import cn.i4.iql.http.bean.primary.SaveIql;
import cn.i4.iql.http.handler.HDFSHandler;
import cn.i4.iql.http.service.primary.IqlExcutionRepository;
import cn.i4.iql.http.service.primary.SaveIqlRepository;
import cn.i4.iql.http.util.DataUtil;
import cn.i4.iql.http.util.HdfsUtils;
import cn.i4.iql.utils.ZkUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.Config;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hive")
public class QueryAction {

	@Autowired
	private ActorSystem actorSystem;
	@Autowired
	private ZkClient zkClient;
	@Autowired
	private IqlExcutionRepository iqlExcutionRepository;
	@Autowired
	private SaveIqlRepository saveIqlRepository;
	@Autowired
	private Config config;

	/**
	 * 执行一个IQL
	 * @param iql
	 * @param code
	 * @param descrption
	 * @return
	 */
	@PostMapping(value="/query")
	public JSONObject execution(@RequestParam("iql") String iql,
							@RequestParam(value="code",required=false,defaultValue="") String code,
							@RequestParam(value="descrption",required=false,defaultValue="") String descrption) {
		JSONObject resultObj = null;
		Bean.IQLEngine iqlEngine = selectValidEngine();
		if(iqlEngine == null){
			resultObj = new JSONObject();
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage","当前未有可用的执行引擎...");
			return resultObj;
		}else {
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/" + iqlEngine.name());
			try {
				Timeout timeout = new Timeout(Duration.create(2, "s"));
				Future<Object> future1 = Patterns.ask(selection, new Bean.Iql(code,iql,iqlEngine.engineId()), timeout);
				String result1 = Await.result(future1, timeout.duration()).toString();
				resultObj = JSON.parseObject(result1);
				resultObj.put("isSuccess",true);
			} catch (Exception e) {
				resultObj.put("errorMessage",e.getMessage());
				resultObj.put("isSuccess",false);
			}
			return resultObj;
		}
	}

	/**
	 * 获取结果
	 */
	@PostMapping(value="/getresult")
	public JSONObject getResult(String engineIdAndGroupId) {
		JSONObject resultObj = null;
		Bean.IQLEngine iqlEngine = selectValidEngineByEngineId(Integer.valueOf(engineIdAndGroupId.split(":")[0]));
		if(iqlEngine == null){
			resultObj = new JSONObject();
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage","当前未有可用的执行引擎...");
			return resultObj;
		}else {
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/" + iqlEngine.name());
			try {
				Timeout timeout = new Timeout(Duration.create(2, "s"));
				Future<Object> future1 = Patterns.ask(selection, new Bean.GetResult(engineIdAndGroupId), timeout);
				String result1 = Await.result(future1, timeout.duration()).toString();
				resultObj = JSON.parseObject(result1);
			} catch (Exception e) {
				resultObj.put("errorMessage",e.getMessage());
			}
			if(!resultObj.getString("status").equals("RUNNING")){
				iqlExcutionRepository.save(new IqlExcution(resultObj.getString("iql"),resultObj.getString("code"),resultObj.getTimestamp("startTime"),
						Long.valueOf(resultObj.getOrDefault("takeTime","0").toString()), resultObj.getBoolean("isSuccess"),
						resultObj.getOrDefault("hdfsPath","").toString(),"",resultObj.getOrDefault("errorMessage","").toString(),
						resultObj.getOrDefault("schema","").toString()));

				if(resultObj.get("hdfsPath") != null && resultObj.get("hdfsPath").toString().length() > 0){
					resultObj.put("data", HdfsUtils.readFileToString(resultObj.get("hdfsPath").toString()));
					resultObj.put("schema",resultObj.getOrDefault("schema","").toString());
				}
				return resultObj;
			}else {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return getResult(engineIdAndGroupId);
			}
		}
	}

	/**
	 * 根据历史查询加载结果
	 */
	@PostMapping(value="/loadresult")
	public JSONObject loadResult(String hdfsPath,String schema) {
		JSONObject resultObj = new JSONObject();
		try {
			resultObj.put("data", HdfsUtils.readFileToString(hdfsPath));
			resultObj.put("schema",schema);
			resultObj.put("isSuccess",true);
		} catch (Exception e) {
			e.printStackTrace();
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage",e.getMessage());
		}
		return resultObj;
	}

	/**
	 * 删除一个job
	 * @return
	 */
	@PostMapping(value="/stopquery")
	public JSONObject cancelJob(String engineIdAndGroupId) {
		JSONObject resultObj = new JSONObject();
		resultObj.put("isSuccess",true);
		Bean.IQLEngine iqlEngine = selectValidEngineByEngineId(Integer.valueOf(engineIdAndGroupId.split(":")[0]));
		if(iqlEngine == null){
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage","stop query fail");
			return resultObj;
		}else {
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/" + iqlEngine.name());
			try {
				selection.tell(new Bean.CancelJob(Integer.valueOf(engineIdAndGroupId.split(":")[1])),ActorRef.noSender());
			} catch (Exception e) {
				resultObj.put("errorMessage",e.getMessage());
				resultObj.put("isSuccess",false);
			}
			return resultObj;
		}
	}


	@RequestMapping(value="/fileDownload", method=RequestMethod.GET)
	public void fileDownload(HttpServletResponse response, String hdfsPath, String schema, String sql, String fileType) throws Exception {
		if("json".equals(fileType)) {
			HDFSHandler.downloadJSON(hdfsPath,response);
		} else if("csv".equals(fileType)) {
			HDFSHandler.downloadCSV(hdfsPath,schema,response);
		} else {
			HDFSHandler.downloadExcel(hdfsPath,schema,response);
		}
	}

	/**
	 * 获取所有的执行引擎
	 * @return
	 */
	@RequestMapping(value="/getAllEngine", method= RequestMethod.GET)
	@ResponseBody
	public String getAllEngine() {
		List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
		if (engines.size() == 1) return "当前未有可用的Spark执行引擎...";
		String hostAndPort = config.getString("akka.remote.netty.tcp.hostname") + ":" + config.getString("akka.remote.netty.tcp.port");
		JSONArray jsonArray = new JSONArray();
		for(Bean.IQLEngine engine : engines){
			if (!engine.engineInfo().equals(hostAndPort)) {
				JSONObject obj = new JSONObject();
				obj.put("engineId", engine.engineId());
				obj.put("info", engine.engineInfo());
				jsonArray.add(obj);
			}
		}
		return jsonArray.toJSONString();
	}

	/**
	 * 停止一个执行引擎
	 * @param engineId
	 * @return
	 */
	@RequestMapping(value="/stop", method= RequestMethod.POST)
	@ResponseBody
	public String stopIQLEngine(@RequestParam("engineId") int engineId) {
		List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
		if (engines.size() == 1) return "当前没有运行的IQL执行引擎";
		Bean.IQLEngine stopEngine = null;
		for (Bean.IQLEngine engine : engines) {
			if (engine.engineId() == engineId) {
				stopEngine = engine;
			}
		}
		for(int index = 1;index <= 3;index ++){
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + stopEngine.engineInfo() + "/user/actor" + index);
			if(shakeHands(selection)){
				selection.tell(new Bean.StopIQL(), ActorRef.noSender());
				return "已发送Stop命令!";
			}
		}
		return "未找到可用的actor连接";
	}


    /**
     * 或者hive元数据
	 */
	@RequestMapping(value="/hiveMetadata", method= RequestMethod.GET)
	@ResponseBody
	public JSONArray hiveMetadata() {
        JSONArray resultArray = null;
        Bean.IQLEngine iqlEngine = selectValidEngine();
        if(iqlEngine == null){
            return null;
        }else {
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/" + iqlEngine.name());
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
	 * 获取历史所有查询
	 * @return
	 */
	@RequestMapping(value="/history", method= RequestMethod.GET)
	@ResponseBody
	public JSONObject getHistoryExcution(BaseBean vo) {
		List<IqlExcution> iqlExcutions = iqlExcutionRepository.findAll();
		JSONArray rows = new JSONArray();
		JSONObject res = new JSONObject();
		for(IqlExcution e : iqlExcutions) {
			rows.add(e.toJSON());
		}
		if(rows != null) {
			DataUtil.sort(rows, "startTime", "desc");
			res.put("total", rows.size());
			res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
		}
		return res;
	}

	/**
	 * 跟新一个IQL
	 * @param iql
	 * @param code
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/saveiql", method= RequestMethod.POST)
	@ResponseBody
	public String updateIql(@RequestParam(value="id",required=false,defaultValue="") String id,
							@RequestParam(value="iql",required=false,defaultValue="") String iql,
							@RequestParam(value="code",required=false,defaultValue="") String code,
							@RequestParam(value="name",required=false,defaultValue="defaultName") String name,
							@RequestParam(value="description",required=false,defaultValue="") String description) {
		if(id.equals("")){
			saveIqlRepository.save(new SaveIql(iql,code,name,description,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis())));
		}else{
			saveIqlRepository.updateOne(iql,code,name,description,new Timestamp(System.currentTimeMillis()),Integer.valueOf(id));
		}
		return "success";
	}

	/**
	 * 删除一个IQL
	 * @return
	 */
	@RequestMapping(value="/deleteiql", method= RequestMethod.POST)
	@ResponseBody
	public void deleteIql(@RequestParam(value="id") String id) {
		saveIqlRepository.delete(Long.valueOf(id));
	}

	/**
	 * 删除一个历史查询IQL
	 * @return
	 */
	@RequestMapping(value="/deletehistoryiql", method= RequestMethod.POST)
	@ResponseBody
	public void deleteHistoryIql(@RequestParam(value="id") String id) {
		iqlExcutionRepository.delete(Long.valueOf(id));
	}

	@RequestMapping(value="/iqls", method=RequestMethod.GET)
	public JSONObject getIqlList(BaseBean vo) throws IOException {
		JSONObject res = new JSONObject();
		List<SaveIql> iqls = saveIqlRepository.findAll();
		JSONArray rows = new JSONArray();
		for(SaveIql e : iqls) {
			rows.add(e.toJSON());
		}
		if(rows != null) {
			res.put("total", rows.size());
			res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
		}
		return res;
	}

	/**
	 * 握手请求
	 * @param selection
	 * @return
	 */
	private boolean shakeHands(ActorSelection selection) {
		Boolean shakeHands = true;
		try {
			Timeout timeout = new Timeout(Duration.create(2, "s"));
			Future<Object> future = Patterns.ask(selection, new Bean.ShakeHands(), timeout);
			Object result = Await.result(future, timeout.duration());
			if(!(result instanceof Bean.ShakeHands)) shakeHands = false;
		} catch (Exception e) {
			shakeHands = false;
		}
		return shakeHands;
	}

	/**
	 * 获取有效的执行引擎
	 * @param
	 */
	private Bean.IQLEngine selectValidEngine() {
		List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
		if (engines.size() == 1) return null;
		String hostAndPort = config.getString("akka.remote.netty.tcp.hostname") + ":" + config.getString("akka.remote.netty.tcp.port");
		List<Bean.IQLEngine> allEngine = new ArrayList<Bean.IQLEngine>();
		for (Bean.IQLEngine engine : engines) {
			if (!engine.engineInfo().equals(hostAndPort)) {
				allEngine.add(engine);
			}
		}
		while (!allEngine.isEmpty()){
			Bean.IQLEngine iqlEngine = allEngine.get(new java.util.Random().nextInt(allEngine.size()));
			for(int index = 1;index <= 3;index ++){
				ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + iqlEngine.engineInfo() + "/user/actor" + index);
				System.out.println("尝试握手Actor:" + iqlEngine.engineInfo() + " name:actor" + index);
				if(shakeHands(selection)){
					iqlEngine.name("actor" + index);
					return iqlEngine;
				}else {
					allEngine = deleteOneFromAllEngine(allEngine,iqlEngine);
				}
			}
		}
		return null;
	}

	/**
	 * 根据engineId获取可用的actor
	 * @param engineId
	 * @return
	 */
	private Bean.IQLEngine selectValidEngineByEngineId(int engineId) {
		List<Bean.IQLEngine> engines = ZkUtils.getAllEngineInClusterASJava(zkClient);
		for (Bean.IQLEngine engine : engines) {
			if (engine.engineId() == engineId) {
				for(int index = 3;index >= 1;index --){
					ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engine.engineInfo() + "/user/actor" + index);
					System.out.println("尝试握手Actor:" + engine.engineInfo() + " name:actor" + index);
					if(shakeHands(selection)){
						engine.name("actor" + index);
						return engine;
					}
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param engines
	 * @param engine
	 * @return
	 */
	private List<Bean.IQLEngine> deleteOneFromAllEngine(List<Bean.IQLEngine> engines,Bean.IQLEngine engine) {
		List<Bean.IQLEngine> allEngine = new ArrayList<Bean.IQLEngine>();
		for (Bean.IQLEngine eng : engines) {
			if (!eng.engineInfo().equals(engine.engineInfo()) && eng.engineId() != engine.engineId()) {
				allEngine.add(engine);
			}
		}
		return allEngine;
	}

	@RequestMapping("/iql")
	public String test(){
		return "iql";
	}
}
