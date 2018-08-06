package cn.i4.iql


import akka.actor.Actor
import org.apache.spark.repl.ReplTT

import scala.tools.nsc.interpreter.IMain


class TestActor extends Actor with Logging {

  var interpreter: IMain = _

  override def preStart(): Unit = {

    val s = new ReplTT()
    interpreter = s.start()
    warn("Actor Start ...")
  }

  override def postStop(): Unit = {
    warn("Actor Stop ...")
    interpreter.close()
  }

  override def receive: Receive = {

    case s:String =>
      println(s)
    interpreter.interpret(s)
//        interpreter.execute(s)
//      getExecuteState(response)
    case _ =>
  }


}


