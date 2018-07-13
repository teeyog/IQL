package cn.mc.report.transport;

import akka.actor.ActorSystem;
import cn.i4.iql.utils.AkkaUtils;
import cn.i4.iql.utils.ZkUtils;
import com.typesafe.config.Config;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SpringExtension springExtension;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create("IQLServiceSystem",getConfig());
        springExtension.initialize(applicationContext);
        return actorSystem;
    }

    @Bean
    public ZkClient getZkClient() {
        return ZkUtils.getZkClient(ZkUtils.ZKURL());
    }

    @Bean
    public Config getConfig() {
        return AkkaUtils.getConfig();
    }

}
