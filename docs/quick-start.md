
# Quick-start
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
 
### engine端

- FAIR 公平调度

[fairscheduler.xml](https://github.com/teeyog/IQL/blob/master/docs/file/fairscheduler.xml)
``` 
由于spark引擎只能以client模式启动，需将自定义的调度池文件存放在启动spark的那台节点上即可，可在启动参数中配置 --flies /path/fairscheduler.xml，也可直接在程序中配置好。
```

- 默认配置文件 iql.properties

``` 
默认配置文件配置的都是一些常用的环境信息，比如加载mysql的数据，避免每次都写一遍连接信息

# web服务地址，用于import语法回调SQL代码片段 
iql.server.address=http://192.168.1.40:8888

# iql任务需要操作的mysql默认地址（不是web后台的mysql地址）
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://dsj01:3306/job?user=root&password=123456&useUnicode=true&characterEncoding=UTF8&useSSL=false

# iql任务需要操作的kafka默认地址
kafka.metadata.broker.list=dsj01:9092
kafka.zookeeper.connect=dsj01:2181

# iql任务需要操作的es默认地址
es.nodes=localhost
es.port=9200

# iql任务需要操作的HBase默认地址
hbase.zookeeper.quorum=dsj01:2181

# 引擎启动向zk注册地址
zkServers=dsj01:2181

# 用于结果下载
hdfs.url=hdfs://dsj01:8020
```
