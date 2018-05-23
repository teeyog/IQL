package cn.i4.iql

import org.slf4j.LoggerFactory


trait Logging {
  lazy val logger = LoggerFactory.getLogger(this.getClass)

  def trace(message: => Any): Unit = {
    if (logger.isTraceEnabled) {
      logger.trace(message.toString)
    }
  }

  def debug(message: => Any): Unit = {
    if (logger.isDebugEnabled) {
      logger.debug(message.toString)
    }
  }

  def info(message: => Any): Unit = {
    if (logger.isInfoEnabled) {
      logger.info(message.toString)
    }
  }

  def warn(message: => Any): Unit = {
    logger.warn(message.toString)
  }

  def warn(message: => Any, t: Throwable): Unit = {
    logger.warn(message.toString, t)
  }

  def error(message: => Any, t: Throwable): Unit = {
    logger.error(message.toString, t)
  }

  def error(message: => Any): Unit = {
    logger.error(message.toString)
  }
}

