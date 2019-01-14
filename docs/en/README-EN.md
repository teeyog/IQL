## IQL
 
![](https://upload-images.jianshu.io/upload_images/3597066-e19cdef507fd77a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

An ad hoc query service based on SparkSQL has the following characteristics：
- Elegant interaction mode, support multiple data sources, multi-data source hybrid computing
- Spark Permanent Service, Automatic Engine Discovery Based on Zookeeper
- Load balancing, random execution of multiple engines
- Implementation of Parallel Query in Multi-session Mode
- Use spark FAIR scheduling to avoid resources being monopolized by large tasks
- Spark-based dynamic resource allocation, does not occupy executor resources without tasks
- Support for Cluster and Client mode startup
- SQL dynamic add stream task based on Structured Streaming
- Similar to Spark Shell interactive data analysis
- Efficient script management, complete the association of each script with import/include syntax
- Permission verification for data source operations

Supported data sources：hdfs、hive、hbase、kafka、mysql、es、solr、mongo

Supported file formats：parquet、csv、orc、json、text、xml

Added support for Hbase, MySQL, and ES in addition to the Sink supported by Structured Streaming



## [Quickstart](https://github.com/teeyog/IQL/blob/master/docs/en/quick-start.md)
 
### HBase
 
#### load data

example：

```
load hbase.t_mbl_user_version_info 
where `spark.table.schema`="userid:String,osversion:String,toolversion:String"
	   and `hbase.table.schema`=":rowkey,info:osversion,info:toolversion" 
	   and `hbase.zookeeper.quorum`="localhost:2181"
as tb;
```

| parameter | description | defaults | 
| ------------- |:-------------:|:-------------:|
| hbase.zookeeper.quorum | zookeeper address| localhost:2181|
| spark.table.schema | spark tempview mapping schema（eg: "ID:String,appname:String,age:Int"）|  |
| hbase.table.schema| HBase table mapping schema（eg: ":rowkey,info:appname,info:age"）|  |
| spark.rowkey.view.name| The name of the spark tempview corresponding to the rowkey. After the value is set, only the data corresponding to the rowkey is obtained.  |   |

> The HBase data corresponding to the specified rowkey set can be obtained. The spark.rowkey.view.name is the tempview corresponding to the rowkey set. By default, the first column is the rowkey column.

#### save data

example：

```
save tb1 as hbase.tableName 
where `hbase.zookeeper.quorum`="localhost:2181"
      and `hbase.table.rowkey.filed`="name"
```

| parameter | description | defaults | 
| ------------- |:-------------:|:-------------:|
|hbase.zookeeper.quorum | zookeeper address| localhost:2181|
|hbase.table.rowkey.field | The field name of the rowkey of the hbase table in the spark tempview| first field |
|bulkload.enable| whether to enable bulkload| false|
|hbase.table.name | HBase table name  |   |
|hbase.table.family | column family name  |  info |
|hbase.table.region.splits | Pre-partition mode 1：specify each subsection of the pre-partition as a string array，eg: ['1','2','3']  |   |
|hbase.table.rowkey.prefix | Pre-partition mode 2：when rowkey is a number, just specify the prefix form, such as 00, you can generate 100 partitions such as 00-99  |   |
|hbase.table.startKey | startkey |   |
|hbase.table.endKey | endkey  |   |
|hbase.table.numReg | number of pre-partitions |   |
|hbase.check_table | when writing to the hbase table, do you need to check if the table exists  |  false |
|hbase.cf.ttl | ttl |  |

### MySQL
- load data
```
load jdbc.ai_log_count 
where driver="com.mysql.jdbc.Driver" 
      and url="jdbc:mysql://localhost/db?characterEncoding=utf8" 
      and user="root" 
      and password="***" 
as tb; 
```

- save data
```
save append tb as jdbc.aatest_delete;
```

### File operation (format can be json、orc、csv、parquet、text)
- load data
 ```
load format.`path` as tb;
```

- save data
```
save tb as formate.`path` partitionBy uid coalesce 2;
```

### Kafka

- batch job
 ```
load kafka.`topicName`
where maxRatePerPartition="200"
	and `group.id`="consumerGroupId"
as tb;
select * from tb;
```
| parameter | description | defaults | 
| ------------- |:-------------:|:-------------:|
| autoCommitOffset | whether to submit offset | false | 
- stream job
```
load kafka.`mc-monitor` 
where startingoffsets="latest"
	and failOnDataLoss="false"
	and `spark.job.mode`="stream" 
as tb1;

register watermark.tb1
where eventTimeCol="timestamp"
and delayThreshold="10 seconds"

select window.end as time_end,
count(1) as count
from tb1 a  
group by  window(a.timestamp,"10 seconds","10 seconds")
as tb2;

save tb2 as json.`/tmp/abc6` 
where outputMode="Append"
	and streamName="Stream"
	and duration="10"
	and sendDingDingOnTerminated="true"
	and `mail.receiver`="3146635263@qq.com"
	and checkpointLocation="/tmp/cp/cp16";
```

| parameter | description | defaults | 
| ------------- |:-------------:|:-------------:|
| spark.job.mode | job mode（batch or stream）| batch |
| mail.receiver | job failure email notification (multiple mailboxes separated by commas)|  |
| sendDingDingOnTerminated | Dingding robot notification | false |

> Stream job failure will automatically restart, can be configured by streamJobMaxAttempts (default 3 times)

### Register UDF function
```
register udf.`myupper`
where func="
	def apply(name:String)={
		name.toUpperCase
	}
";

load jsonStr.'
{"name":"ufo"}
{"name":"uu"}
{"name":"HIN"}
' as tb1;

select myupper(name) as newName from tb1;
```

### include (import equivalent) syntax, introducing script fragments through paths

![import grammar](https://upload-images.jianshu.io/upload_images/3597066-cf42197b02fbaa5c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### reference
[StreamingPro之MLSQL](https://github.com/allwefantasy/streamingpro)

[spark sql在喜马拉雅的使用之xql](https://github.com/cjuexuan/mynote/issues/21)

