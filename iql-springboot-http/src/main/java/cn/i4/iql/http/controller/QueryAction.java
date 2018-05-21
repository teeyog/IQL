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
import cn.i4.iql.utils.ZkUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scala.collection.Seq;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

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
	 * @param iql
	 * @param descrption
	 * @return
	 */
	@PostMapping(value="/query")
	public JSONObject execution(@RequestParam("iql") String iql,
							@RequestParam(value = "variables") String  variables,
							@RequestParam(value="code",required=false,defaultValue="") String code,
							@RequestParam(value="descrption",required=false,defaultValue="") String descrption) {
		JSONObject resultObj = new JSONObject();
		resultObj.put("isSuccess",false);
		if(iql.trim().equals("")) {
			resultObj.put("errorMessage","iql can't be empty...");
			return resultObj;
		}
		Seq<String> validEngines = ZkUtils.getValidChildren(zkClient, ZkUtils.validEnginePath());
		if(validEngines.size() == 0){
			resultObj.put("errorMessage","当前未有可用的执行引擎...");
			return resultObj;
		}else {
			System.out.println("Query:" + validEngines.head());
			String[] engineInfoAndActorname = validEngines.head().split("_");
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
			try {
				Timeout timeout = new Timeout(Duration.create(2, "s"));
				Future<Object> future1 = Patterns.ask(selection, new Bean.Iql(code,iql,variables), timeout);
				String result1 = Await.result(future1, timeout.duration()).toString();
				resultObj = JSON.parseObject(result1);
				resultObj.put("isSuccess",true);
			} catch (Exception e) {
				resultObj.put("errorMessage",e.getMessage());
			}
			return resultObj;
		}
	}

	/**
	 * 获取结果
	 */
	@PostMapping(value="/getresult")
	public JSONObject getResult(String engineInfoAndGroupId) {
		JSONObject resultObj  = new JSONObject();
		String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfoAndGroupId.split("_")[0]);
		if(validEngineByEngineInfo == null){
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage","当前未有可用的执行引擎...");
			return resultObj;
		}else {
			String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
			try {
				Timeout timeout = new Timeout(Duration.create(2, "s"));
				Future<Object> future1 = Patterns.ask(selection, new Bean.GetResult(engineInfoAndGroupId), timeout);
				String result1 = Await.result(future1, timeout.duration()).toString();
				resultObj = JSON.parseObject(result1);
			} catch (Exception e) {
				resultObj.put("errorMessage",e.getMessage());
			}
			if(!resultObj.getString("status").equals("RUNNING")){
				iqlExcutionRepository.save(new IqlExcution(resultObj.getString("iql"),resultObj.getString("code"),resultObj.getTimestamp("startTime"),
						Long.valueOf(resultObj.getOrDefault("takeTime","0").toString()), resultObj.getBoolean("isSuccess"),
						resultObj.getOrDefault("hdfsPath","").toString(),"",resultObj.getOrDefault("errorMessage","").toString(),
						resultObj.getOrDefault("schema","").toString(),resultObj.getString("variables")));
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
				return getResult(engineInfoAndGroupId);
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
	public JSONObject cancelJob(String engineInfoAndGroupId) {
		JSONObject resultObj = new JSONObject();
		resultObj.put("isSuccess",true);
		String validEngineByEngineInfo = getValidEngineByEngineInfo(engineInfoAndGroupId.split("_")[0]);
		if(validEngineByEngineInfo == null){
			resultObj.put("isSuccess",false);
			resultObj.put("errorMessage","stop query fail,no valid engine...");
			return resultObj;
		}else {
			System.out.println("StopQuery:" + validEngineByEngineInfo);
			String[] engineInfoAndActorname = validEngineByEngineInfo.split("_");
			ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
			try {
				selection.tell(new Bean.CancelJob(Integer.valueOf(engineInfoAndGroupId.split("_")[1])),ActorRef.noSender());
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
     * 或者hive元数据
	 */
	@RequestMapping(value="/hiveMetadata", method= RequestMethod.GET)
	@ResponseBody
	public JSONArray hiveMetadata() {
        JSONArray resultArray = null;
		Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
        if(validEngines.size() == 0){
            return null;
        }else {
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
	@RequestMapping(value="/autoCompletehiveMetadata", method= RequestMethod.GET)
	@ResponseBody
	public JSONObject autoCompleteHiveMetadata() {
		JSONObject resultArray = null;
		Seq<String> validEngines = ZkUtils.getChildren(zkClient, ZkUtils.validEnginePath());
		if(validEngines.size() == 0){
			return null;
		}else {
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
	 * @return
	 */
	@RequestMapping(value="/history", method= RequestMethod.GET)
	@ResponseBody
	public JSONObject getHistoryExcution(BaseBean vo) {
		List<IqlExcution> iqlExcutions = iqlExcutionRepository.findByIqlLike(vo.getSearch());
		JSONArray rows = new JSONArray();
		JSONObject res = new JSONObject();
		for(IqlExcution e : iqlExcutions) {
			rows.add(e.toJSON());
		}
		DataUtil.sort(rows, "startTime", "desc");
		res.put("total", rows.size());
		res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
		return res;
	}

	/**
	 * 跟新一个IQL
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

	/**
	 * 获取所有保存的iql
	 * @param vo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/iqls", method=RequestMethod.GET)
	public JSONObject getIqlList(BaseBean vo) throws IOException {
		JSONObject res = new JSONObject();
		List<SaveIql> iqls = saveIqlRepository.findAll();
		JSONArray rows = new JSONArray();
		for(SaveIql e : iqls) {
			rows.add(e.toJSON());
		}
		res.put("total", rows.size());
		res.put("rows", DataUtil.pageFormat(rows, vo.getOffset(), vo.getLimit()));
		return res;
	}

	/**
	 * 获取指定节点上可用的actor
	 * @param engineInfo
	 * @return
	 */
	private String getValidEngineByEngineInfo(String engineInfo) {
		Seq<String> validEngines = ZkUtils.getChildrenFilter(zkClient, ZkUtils.validEnginePath(),engineInfo);
		return validEngines.size() == 0 ? null : validEngines.head();
	}
}
