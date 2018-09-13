package iql.web.transport;

import akka.actor.ActorSystem;
import iql.common.utils.AkkaUtils;
import com.typesafe.config.Config;
import iql.common.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AkkaConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SpringExtension springExtension;
    @Autowired
    private Environment env;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create("IQLServiceSystem",getConfig());
        springExtension.initialize(applicationContext);
        return actorSystem;
    }

    @Bean
    public ZkClient getZkClient() {
        return ZkUtils.getZkClient(env.getProperty("zkServers"));
    }

    @Bean
    public Config getConfig() {
        return AkkaUtils.getConfig(getZkClient());
    }

}
