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
- 加载数据
```
load hbase.t_mbl_user_version_info 
where `spark.table.schema`="userid:String,osversion:String,toolversion:String"
	   and `hbase.table.schema`=":rowkey,info:osversion,info:toolversion" 
	   and `hbase.zookeeper.quorum`="dsj01:2181"
as db1;
```

- 保存数据
```
save tb1 as hbase.test_delete2 
where `hbase.zookeeper.quorum`="dsj01:2181"
      and `hbase.table.rowkey.filed`="name"                       // 默认为第一个字段
      and `hbase.table.startKey`="00000000000000000000000000000000" // 有表的情况下不需要一下三个字段
      and `hbase.table.endKey`="ffffffffffffffffffffffffffffffff"
      and `hbase.table.numReg`="12" 
```

### MySQL
- 加载数据
```
load jdbc.ai_log_count 
where driver="com.mysql.jdbc.Driver" // 默认
      and url="jdbc:mysql://192.168.1.233/logweb-pro?characterEncoding=utf8" //默认
      and user="root" // 默认
      and password="123456" //默认
as tr; 
```

- 保存数据
```$xslt
save append tb as jdbc.aatest_delete;
```

### 文件操作 (其中formate可为：json、orc、csv、parquet、text)
 - 加载数据
 ```$xslt
load formate.`path` as tb1;
```

 - 保存数据
 ```$xslt
save tb1 as formate.`path` partitionBy uid coalesce 2;
```


