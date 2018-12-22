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
  `data` longtext COLLATE utf8mb4_unicode_ci,
  `data_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `table_schema` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `variables` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3838 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `iql_excution` */

insert  into `iql_excution`(`id`,`iql`,`mode`,`is_success`,`result_path`,`start_time`,`take_time`,`user`,`data`,`data_type`,`table_schema`,`variables`) values (3836,'register udf.`myupper`\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` \nas tb1;\n\nselect myupper(name) as newName from tb1;','iql','','/tmp/iql/result/iql_query_result_1544065445914','2018-12-06 03:04:03',2471,'superadmin','[{\"newName\":\"UFO\"},{\"newName\":\"UU\"},{\"newName\":\"HIN\"}]','structuredData','newName','[]'),(3837,'spark.udf.register(\"getModel\",(a:String) => {\n      if(a.toLowerCase.startsWith(\"ipho\")) 1\n      else if(a.toLowerCase.startsWith(\"ipad\")) 2\n      else 3\n    });\n\nspark.sql(\"\"\"select idfa,getModel(model) as modfdel from mc.mbl where date=\'20180908\' limit 10\"\"\").show(false)','code','','','2018-12-06 03:04:42',2,'superadmin','+----+-------+\n|idfa|modfdel|\n+----+-------+\n+----+-------+\n\r\n','preData','','[]');

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=790 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `job_script` */

insert  into `job_script`(`id`,`name`,`pid`,`script`,`isparent`,`sort`,`path`) values (1,'mc',0,'',1,1,'mc'),(4,'Update MySQL',724,'select 11 as id, \"test\" as name,22 as age \nfrom i4.app \nlimit 1 \nas tb;\n\nsave update tb as jdbc.user ',0,5,'mc.demo.Update MySQL'),(777,'Code',724,'spark.udf.register(\"getModel\",(a:String) => {\n      if(a.toLowerCase.startsWith(\"ipho\")) 1\n      else if(a.toLowerCase.startsWith(\"ipad\")) 2\n      else 3\n    });\n\nspark.sql(\"\"\"select idfa,getModel(model) as modfdel from mc.mbl where date=\'20180908\' limit 10\"\"\").show(false)',0,12,'mc.demo.Code'),(714,'spark',1,NULL,1,1,'mc.spark'),(715,'udf',714,NULL,1,1,'mc.spark.udf'),(724,'demo',1,NULL,1,6,'mc.demo'),(726,'Read Kafka',724,'load kafka.`i4-sdk` \nwhere maxRatePerPartition=\"200\"\n   and `group.id`=\"consumer_sdk_offline\" \nas tb;\n   \nselect * from tb;   \n',0,2,'mc.demo.Read Kafka'),(727,'Write HBase',724,'load json.`/middata/sdk/member.log` \nas tb;\n\nselect reverse(CONCAT(\'0\',cast(userid as string))) as userid,time \nfrom tb \nas tb2;\n\nsave tb2 as hbase.`t_sdk_register_user`\n \n--  写Hbase \n-- hbase.zookeeper.quorum：zookeeper地址\n-- hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段\n-- bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动。     \n-- hbase.table.name：Hbase表名  \n-- hbase.table.family：列族名，默认info\n-- hbase.table.region.splits：预分区分区段，以数组字符串方式指定，如 [\'1\',\'2\',\'3\'] (预分区指定分区段优先级最高)\n-- hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00，在startKey和endKey都未设置的情况下会生成00-99等100个分区\n-- hbase.table.startKey：预分区开始key，当hbase表不存在时，会自动创建Hbase表，不带一下三个参数则只有一个分区\n-- hbase.table.endKey：预分区开始key\n-- hbase.table.numReg：分区个数\n-- hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false',0,6,'mc.demo.Write HBase'),(729,'Read HBase',724,'load hbase.t_sdk_consume\nwhere `spark.table.schema`=\"userid:String,time:Long\"\n	   and `hbase.table.schema`=\":rowkey,info:time\" \nas tb;\n\nselect * from tb limit 100;\n\n--  读Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 localhost:2181 \n--  spark.table.schema：Spark临时表对应的schema eg: \"ID:String,appname:String,count:Int\" \n--  hbase.table.schema：Hbase表对应schema eg: \":rowkey,0:APPNAME,0:COUNT\" \n--  hbase.table.name：Hbase表名 \n--  spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据） \n  ',0,7,'mc.demo.Read HBase'),(779,'t4',733,'import mc.test.t3;\n\nselect * from tb1;',0,8,'mc.test.t4'),(778,'t3',733,'load hbase.t_mbl_user_info\nwhere `spark.table.schema`=\"idfa:String,version:String\"\n     and `hbase.table.schema`=\":rowkey,info:version\"\nas tb;\n\nselect * from tb limit 100 as tb1; \n ',0,7,'mc.test.t3'),(730,'JSON->hive',724,'load json.`/data/sdkscore/score_20180514.log` \nas tb;\n\nselect userid,number,score,time,from_unixtime(substr(time,0,10),\'yyyyMMdd\') date,source \nfrom tb \nas tb1;\n\nINSERT OVERWRITE TABLE i4.sdk_score PARTITION (date,source)\nSELECT userid,number,score,time,date,source FROM tb1\nDISTRIBUTE BY date,source;',0,8,'mc.demo.JSON->hive'),(731,'Read ES',724,'load es.`i4-monitor/metrics/` \nwhere `es.nodes`=\"192.168.1.232\"\n	and `es.port`=\"9200\" \nas tb;\n\nselect * from tb limit 10;',0,9,'mc.demo.Read ES'),(732,'Structured Streaming',724,'load kafka.`mc-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" \nas tb1;\n	\nselect * from tb1 \nas table21;	\n	\nsave table21 as json.`/tmp/abc2` \nwhere outputMode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";',0,10,'mc.demo.Structured Streaming'),(733,'test',1,'load kafka.`i4-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" \nas tb1;\n	\nselect * from tb1 \nas table21;	\n	\nsave table21 as json.`/tmp/abc2` \nwhere mode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";',1,7,'mc.test'),(735,'myupper',715,'register udf.myupper\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nregister udf.mylower\nwhere func=\n```\n	def apply(name:String)={\n		name.toLowerCase\n	}\n```;',0,1,'mc.spark.udf.myupper'),(736,'Use UDF',724,'register udf.`myupper`\nwhere func=\n```\n	def apply(name:String)={\n		name.toUpperCase\n	}\n```;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` \nas tb1;\n\nselect myupper(name) as newName from tb1;',0,11,'mc.demo.Use UDF'),(782,'bigscreen',781,NULL,1,1,'mc.job.stream.bigscreen'),(771,'job',1,NULL,1,2,'mc.job'),(762,'t2',733,'import mc.spark.udf.myupper;\n\nload jsonStr.\n```\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n``` as tb1;\n\nselect myupper(name) as upperName,mylower(name) as lowerName from tb1;',0,6,'mc.test.t2'),(781,'stream',771,NULL,1,3,'mc.job.stream'),(783,'dau',782,'load parquet.`/tmp/bigscreen/dau/` as tb;\n\nselect * from tb as tb1;\n \nselect * from tb1; \n \nsave overwrite tb1  as parquet.`/tmp/bigscreen/dau_bac/` \n	partitionBy date;',0,1,'mc.job.stream.bigscreen.dau'),(784,'new_user',782,'load parquet.`/tmp/bigscreen/newUser/` as tb;\n\nselect * from tb as tb1;\n \nselect * from tb1; \n \nsave overwrite tb1 as parquet.`/tmp/bigscreen/newUser_bac` \n	partitionBy date;',0,2,'mc.job.stream.bigscreen.new_user'),(786,'common',771,NULL,1,2,'mc.job.common'),(787,'查询mbl表',786,'select * from mc.mbl where date=\'${date}\' and key=\'${key}\' limit ${limit}',0,1,'mc.job.common.查询mbl表'),(789,'event',782,'load parquet.`/tmp/bigscreen/event/` as tb;\n\nselect * from tb as tb1;\n \nselect * from tb1; \n \nsave overwrite tb1  as parquet.`/tmp/bigscreen/event_bac` \n	partitionBy date;',0,3,'mc.job.stream.bigscreen.event');

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

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pid` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `icon` varchar(255) DEFAULT '',
  `reorder` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`name`,`pid`,`url`,`icon`,`reorder`) values (8,'系统管理',-1,'#','fa fa-gears',99),(35,'修改密码',8,'system/changepw','',1009),(36,'用户管理',8,'system/user','',1),(37,'角色管理',8,'system/role','',10),(38,'功能管理',8,'system/menu','',20),(114,'Swagger',8,'http://192.168.1.40:8888/swagger-ui.html#/','',600),(147,'开发平台',-1,'#','fa fa-desktop',1),(148,'IQL查询',147,'iql/iql','',1),(149,'实时日志',147,'iql/joblog','',2),(150,'监控',-1,'#','fa fa-video-camera',199),(151,'druid监控',150,'http://192.168.1.40:8888/druid/index.html','',1),(152,'Profile',8,'system/profile','',601),(153,'数据源管理',147,'iql/datasource','',21);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  `descs` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`rolename`,`descs`) values (2,'普通用户','可查看基本数据'),(1,'开发者账号','开发使用账号'),(12,'管理员','数据查询、用户管理、权限管理'),(13,'开发测试角色','用于开发时测试权限和数据');

/*Table structure for table `t_role_datasource` */

DROP TABLE IF EXISTS `t_role_datasource`;

CREATE TABLE `t_role_datasource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `datasourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

/*Data for the table `t_role_datasource` */

insert  into `t_role_datasource`(`id`,`roleid`,`datasourceid`) values (1,1,9),(2,1,11),(38,2,7),(37,2,9),(36,2,8),(35,2,6),(34,2,3),(33,2,4),(28,24,8),(32,2,2),(31,24,11),(30,24,5),(29,24,6),(27,24,4),(26,24,2),(39,2,12),(40,2,5),(41,2,11);

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `menuid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=22933 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

/*Data for the table `t_role_menu` */

insert  into `t_role_menu`(`id`,`roleid`,`menuid`) values (2031,9,20),(43,4,9),(42,4,2),(44,4,10),(45,4,12),(46,4,13),(47,4,14),(48,4,15),(49,4,16),(50,4,17),(51,4,18),(52,4,19),(2037,9,8),(2036,9,35),(2035,9,1),(2034,9,24),(2033,9,21),(2032,9,3),(2012,8,1),(2011,8,5),(2010,8,30),(22844,1,155),(22633,12,115),(22632,12,119),(22631,12,35),(22630,12,114),(22629,12,66),(22628,12,109),(22627,12,113),(22626,12,103),(22625,12,100),(22624,12,61),(22623,12,38),(22622,12,37),(22621,12,36),(22620,12,8),(22619,12,97),(22618,12,80),(22617,12,110),(22616,12,102),(22615,12,79),(22614,12,76),(22613,12,84),(22612,12,78),(22611,12,98),(22610,12,83),(22609,12,77),(22608,12,82),(22607,12,74),(22606,12,86),(22605,12,75),(22604,12,51),(22603,12,122),(22602,12,127),(22601,12,129),(22600,12,92),(22599,12,96),(22598,12,57),(22597,12,99),(22596,12,95),(22595,12,56),(22594,12,101),(22593,12,33),(22592,12,73),(22591,12,94),(22590,12,55),(22589,12,53),(22588,12,52),(22587,12,48),(22586,12,47),(22585,12,85),(22584,12,46),(22583,12,6),(22582,12,72),(22581,12,31),(22580,12,30),(22579,12,5),(22578,12,118),(22577,12,117),(22576,12,116),(22575,12,71),(22574,12,65),(22573,12,64),(22572,12,63),(22571,12,62),(22570,12,58),(22569,12,29),(22568,12,28),(22567,12,27),(22566,12,26),(22565,12,25),(22564,12,4),(22932,2,151),(22563,12,132),(22562,12,111),(22561,12,120),(22560,12,23),(22559,12,21),(22558,12,20),(22931,2,150),(22557,12,3),(22556,12,145),(22555,12,144),(22930,2,35),(22554,12,143),(22553,12,142),(22814,13,35),(22552,12,139),(22813,13,147),(22812,13,148),(22551,12,90),(22929,2,152),(22928,2,114),(22927,2,38),(22926,2,37),(22843,1,154),(22925,2,36),(22550,12,91),(22549,12,125),(22548,12,112),(22547,12,59),(22546,12,107),(22545,12,19),(22544,12,18),(22543,12,131),(22842,1,151),(22924,2,8),(22542,12,105),(22923,2,149),(22541,12,104),(22540,12,17),(22539,12,128),(22538,12,16),(22537,12,15),(22841,1,150),(22840,1,35),(22922,2,147),(22839,1,152),(22536,12,14),(22535,12,13),(22921,2,148),(22838,1,114),(22534,12,12),(22837,1,38),(22836,1,37),(22815,13,8),(22835,1,36),(22533,12,10),(22532,12,108),(22834,1,8),(22531,12,124),(22833,1,153),(22530,12,123),(22529,12,2),(22832,1,149),(22528,12,9),(22831,1,148),(22527,12,1),(22830,1,147),(22526,12,130);

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `INDEX_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`real_name`,`phone`,`create_time`,`status`,`type`,`isfirstlogin`,`token`) values (1,'admin','e10adc3949ba59abbe56e057f20f883e','开发者账号','111111111111','2016-10-18 11:44:32',0,0,0001,'fa39e32c09332d47f6f38d9c946cfa25'),(33,'ty','83f588446c477faffce1b28a1a3fd1f4','UFO','0','2018-12-06 03:06:28',0,0,0001,NULL);

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=224 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

/*Data for the table `t_user_role` */

insert  into `t_user_role`(`id`,`userid`,`roleid`) values (1,1,1),(21,2,2),(29,6,7),(28,9,9),(26,8,8),(25,5,2),(71,10,10),(31,11,10),(108,12,2),(197,14,15),(64,15,12),(208,16,17),(109,19,15),(39,20,2),(78,21,2),(169,22,7),(106,23,10),(203,60,10),(44,25,2),(220,63,10),(50,27,2),(51,27,1),(52,27,7),(53,27,10),(54,27,11),(174,28,10),(66,29,12),(207,16,12),(69,30,2),(74,31,10),(212,32,2),(223,33,2),(80,34,2),(84,35,10),(117,36,10),(93,37,11),(99,38,14),(96,39,2),(116,40,15),(115,44,15),(120,45,2),(121,46,2),(122,47,10),(123,48,15),(125,49,16),(128,50,15),(187,54,2),(136,55,10),(196,14,14),(195,14,13),(194,14,7),(193,14,2),(184,57,15),(183,57,10),(177,56,10),(201,58,10),(188,54,17),(198,14,17),(204,59,7),(215,61,10),(214,62,10);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
