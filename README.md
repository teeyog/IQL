## IQL

### Hive
- 加载数据
```
select * from hive_table
```

- 保存数据
```
save tb1 as hive.test_delete2
```

### Hbase
 
##### 加载数据

- hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181
- spark.table.schema：Spark临时表对应的schema  eg: "ID:String,appname:String,count:Int"
- hbase.table.schema：Hbase表对应schema        eg: ":rowkey,0:APPNAME,0:COUNT"
- hbase.table.name：Hbase表名
- spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据）

```
load hbase.t_mbl_user_version_info 
where `spark.table.schema`="userid:String,osversion:String,toolversion:String"
	   and `hbase.table.schema`=":rowkey,info:osversion,info:toolversion" 
	   and `hbase.zookeeper.quorum`="localhost:2181"
as db1;
```

##### 保存数据

- hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181
- hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段
- bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动。     
- hbase.table.name：Hbase表名  
- hbase.table.family：列族名，默认info
- hbase.table.startKey：预分区开始key，当hbase表不存在时，会自动创建Hbase表，不带一下三个参数则只有一个分区
- hbase.table.endKey：预分区开始key
- hbase.table.numReg：分区个数

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
as tr; 
```

- 保存数据
```
save append tb as jdbc.aatest_delete;
```

### 文件操作 (其中formate可为：json、orc、csv、parquet、text)
 - 加载数据
 ```
load formate.`path` as tb1;
```

 - 保存数据
 ```
save tb1 as formate.`path` partitionBy uid coalesce 2;
```


