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
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.collection.Seq;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Iterator;


@RestController
@RequestMapping("/auth")
public class AuthAction {

    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private ZkClient zkClient;

    @RequestMapping(value = "/checkAuth")
    public String checkAuth(String data) {
        System.out.println(data);
        JSONArray returnArray = new JSONArray();
        Iterator<Object> iterator = JSON.parseArray(data).iterator();
        while (iterator.hasNext()){
            JSONObject jsonObject = new JSONObject();
            JSONObject obj = JSON.parseObject(iterator.next().toString());
            jsonObject.put("granted",false);
            jsonObject.put("msg","You do not have permission to operate the table: " + obj.getString("type") + "." + obj.getString("db") + "." + obj.getString("table"));
            returnArray.add(jsonObject);
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
