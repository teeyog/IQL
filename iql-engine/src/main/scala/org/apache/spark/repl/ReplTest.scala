package org.apache.spark.repl

import akka.actor.{Actor, ActorSystem, Props}
import cn.i4.iql.TestActor
import cn.i4.iql.repl.SparkInterpreter

import scala.tools.nsc.interpreter.IMain


class HelloActor extends Actor{

    var interpreter: IMain = _

    override def preStart(): Unit = {
        super.preStart()
        val tt = new ReplTT()
        interpreter = tt.start()
    }

    override def postStop(): Unit = super.postStop()

    def receive = {
        case s:String  => interpreter.interpret(s)
        case _        => println("huh?")
    }
}

object Test1_HelloActor extends App {
    // actor need an ActorSystem
    val system = ActorSystem("HelloSystem")
    // create and start the actor
    val helloActor = system.actorOf(Props[TestActor], name="helloActor")
    // send two messages
    helloActor ! """spark.sparkContext.parallelize(Seq(("A",12),("B",13))).reduceByKey(_+_).foreach(println)"""
//    helloActor ! "what"
    // shutdown the actor system
}