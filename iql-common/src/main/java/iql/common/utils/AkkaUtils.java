package iql.common.utils;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iql.common.domain.Bean;
import org.I0Itec.zkclient.ZkClient;
import scala.collection.Iterator;
import scala.collection.Seq;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaUtils {


//    private static ActorSystem actorSystem;
//
//    public static ActorSystem getActorSystem(){
//        if(actorSystem == null){
//                actorSystem = ActorSystem.create("iqlSystem", getConfig());
//        }
//        return actorSystem;
//    }

    public static Config getConfig(ZkClient zkClient) {
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("akka.loglevel", "ERROR");
//        map.put("akka.stdout-loglevel", "ERROR");

//        map.put("akka.actor.deployment.remote","akka.tcp://sampleActorSystem@127.0.0.1:2553");

        //开启akka远程调用
        map.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");

        List<String> remoteTransports = new ArrayList<String>();
        remoteTransports.add("akka.remote.netty.tcp");
        map.put("akka.remote.enabled-transports", remoteTransports);
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

        map.put("akka.remote.netty.tcp.hostname", ip);
        map.put("akka.remote.netty.tcp.port", port);

//        map.put("akka.remote.netty.tcp.maximum-frame-size", 100 * 1024 * 1024);

        //forkjoinpool默认线程数 max(min(cpu线程数 * parallelism-factor, parallelism-max), 8)
        map.put("akka.actor.default-dispatcher.fork-join-executor.parallelism-factor", "50");
        map.put("akka.actor.default-dispatcher.fork-join-executor.parallelism-max", "50");

//        logger.info("akka.remote.netty.tcp.hostname="+map.get("akka.remote.netty.tcp.hostname"));
//        logger.info("akka.remote.netty.tcp.port="+map.get("akka.remote.netty.tcp.port"));
        Config config = ConfigFactory.load();
        return ConfigFactory.parseMap(map);
    }

}
