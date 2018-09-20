package org.apache.spark.sql.execution.datasources.hbase

import org.apache.commons.pool2.{BasePooledObjectFactory, PooledObject}
import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool, GenericObjectPoolConfig}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Table}

/**
  * Created by UFO on 12/25/2017.
  */
object HBasePool {

    private var pool: GenericObjectPool[HBaseProxy] = _

    def apply(quorum: String, port: String): GenericObjectPool[HBaseProxy] = {
        HBasePool.synchronized {
            if (null == pool) {
                val poolFactory = new HBaseProxyFactory(quorum,port)
                val poolConfig = {
                    val c = new GenericObjectPoolConfig
                    c.setMaxTotal(1)
                    c.setMaxIdle(0)
                    c
                }
                pool = new GenericObjectPool[HBaseProxy](poolFactory,poolConfig)
            }
            pool
        }
    }

}

case class HBaseProxy(quorum: String, port: String) {

    lazy private val conn = {
        val config = HBaseConfiguration.create()
        config.set("hbase.zookeeper.quorum", quorum)
        config.set("hbase.zookeeper.property.clientPort", port)
        ConnectionFactory.createConnection(config)
    }

    def getTable(tableName:String): Table ={
        conn.getTable(TableName.valueOf(tableName))
    }

    // 关闭客户端
    def shutdown(): Unit = conn.close()

}


//这个类是创建池化对象的工厂类
class HBaseProxyFactory(quorum: String, port: String) extends BasePooledObjectFactory[HBaseProxy] {
    //创建实体
    override def create(): HBaseProxy = HBaseProxy(quorum, port)

    //包装实体
    override def wrap(t: HBaseProxy): PooledObject[HBaseProxy] = new DefaultPooledObject[HBaseProxy](t)

    override def destroyObject(p: PooledObject[HBaseProxy]): Unit = {
        p.getObject.shutdown()
        super.destroyObject(p)
    }
}
