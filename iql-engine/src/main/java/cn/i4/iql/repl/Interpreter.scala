package cn.i4.iql.repl

import org.json4s.JObject


object Interpreter {
  abstract class ExecuteResponse

  case class ExecuteSuccess(content: JObject) extends ExecuteResponse
  case class ExecuteError(ename: String,
                          evalue: String,
                          traceback: Seq[String] = Seq()) extends ExecuteResponse
  case class ExecuteIncomplete() extends ExecuteResponse
  case class ExecuteAborted(message: String) extends ExecuteResponse
}

trait Interpreter {
  import Interpreter._

  /**
    * Start the Interpreter.
    */
  def start(): Unit

  /**
    * Execute the code and return the result, it may
    * take some time to execute.
    */
  protected[repl] def execute(code: String): ExecuteResponse

  /** Shut down the interpreter. */
  def close(): Unit
}
