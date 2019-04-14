package iql.web.util;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.alibaba.fastjson.JSONObject;
import iql.common.domain.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Component
public class ActorUtils {
    @Autowired
    private ActorSystem actorSystem;

    public JSONObject queryActor(String actorAddr,String actorName,Object bean){
        JSONObject resultObj = new JSONObject();
        ActorSelection selection = actorSystem.actorSelection("akka.tcp://iqlSystem@" + actorAddr + "/user/" + actorName);
        try {
            Timeout timeout = new Timeout(Duration.create(10, "s"));
            Future<Object> future = Patterns.ask(selection,bean, timeout);
            Object obj=Await.result(future, timeout.duration());
            String resultStr;
            resultObj.put("isSuccess", true);
            if(obj instanceof Bean.IQLExcution){
                Bean.IQLExcution result1 = (Bean.IQLExcution)obj;
                return result1.toJSONObjet();
            }else{
                resultStr =obj.toString();
            }
            resultObj.put("data", resultStr);

        } catch (Exception e) {
            resultObj.put("data", e.getMessage());
            resultObj.put("isSuccess", false);
        }
        return resultObj;
    }
}
