## IQL
 
![](https://upload-images.jianshu.io/upload_images/3597066-e19cdef507fd77a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

参考 [spark sql在喜马拉雅的使用之xql](https://github.com/cjuexuan/mynote/issues/21)的load、select、save语法实现了一套基于spark的即席查询服务
- 优雅的交互方式，支持多种datasource/sink
- spark常驻服务，基于zookeeper的引擎自动发现
- 负载均衡，多个引擎随机执行
- 多session模式实现并行查询
- 采用spark的FAIR调度
- 基于spark的动态资源分配，在无任务的情况下不会占用executor资源
- 基于Structured Streaming实现SQL动态添加流
- 基于REPL的写代码功能，动态注册UDF函数

支持的数据源：hdfs、hive、hbase、kafka、mysql、es

支持的文件格式：parquet、csv、orc、json、text

在Structured Streaming支持的Sink之外还增加了对Hbase、MySQL、es的支持

---

### [quick-start](https://github.com/teeyog/IQL/blob/master/docs/quick-start.md)

### Hive
- 加载数据
```
select * from hive_table
```

- 保存数据
```
save tb1 as hive.table
```

### Hbase
 
##### 加载数据



- hbase.zookeeper.quorum：zookeeper地址
- spark.table.schema：Spark临时表对应的schema  eg: "ID:String,appname:String,age:Int"
- hbase.table.schema：Hbase表对应schema        eg: ":rowkey,info:appname,info:age"
- hbase.table.name：Hbase表名
- spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据）

```
load hbase.t_mbl_user_version_info 
where `spark.table.schema`="userid:String,osversion:String,toolversion:String"
	   and `hbase.table.schema`=":rowkey,info:osversion,info:toolversion" 
	   and `hbase.zookeeper.quorum`="localhost:2181"
as tb;
```

##### 保存数据

- hbase.zookeeper.quorum：zookeeper地址
- hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段
- bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动。     
- hbase.table.name：Hbase表名  
- hbase.table.family：列族名，默认info
- hbase.table.startKey：预分区开始key，当hbase表不存在时，会自动创建Hbase表，不带一下三个参数则只有一个分区
- hbase.table.endKey：预分区开始key
- hbase.table.numReg：分区个数
- hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00
- hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false

```
save tb1 as hbase.tableName 
where `hbase.zookeeper.quorum`="localhost:2181"
      and `hbase.table.rowkey.filed`="name"
```

### MySQL
- 加载数据
```
load jdbc.ai_log_count 
where driver="com.mysql.jdbc.Driver" // 默认
      and url="jdbc:mysql://localhost/db?characterEncoding=utf8" 
      and user="root" // 默认
      and password="***" //默认
as tb; 
```

- 保存数据
```
save append tb as jdbc.aatest_delete;
```

### 文件操作 (其中formate可为：json、orc、csv、parquet、text)
 - 加载数据
 ```
load formate.`path` as tb;
```

 - 保存数据
```
save tb as formate.`path` partitionBy uid coalesce 2;
```

### Kafka
 - 加载数据
 ```$xslt

load kafka.`topicName`
where maxRatePerPartition="200"
	and `group.id`="consumerGroupId"
```

### 参考
[StreamingPro 支持类SQL DSL](https://www.jianshu.com/p/b580ce1ed822)

