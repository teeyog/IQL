package iql.web.iql.action;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import iql.common.domain.Bean;
import iql.common.utils.ZkUtils;
import iql.web.bean.DataSource;
import iql.web.iql.service.DataSourceRepository;
import iql.web.system.domain.User;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.collection.Seq;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthAction {

    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private ZkClient zkClient;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @RequestMapping(value = "/checkAuth")
    public String checkAuth(HttpServletRequest request, String data) {
        User user = (User) request.getSession().getAttribute("user");
        List<DataSource> roleDataSourceByUserId = dataSourceRepository.findRoleDataSourceByUserId(user.getId());
        HashSet<String> availableDataSources = new HashSet<>();
        roleDataSourceByUserId.forEach( ds -> availableDataSources.add(ds.getPath().replaceAll("datasource.","").toLowerCase()));
        JSONArray returnArray = new JSONArray();
        Iterator<Object> iterator = JSON.parseArray(data).iterator();
        while (iterator.hasNext()){
            JSONObject obj = JSON.parseObject(iterator.next().toString());
            String chechTable;
            if(obj.getString("db").equals("")){
                chechTable = obj.getString("type") + "." + obj.getString("table");
            }else{
                chechTable = obj.getString("type") + "." + obj.getString("db") + "." + obj.getString("table");
            }
            if(!availableDataSources.contains(chechTable.toLowerCase())){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("granted",false);
                jsonObject.put("msg","You do not have permission to operate the table: " + chechTable);
                returnArray.add(jsonObject);
            }
        }
        return returnArray.toJSONString();
    }

    @RequestMapping(value = "/hbaseTables")
    public JSONObject hbaseTables(String zookeeper) {
        JSONObject jsonObject = pullData(new Bean.HbaseTables(zookeeper));
        return jsonObject;
    }

    @RequestMapping(value = "/hiveTables")
    public JSONObject hiveTables() {
        JSONObject jsonObject = pullData(new Bean.HiveTables());
        return jsonObject;
    }

    public JSONObject pullData(Object message) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("isSuccess", false);
        Seq<String> validEngines = ZkUtils.getValidChildren(zkClient, ZkUtils.validEnginePath());
        if (validEngines.size() == 0) {
            resultObj.put("errorMessage", "There is no available execution engine....");
            return resultObj;
        } else {
            String[] engineInfoAndActorname = validEngines.head().split("_");
            ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + engineInfoAndActorname[0] + "/user/" + engineInfoAndActorname[1]);
            try {
                Timeout timeout = new Timeout(Duration.create(2, "s"));
                Future<Object> future1 = Patterns.ask(selection, message, timeout);
                resultObj.put("result", Await.result(future1, timeout.duration()));
                resultObj.put("isSuccess", true);
            } catch (Exception e) {
                resultObj.put("errorMessage", e.getMessage());
            }
            return resultObj;
        }
    }

}
