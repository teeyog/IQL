package iql.common.utils;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.ConfigValue;
import iql.common.domain.Bean;
import org.I0Itec.zkclient.ZkClient;
import scala.collection.Iterator;
import scala.collection.Seq;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaUtils {

    public static Config getConfig(ZkClient zkClient) {
        String ip = "127.0.0.1";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Seq<Bean.IQLEngine> allEngineInCluster = ZkUtils.getAllEngineInCluster(zkClient);
        Iterator<Bean.IQLEngine> iterator = allEngineInCluster.iterator();
        List ids = new ArrayList<Integer>();
        List ports = new ArrayList<Integer>();
        while (iterator.hasNext()){
            Bean.IQLEngine engine = iterator.next();
            ids.add(engine.engineId());
            if(engine.engineInfo().contains(ip)){
                ports.add(Integer.valueOf(engine.engineInfo().split(":")[1]));
            }
        }
        Integer port = 2550;
        if(ports.size() != 0){
            while (ports.contains(port)){
                port += 1;
            }
        }
        Integer id = 1;
        if(ids.size() != 0) {
            while (ids.contains(id)){
                id += 1;
            }
        }

        ZkUtils.registerEngineInZk(zkClient,id,ip,port,6000,-1);

        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.actor.provider=akka.remote.RemoteActorRefProvider"))
                .withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + ip))
                .withFallback(
                        ConfigFactory.load());

        return config;
    }

}
