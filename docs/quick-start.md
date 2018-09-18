
# Quick-start
## WEB端
- 配置application.properties，mysql连接信息和zookeeper地址
```
spring.datasource.druid.url=jdbc:mysql://localhost:3306/iql?useUnicode=true&characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.druid.username=root
spring.datasource.druid.password=123456

...

# 通过zk发现引擎地址
zkServers=localhost:2181
```

- 导入SQL表

[iql.sql](https://github.com/teeyog/IQL/blob/master/docs/file/iql.sql)

> 打jar包，运行 java -jar iql.jar
 
## Engine端

- FAIR 公平调度

[fairscheduler.xml](https://github.com/teeyog/IQL/blob/master/docs/file/fairscheduler.xml)
``` 
可在spark-submit启动参数中配置 --flies /path/fairscheduler.xml；
也可直接在SparkConf中指定参数：spark.scheduler.allocation.file
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

- 启动示例

```
sudo -u hdfs spark2-submit \
	--name IQL \
	--master yarn \
	--deploy-mode client \
	--conf spark.executor.extraJavaOptions="-Xms1024m  -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m" \
	--files /home/runtime_file/fairscheduler.xml \
	--num-executors 1 \
	--driver-memory 3G \ 
	--executor-memory 1g \   
	--class iql.engine.main.IqlMain \
	/home/run/jobJar/iql-engine.jar
```
