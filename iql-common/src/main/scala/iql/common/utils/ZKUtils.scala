package iql.common.utils

import iql.common.domain.Bean.IQLEngine
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.{ZkMarshallingError, ZkNoNodeException, ZkNodeExistsException}
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.apache.log4j.Logger
import org.apache.zookeeper.data.Stat

import scala.collection._
import scala.util.Random

object ZkUtils {

  val log: Logger = Logger.getLogger(ZkUtils.getClass)

  var zkClient:ZkClient = null

  val enginePath = "/iql/engine"
  val validEnginePath = "/iql/valid_engine"

  def getZkClient(zkServers: String):ZkClient = {
    if(zkClient == null) {
      zkClient = new ZkClient(zkServers, 60000, 60000, new ZkSerializer {
        override def serialize(data: Object): Array[Byte] = {
          try {
            return data.toString().getBytes("UTF-8")
          } catch {
            case e: ZkMarshallingError => return null
          }
        }
        override def deserialize(bytes: Array[Byte]): Object = {
          try {
            return new String(bytes, "UTF-8")
          } catch {
            case e: ZkMarshallingError => return null
          }
        }
      })
      sys.addShutdownHook {
        // Ensure that, on executor JVM shutdown, the Kafka producer sends
        // any buffered messages to Kafka before shutting down.
        zkClient.close()
      }
    }
    zkClient
  }

  def getAllEngineInClusterASJava(zkClient: ZkClient): java.util.List[IQLEngine] = {
    import scala.collection.JavaConversions._
    val engines: java.util.List[IQLEngine] = getAllEngineInCluster(zkClient)      // 其中visitors类型为Seq[Long]类型
    engines
  }

  def getAllEngineInCluster(zkClient: ZkClient): Seq[IQLEngine] = {
    val engineIds = ZkUtils.getChildrenParentMayNotExist(zkClient, ZkUtils.enginePath).sorted
    engineIds.map(_.toInt).map(getEngineInfo(zkClient, _)).filter(_.isDefined).map(_.get)
  }

  def registerActorInEngine(zkClient: ZkClient,path:String,data:String, timeout: Int, jmxPort: Int) {
    try {
      makeSurePersistentPathExists(zkClient,validEnginePath)
      createEphemeralPathExpectConflictHandleZKBug(zkClient, path, data, data,
        (data: String, expectedData: Any) => expectedData.toString.equals(data),
        timeout)
    } catch {
      case e: ZkNodeExistsException =>
        throw new RuntimeException("A actor is already registered on the path " + path
          + ". This probably " + "indicates that you either have configured a brokerid that is already in use, or "
          + "else you have shutdown this broker and restarted it faster than the zookeeper "
          + "timeout so it appears to be re-registering.")
    }
  }

  def registerEngineInZk(zkClient: ZkClient, id: Int, host: String, port: Int, timeout: Int, jmxPort: Int) {
    val brokerIdPath = ZkUtils.enginePath + "/" + id
    val brokerInfo = host + ":" + port
    val expectedBroker = IQLEngine(id, host + ":" + port)
    try {
      makeSurePersistentPathExists(zkClient,enginePath)
      createEphemeralPathExpectConflictHandleZKBug(zkClient, brokerIdPath, brokerInfo, expectedBroker,
        (engineString: String, engine: Any) => engine.asInstanceOf[IQLEngine].engineInfo.equals(engineString),
        timeout)
    } catch {
      case e: ZkNodeExistsException =>
        throw new RuntimeException("A broker is already registered on the path " + brokerIdPath
          + ". This probably " + "indicates that you either have configured a brokerid that is already in use, or "
          + "else you have shutdown this broker and restarted it faster than the zookeeper "
          + "timeout so it appears to be re-registering.")
    }
    log.info("Registered broker %d at path %s with address %s:%d.".format(id, brokerIdPath, host, port))
  }

  /**
    *  make sure a persistent path exists in ZK. Create the path if not exist.
    */
  def makeSurePersistentPathExists(client: ZkClient, path: String) {
    if (!client.exists(path))
      client.createPersistent(path, true) // won't throw NoNodeException or NodeExistsException
  }

  /**
    *  create the parent path
    */
  private def createParentPath(client: ZkClient, path: String): Unit = {
    val parentDir = path.substring(0, path.lastIndexOf('/'))
    if (parentDir.length != 0)
      client.createPersistent(parentDir, true)
  }

  /**
    * Create an ephemeral node with the given path and data. Create parents if necessary.
    */
  private def createEphemeralPath(client: ZkClient, path: String, data: String): Unit = {
    try {
      client.createEphemeral(path, data)
    } catch {
      case e: ZkNoNodeException => {
        createParentPath(client, path)
        client.createEphemeral(path, data)
      }
    }
  }

  /**
    * Create an ephemeral node with the given path and data.
    * Throw NodeExistException if node already exists.
    */
  def createEphemeralPathExpectConflict(client: ZkClient, path: String, data: String): Unit = {
    try {
      createEphemeralPath(client, path, data)
    } catch {
      case e: ZkNodeExistsException => {
        // this can happen when there is connection loss; make sure the data is what we intend to write
        var storedData: String = null
        try {
          storedData = readData(client, path)._1
        } catch {
          case e1: ZkNoNodeException => // the node disappeared; treat as if node existed and let caller handles this
          case e2: Throwable => throw e2
        }
        if (storedData == null || storedData != data) {
         log.info("conflict in " + path + " data: " + data + " stored data: " + storedData)
          throw e
        } else {
          // otherwise, the creation succeeded, return normally
          log.info(path + " exists with value " + data + " during connection loss; this is ok")
        }
      }
      case e2: Throwable => throw e2
    }
  }

  /**
    * Create an ephemeral node with the given path and data.
    * Throw NodeExistsException if node already exists.
    * Handles the following ZK session timeout bug:
    *
    * https://issues.apache.org/jira/browse/ZOOKEEPER-1740
    *
    * Upon receiving a NodeExistsException, read the data from the conflicted path and
    * trigger the checker function comparing the read data and the expected data,
    * If the checker function returns true then the above bug might be encountered, back off and retry;
    * otherwise re-throw the exception
    */
  def createEphemeralPathExpectConflictHandleZKBug(zkClient: ZkClient, path: String, data: String, expectedCallerData: Any, checker: (String, Any) => Boolean, backoffTime: Int): Unit = {
    while (true) {
      try {
        createEphemeralPathExpectConflict(zkClient, path, data)
        return
      } catch {
        case e: ZkNodeExistsException => {
          // An ephemeral node may still exist even after its corresponding session has expired
          // due to a Zookeeper bug, in this case we need to retry writing until the previous node is deleted
          // and hence the write succeeds without ZkNodeExistsException
          ZkUtils.readDataMaybeNull(zkClient, path)._1 match {
            case Some(writtenData) => {
              if (checker(writtenData, expectedCallerData)) {
                log.info("I wrote this conflicted ephemeral node [%s] at %s a while back in a different session, ".format(data, path)
                  + "hence I will backoff for this node to be deleted by Zookeeper and retry")

                Thread.sleep(backoffTime)
              } else {
                throw e
              }
            }
            case None => // the node disappeared; retry creating the ephemeral node immediately
          }
        }
        case e2: Throwable => throw e2
      }
    }
  }

  /**
    * Create an persistent node with the given path and data. Create parents if necessary.
    */
  def createPersistentPath(client: ZkClient, path: String, data: String = ""): Unit = {
    try {
      client.createPersistent(path, data)
    } catch {
      case e: ZkNoNodeException => {
        createParentPath(client, path)
        client.createPersistent(path, data)
      }
    }
  }

  def createSequentialPersistentPath(client: ZkClient, path: String, data: String = ""): String = {
    client.createPersistentSequential(path, data)
  }

  /**
    * Update the value of a persistent node with the given path and data.
    * create parrent directory if necessary. Never throw NodeExistException.
    * Return the updated path zkVersion
    */
  def updatePersistentPath(client: ZkClient, path: String, data: String) = {
    try {
      client.writeData(path, data)
    } catch {
      case e: ZkNoNodeException => {
        createParentPath(client, path)
        try {
          client.createPersistent(path, data)
        } catch {
          case e: ZkNodeExistsException =>
            client.writeData(path, data)
          case e2: Throwable => throw e2
        }
      }
      case e2: Throwable => throw e2
    }
  }


  /**
    * Update the value of a persistent node with the given path and data.
    * create parrent directory if necessary. Never throw NodeExistException.
    */
  def updateEphemeralPath(client: ZkClient, path: String, data: String): Unit = {
    try {
      client.writeData(path, data)
    } catch {
      case e: ZkNoNodeException => {
        createParentPath(client, path)
        client.createEphemeral(path, data)
      }
      case e2: Throwable => throw e2
    }
  }

  def deletePath(client: ZkClient, path: String): Boolean = {
    try {
      client.delete(path)
    } catch {
      case e: ZkNoNodeException =>
        // this can happen during a connection loss event, return normally
        log.info(path + " deleted during connection loss; this is ok")
        false
      case e2: Throwable => throw e2
    }
  }

  def deletePathRecursive(client: ZkClient, path: String) {
    try {
      client.deleteRecursive(path)
    } catch {
      case e: ZkNoNodeException =>
        // this can happen during a connection loss event, return normally
        log.info(path + " deleted during connection loss; this is ok")
      case e2: Throwable => throw e2
    }
  }

  def maybeDeletePath(zkUrl: String, dir: String) {
    try {
      val zk = new ZkClient(zkUrl, 30*1000, 30*1000, ZKStringSerializer)
      zk.deleteRecursive(dir)
      zk.close()
    } catch {
      case _: Throwable => // swallow
    }
  }

  def readData(client: ZkClient, path: String): (String, Stat) = {
    val stat: Stat = new Stat()
    val dataStr: String = client.readData(path, stat)
    (dataStr, stat)
  }

  def readDataMaybeNull(client: ZkClient, path: String): (Option[String], Stat) = {
    val stat: Stat = new Stat()
    val dataAndStat = try {
      (Some(client.readData(path, stat)), stat)
    } catch {
      case e: ZkNoNodeException =>
        (None, stat)
      case e2: Throwable => throw e2
    }
    dataAndStat
  }

  def getChildren(client: ZkClient, path: String): Seq[String] = {
    import scala.collection.JavaConversions._
    // triggers implicit conversion from java list to scala Seq
    client.getChildren(path)
  }

  def getValidChildren(client: ZkClient, path: String): Seq[String] = {
    getChildren(client,path)
      .map(r => (r.split("_")(0),r))
      .groupBy(_._1)
      .filter(_._2.size>1)
      .flatMap(r => r._2)
      .map(_._2).toSeq
  }

  def getChildrenFilter(client: ZkClient, path: String, engineInfo:String): Seq[String] = {
    val seqEngine = getChildren(client,path).filter(_.startsWith(engineInfo))
    Random.shuffle(seqEngine)
  }

  def getChildrenParentMayNotExist(client: ZkClient, path: String): Seq[String] = {
    import scala.collection.JavaConversions._
    // triggers implicit conversion from java list to scala Seq
    try {
      client.getChildren(path)
    } catch {
      case e: ZkNoNodeException => return Nil
      case e2: Throwable => throw e2
    }
  }

  /**
    * Check if the given path exists
    */
  def pathExists(client: ZkClient, path: String): Boolean = {
    client.exists(path)
  }


  /**
    * This API takes in a broker id, queries zookeeper for the broker metadata and returns the metadata for that broker
    * or throws an exception if the broker dies before the query to zookeeper finishes
    * @param brokerId The broker id
    * @param zkClient The zookeeper client connection
    * @return An optional Broker object encapsulating the broker metadata
    */
  def getEngineInfo(zkClient: ZkClient, brokerId: Int): Option[IQLEngine] = {
    ZkUtils.readDataMaybeNull(zkClient, ZkUtils.enginePath + "/" + brokerId)._1 match {
      case Some(engineInfo) => Some(IQLEngine(brokerId, engineInfo))
      case None => None
    }
  }

}

object ZKStringSerializer extends ZkSerializer {

  @throws(classOf[ZkMarshallingError])
  def serialize(data : Object) : Array[Byte] = data.asInstanceOf[String].getBytes("UTF-8")

  @throws(classOf[ZkMarshallingError])
  def deserialize(bytes : Array[Byte]) : Object = {
    if (bytes == null)
      null
    else
      new String(bytes, "UTF-8")
  }
}

