package cn.i4.iql.http.transport;

import akka.actor.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("springExtension")
public class SpringExtension implements Extension {
    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
    }
}