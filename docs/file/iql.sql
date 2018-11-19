/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.7.17-log : Database - iql
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`iql` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

/*Table structure for table `iql_excution` */

DROP TABLE IF EXISTS `iql_excution`;

CREATE TABLE `iql_excution` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `iql` longtext COLLATE utf8mb4_unicode_ci,
  `mode` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_success` bit(1) NOT NULL,
  `result_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `take_time` bigint(20) DEFAULT NULL,
  `user` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_message` longtext COLLATE utf8mb4_unicode_ci,
  `content` longtext COLLATE utf8mb4_unicode_ci,
  `table_schema` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `variables` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=2908 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `iql_excution` */

insert  into `iql_excution`(`id`,`iql`,`mode`,`is_success`,`result_path`,`start_time`,`take_time`,`user`,`error_message`,`content`,`table_schema`,`variables`) values (2906,'register udf.`myupper`\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` \nas tb1;\n\nselect myupper(name) as newName from tb1;','iql','','/tmp/iql/result/iql_query_result_1537519143927','2018-09-21 08:39:00',9,'superadmin',NULL,NULL,'newName','[]'),(2907,'import mc.spark.udf.myupper;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` as tb1;\n\nselect myupper(name) as upperName,mylower(name) as lowerName from tb1;','iql','','/tmp/iql/result/iql_query_result_1537519169109','2018-09-21 08:39:28',2,'superadmin',NULL,NULL,'upperName,lowerName','[]');

/*Table structure for table `job_script` */

DROP TABLE IF EXISTS `job_script`;

CREATE TABLE `job_script` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pid` int(11) NOT NULL,
  `script` text,
  `isparent` tinyint(4) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=780 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `job_script` */

insert  into `job_script`(`id`,`name`,`pid`,`script`,`isparent`,`sort`,`path`) values (1,'mc',0,'',1,1,'mc'),(4,'Update MySQL',724,'select 11 as id, \"test\" as name,22 as age \nfrom i4.app \nlimit 1 \nas tb;\n\nsave update tb as jdbc.user ',0,5,'mc.demo.Update MySQL'),(777,'Code',724,'spark.udf.register(\"getModel\",(a:String) => {\n      if(a.toLowerCase.startsWith(\"ipho\")) 1\n      else if(a.toLowerCase.startsWith(\"ipad\")) 2\n      else 3\n    });\n\nspark.sql(\"\"\"select idfa,getModel(model) as modfdel from mc.mbl where date=\'20180908\' limit 10\"\"\").show(false)',0,12,'mc.demo.Code'),(714,'spark',1,NULL,1,1,'mc.spark'),(715,'udf',714,NULL,1,1,'mc.spark.udf'),(724,'demo',1,NULL,1,6,'mc.demo'),(726,'Read Kafka',724,'load kafka.`i4-sdk` \nwhere maxRatePerPartition=\"200\"\n   and `group.id`=\"consumer_sdk_offline\" \nas tb;\n   \nselect * from tb;   \n',0,2,'mc.demo.Read Kafka'),(727,'Write HBase',724,'load json.`/middata/sdk/member.log` \nas tb;\n\nselect reverse(CONCAT(\'0\',cast(userid as string))) as userid,time \nfrom tb \nas tb2;\n\nsave tb2 as hbase.`t_sdk_register_user`\n \n--  写Hbase \n-- hbase.zookeeper.quorum：zookeeper地址\n-- hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段\n-- bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动。     \n-- hbase.table.name：Hbase表名  \n-- hbase.table.family：列族名，默认info\n-- hbase.table.region.splits：预分区分区段，以数组字符串方式指定，如 [\'1\',\'2\',\'3\'] (预分区指定分区段优先级最高)\n-- hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00，在startKey和endKey都未设置的情况下会生成00-99等100个分区\n-- hbase.table.startKey：预分区开始key，当hbase表不存在时，会自动创建Hbase表，不带一下三个参数则只有一个分区\n-- hbase.table.endKey：预分区开始key\n-- hbase.table.numReg：分区个数\n-- hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false',0,6,'mc.demo.Write HBase'),(729,'Read HBase',724,'load hbase.t_sdk_consume\nwhere `spark.table.schema`=\"userid:String,time:Long\"\n	   and `hbase.table.schema`=\":rowkey,info:time\" \nas tb;\n\nselect * from tb limit 100;\n\n--  读Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 localhost:2181 \n--  spark.table.schema：Spark临时表对应的schema eg: \"ID:String,appname:String,count:Int\" \n--  hbase.table.schema：Hbase表对应schema eg: \":rowkey,0:APPNAME,0:COUNT\" \n--  hbase.table.name：Hbase表名 \n--  spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据） \n  ',0,7,'mc.demo.Read HBase'),(779,'t4',733,'import mc.test.t3;\n\nselect * from tb1;',0,8,'mc.test.t4'),(778,'t3',733,'load hbase.t_mbl_user_info\nwhere `spark.table.schema`=\"idfa:String,version:String\"\n     and `hbase.table.schema`=\":rowkey,info:version\"\nas tb;\n\nselect * from tb limit 100 as tb1; \n ',0,7,'mc.test.t3'),(730,'JSON->hive',724,'load json.`/data/sdkscore/score_20180514.log` \nas tb;\n\nselect userid,number,score,time,from_unixtime(substr(time,0,10),\'yyyyMMdd\') date,source \nfrom tb \nas tb1;\n\nINSERT OVERWRITE TABLE i4.sdk_score PARTITION (date,source)\nSELECT userid,number,score,time,date,source FROM tb1\nDISTRIBUTE BY date,source;',0,8,'mc.demo.JSON->hive'),(731,'Read ES',724,'load es.`i4-monitor/metrics/` \nwhere `es.nodes`=\"192.168.1.232\"\n	and `es.port`=\"9200\" \nas tb;\n\nselect * from tb limit 10;',0,9,'mc.demo.Read ES'),(732,'Structured Streaming',724,'load kafka.`i4-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" \nas tb1;\n	\nselect * from tb1 \nas table21;	\n	\nsave table21 as json.`/tmp/abc2` \nwhere mode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";',0,10,'mc.demo.Structured Streaming'),(733,'test',1,'load kafka.`i4-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" \nas tb1;\n	\nselect * from tb1 \nas table21;	\n	\nsave table21 as json.`/tmp/abc2` \nwhere mode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";',1,7,'mc.test'),(735,'myupper',715,'register udf.myupper\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nregister udf.mylower\nwhere func=\n```\n	def apply(name:String)={\n		name.toLowerCase\n	}\n```;',0,1,'mc.spark.udf.myupper'),(736,'Use UDF',724,'register udf.`myupper`\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` \nas tb1;\n\nselect myupper(name) as newName from tb1;',0,11,'mc.demo.Use UDF'),(771,'job',1,NULL,0,2,'mc.job'),(762,'t2',733,'import mc.spark.udf.myupper;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` as tb1;\n\nselect myupper(name) as upperName,mylower(name) as lowerName from tb1;',0,6,'mc.test.t2');

/*Table structure for table `save_iql` */

DROP TABLE IF EXISTS `save_iql`;

CREATE TABLE `save_iql` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `iql` longtext COLLATE utf8mb4_unicode_ci,
  `mode` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `save_iql` */

insert  into `save_iql`(`id`,`iql`,`mode`,`name`,`description`,`create_time`,`update_time`) values (10,'load kafka.`i4-sdk` \nwhere maxRatePerPartition=\"200\"\n   and `group.id`=\"consumer_sdk_offline\" as tb;\n   \nselect * from tb;   \n','iql','kafka数据查询','Kafka','2018-04-20 18:12:35','2018-05-03 16:33:00'),(12,'load hbase.t_sdk_consume\nwhere `spark.table.schema`=\"userid:String,time:Long\"\n	   and `hbase.table.schema`=\":rowkey,info:time\" \nas tb;\n\nselect * from tb limit 100;\n\n--  读Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181 \n--  spark.table.schema：Spark临时表对应的schema eg: \"ID:String,appname:String,count:Int\" \n--  hbase.table.schema：Hbase表对应schema eg: \":rowkey,0:APPNAME,0:COUNT\" \n--  hbase.table.name：Hbase表名 \n--  spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据） \n \n--  写Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181 \n--  hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段 \n--  bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动\n--  hbase.table.name：Hbase表名 \n--  hbase.table.family：列族名，默认info \n--  hbase.table.startKey：预分区开始key，当hbase表不存在时并开启了检查，会自动创建Hbase表，不带以下三个参数则只有一个分区\n--  hbase.table.endKey：预分区开始key\n--  hbase.table.numReg：分区个数\n--  hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00 \n--  hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false','iql','Hbase表查询','Hbase','2018-04-21 21:01:16','2018-05-04 17:08:36'),(13,'load json.`/middata/sdk/member.log` as tb;\nselect reverse(CONCAT(\'0\',cast(userid as string))) as userid,time from tb as tb2;\n\nsave tb2 as hbase.`t_sdk_register_user`\n\n\n--  读Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181 \n--  spark.table.schema：Spark临时表对应的schema eg: \"ID:String,appname:String,count:Int\" \n--  hbase.table.schema：Hbase表对应schema eg: \":rowkey,0:APPNAME,0:COUNT\" \n--  hbase.table.name：Hbase表名 \n--  spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据） \n \n--  写Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 dsj01:2181 \n--  hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段 \n--  bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动\n--  hbase.table.name：Hbase表名 \n--  hbase.table.family：列族名，默认info \n--  hbase.table.startKey：预分区开始key，当hbase表不存在时并开启了检查，会自动创建Hbase表，不带以下三个参数则只有一个分区\n--  hbase.table.endKey：预分区开始key\n--  hbase.table.numReg：分区个数\n--  hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00 \n--  hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false','iql','Hbase表保存','Hbase','2018-04-28 17:30:44','2018-05-03 16:33:21'),(14,'load csv.`/home/jihuo.csv` as tb1;\n\nselect * from tb1;','iql','加载csv文件','CSV','2018-05-02 15:30:43','2018-05-03 16:33:30'),(15,'load json.`/data/sdkscore/score_20180514.log` as tb;\n\nselect userid,number,score,time,from_unixtime(substr(time,0,10),\'yyyyMMdd\') date,source from tb as tb1;\n\nINSERT OVERWRITE TABLE i4.sdk_score PARTITION (date,source)\nSELECT userid,number,score,time,date,source FROM tb1\nDISTRIBUTE BY date,source;','iql','JSON->Hive','JSON->Hive','2018-05-15 14:48:43','2018-05-15 14:48:43'),(16,'select 11 as id, \"test\" as name,22 as age from i4.app limit 1 as tb;\n\nsave update tb as jdbc.user','iql','mysql','update','2018-05-15 18:26:05','2018-05-15 18:26:05'),(17,'load es.`i4-monitor/metrics/` \nwhere `es.nodes`=\"192.168.1.232\"\n	and `es.port`=\"9200\" as tb;\n\nselect * from tb limit 10;','iql','ES','ES','2018-05-15 22:44:21','2018-05-15 22:44:21'),(19,'load kafka.`i4-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" as tb1;\n	\nselect * from tb1 as table21;	\n	\nsave append table21 as json.`/tmp/abc2` \nwhere mode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";','iql','Structured Streaming','动态添加流','2018-07-10 16:33:30','2018-07-17 00:42:58');

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pid` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `icon` varchar(255) DEFAULT '',
  `reorder` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`name`,`pid`,`url`,`icon`,`reorder`) values (8,'系统管理',-1,'#','fa fa-gears',99),(35,'修改密码',8,'system/changepw','',1009),(36,'用户管理',8,'system/user','',1),(37,'角色管理',8,'system/role','',10),(38,'功能管理',8,'system/menu','',20),(114,'Swagger',8,'http://192.168.1.40:8888/swagger-ui.html#/','',600),(147,'开发平台',-1,'#','fa fa-desktop',1),(148,'IQL查询',147,'iql/iql','',1),(149,'实时日志',147,'iql/joblog','',2),(150,'监控',-1,'#','fa fa-video-camera',199),(151,'druid监控',150,'http://192.168.1.40:8888/druid/index.html','',1),(152,'Profile',8,'system/profile','',601);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  `descs` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`rolename`,`descs`) values (2,'普通用户','可查看基本数据'),(1,'开发者账号','开发使用账号');

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `menuid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=22808 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

/*Data for the table `t_role_menu` */

insert  into `t_role_menu`(`id`,`roleid`,`menuid`) values (2031,9,20),(43,4,9),(42,4,2),(44,4,10),(45,4,12),(46,4,13),(47,4,14),(48,4,15),(49,4,16),(50,4,17),(51,4,18),(52,4,19),(2037,9,8),(2036,9,35),(2035,9,1),(2034,9,24),(2033,9,21),(2032,9,3),(2012,8,1),(2011,8,5),(2010,8,30),(22807,2,151),(22806,2,150),(22805,2,35),(22804,2,152),(22803,2,8),(22802,2,114),(22801,2,149),(22786,1,151),(22785,1,150),(22800,2,148),(22784,1,35),(22799,2,147),(22783,1,152),(22782,1,114),(22781,1,38),(22780,1,37),(22779,1,36),(22778,1,8),(22777,1,149),(22776,1,148),(22775,1,147);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `real_name` varchar(50) NOT NULL,
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0,非商务人员1,商务人员',
  `isfirstlogin` int(4) unsigned zerofill DEFAULT '0000',
  `token` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) ,
  UNIQUE KEY `INDEX_username` (`username`) 
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`real_name`,`phone`,`create_time`,`status`,`type`,`isfirstlogin`,`token`) values (1,'admin','e10adc3949ba59abbe56e057f20f883e','开发者账号','111111111111','2016-10-18 11:44:32',0,0,0001,'fa39e32c09332d47f6f38d9c946cfa25'),(33,'ufo','e10adc3949ba59abbe56e057f20f883e','teeyog','0','2018-09-21 08:40:15',0,0,0001,NULL);

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=MyISAM AUTO_INCREMENT=224 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

/*Data for the table `t_user_role` */

insert  into `t_user_role`(`id`,`userid`,`roleid`) values (1,1,1),(21,2,2),(29,6,7),(28,9,9),(26,8,8),(25,5,2),(71,10,10),(31,11,10),(108,12,2),(197,14,15),(64,15,12),(208,16,17),(109,19,15),(39,20,2),(78,21,2),(169,22,7),(106,23,10),(203,60,10),(44,25,2),(220,63,10),(50,27,2),(51,27,1),(52,27,7),(53,27,10),(54,27,11),(174,28,10),(66,29,12),(207,16,12),(69,30,2),(74,31,10),(212,32,2),(223,33,2),(80,34,2),(84,35,10),(117,36,10),(93,37,11),(99,38,14),(96,39,2),(116,40,15),(115,44,15),(120,45,2),(121,46,2),(122,47,10),(123,48,15),(125,49,16),(128,50,15),(187,54,2),(136,55,10),(196,14,14),(195,14,13),(194,14,7),(193,14,2),(184,57,15),(183,57,10),(177,56,10),(201,58,10),(188,54,17),(198,14,17),(204,59,7),(215,61,10),(214,62,10);

/*Table structure for table `t_datasource` */

DROP TABLE IF EXISTS `t_datasource`;

CREATE TABLE `t_datasource` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pid` int(11) NOT NULL,
  `isparent` tinyint(4) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `t_datasource` */

insert  into `t_datasource`(`id`,`name`,`pid`,`isparent`,`sort`,`path`) values (1,'datasource',0,1,1,'datasource'),(2,'HBase',1,1,1,'datasource.HBase'),(3,'Hive',1,1,2,'datasource.Hive'),(4,'t_user_info',2,0,1,'datasource.HBase.t_user_info'),(5,'MySQL',1,1,3,'datasource.MySQL'),(6,'mc',3,1,1,'datasource.Hive.mc'),(7,'default',3,1,2,'datasource.Hive.default'),(8,'mbl',6,0,1,'datasource.Hive.mc.mbl'),(9,'user',6,0,3,'datasource.Hive.mc.user'),(11,'app',5,0,1,'datasource.MySQL.app'),(12,'test',7,0,1,'datasource.Hive.default.test');

/*Table structure for table `t_role_datasource` */

DROP TABLE IF EXISTS `t_role_datasource`;

CREATE TABLE `t_role_datasource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `datasourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Data for the table `t_role_datasource` */

insert  into `t_role_datasource`(`id`,`roleid`,`datasourceid`) values (1,1,9),(2,1,11),(28,24,8),(31,24,11),(30,24,5),(29,24,6),(27,24,4),(26,24,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
