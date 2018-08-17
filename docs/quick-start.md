
# quick-start
### web端
- 配置application.properties，mysql连接信息和zookeeper地址
```
spring.datasource.druid.url=jdbc:mysql://localhost:3306/iql?useUnicode=true&characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.druid.username=root
spring.datasource.druid.password=123456
# 通过zk发现引擎地址
zkServers=localhost:2181
```

- 导入SQL表

[iql.sql](https://github.com/teeyog/IQL/blob/master/docs/file/iql.sql)


- 连接hdfs配置
```
方式一：把hdfs的配置文件往资源目录resource里放一份 core-site.xml
方式二：获取FileSystem实例的时候指定地址

```

### engine端

- FAIR 公平调度

[fairscheduler.xml](https://github.com/teeyog/IQL/blob/master/docs/file/fairscheduler.xml)
```$xslt
由于spark引擎只能以client模式启动，需将自定义的调度池文件存放在启动spark的那台节点上即可，记得配置属性spark.scheduler.allocation.file。
```

- 默认配置文件

```
默认配置文件信息在hdfs目录：/data/resource/iql-default.properties，可在PropsUtils类中更改；

默认配置文件配置的都是一些常用的环境信息，比如加载mysql的数据，避免每次都写一遍连接信息

jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/logweb-pro?user=root&password=123456&useUnicode=true&characterEncoding=UTF8&useSSL=false

kafka.metadata.broker.list=localhost:9092
kafka.zookeeper.connect=localhost:2181

es.nodes=localhost
es.port=9200

hbase.zookeeper.quorum=localhost:2181
# 引擎启动向zk注册地址
zkServers=localhost:2181
```
