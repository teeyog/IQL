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
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_message` longtext COLLATE utf8mb4_unicode_ci,
  `content` longtext COLLATE utf8mb4_unicode_ci,
  `table_schema` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `variables` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2480 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `iql_excution` */

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
) ENGINE=MyISAM AUTO_INCREMENT=770 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `job_script` */

insert  into `job_script`(`id`,`name`,`pid`,`script`,`isparent`,`sort`,`path`) values (1,'mc',0,'',1,1,'mc'),(4,'Update MySQL',724,'select 11 as id, \"test\" as name,22 as age from i4.app limit 1 as tb;\n\nsave update tb as jdbc.user ',0,5,'mc.demo.Update MySQL'),(714,'spark',1,NULL,1,1,'mc.spark'),(715,'udf',714,NULL,1,1,'mc.spark.udf'),(724,'demo',1,NULL,1,5,'mc.demo'),(726,'Read Kafka',724,'load kafka.`i4-sdk` \r\nwhere maxRatePerPartition=\"200\"\r\n   and `group.id`=\"consumer_sdk_offline\" as tb;\r\n   \r\nselect * from tb;   \r\n',0,2,'mc.demo.Read Kafka'),(727,'Write HBase',724,'load json.`/middata/sdk/member.log` as tb;\nselect reverse(CONCAT(\'0\',cast(userid as string))) as userid,time from tb as tb2;\nsave tb2 as hbase.`t_sdk_register_user`\n \n--  写Hbase \n-- hbase.zookeeper.quorum：zookeeper地址\n-- hbase.table.rowkey.field：spark临时表的哪个字段作为hbase的rowkey，默认第一个字段\n-- bulkload.enable：是否启动bulkload，默认不启动，暂时bulkload有bug（排序问题），当要插入的hbase表只有一列rowkey时，必需启动。     \n-- hbase.table.name：Hbase表名  \n-- hbase.table.family：列族名，默认info\n-- hbase.table.region.splits：预分区分区段，以数组字符串方式指定，如 [\'1\',\'2\',\'3\'] (预分区指定分区段优先级最高)\n-- hbase.table.rowkey.prefix: 当rowkey是数字，预分区需要指明前缀的formate形式，如 00，在startKey和endKey都未设置的情况下会生成00-99等100个分区\n-- hbase.table.startKey：预分区开始key，当hbase表不存在时，会自动创建Hbase表，不带一下三个参数则只有一个分区\n-- hbase.table.endKey：预分区开始key\n-- hbase.table.numReg：分区个数\n-- hbase.check_table: 写入hbase表时，是否需要检查表是否存在，默认 false',0,6,'mc.demo.Write HBase'),(729,'Read HBase',724,'load hbase.t_sdk_consume\nwhere `spark.table.schema`=\"userid:String,time:Long\"\n	   and `hbase.table.schema`=\":rowkey,info:time\" \nas tb;\n\nselect * from tb limit 100;\n\n--  读Hbase \n--  hbase.zookeeper.quorum：zookeeper地址,默认是 localhost:2181 \n--  spark.table.schema：Spark临时表对应的schema eg: \"ID:String,appname:String,count:Int\" \n--  hbase.table.schema：Hbase表对应schema eg: \":rowkey,0:APPNAME,0:COUNT\" \n--  hbase.table.name：Hbase表名 \n--  spark.rowkey.view.name：rowkey对应的dataframe创建的tempview名（设置了该值后，只获取rowkey对应的数据） \n  ',0,7,'mc.demo.Read HBase'),(730,'JSON->hive',724,'load json.`/data/sdkscore/score_20180514.log` as tb;\n\nselect userid,number,score,time,from_unixtime(substr(time,0,10),\'yyyyMMdd\') date,source from tb as tb1;\n\nINSERT OVERWRITE TABLE i4.sdk_score PARTITION (date,source)\nSELECT userid,number,score,time,date,source FROM tb1\nDISTRIBUTE BY date,source;',0,8,'mc.demo.JSON->hive'),(731,'Read ES',724,'load es.`i4-monitor/metrics/` \nwhere `es.nodes`=\"192.168.1.232\"\n	and `es.port`=\"9200\" as tb;\n\nselect * from tb limit 10;',0,9,'mc.demo.Read ES'),(732,'Structured Streaming',724,'load kafka.`i4-monitor` \nwhere startingoffsets=\"latest\"\n	and failOnDataLoss=\"false\"\n	and `spark.job.mode`=\"stream\" as tb1;\n	\nselect * from tb1 as table21;	\n	\nsave append table21 as json.`/tmp/abc2` \nwhere mode=\"Append\"\n	and streamName=\"Stream\"\n	and duration=\"10\"\n	and checkpointLocation=\"/tmp/cp/cp12\";',0,10,'mc.demo.Structured Streaming'),(733,'test',1,NULL,1,6,'mc.test'),(735,'myupper',715,'register udf.`myupper`\nwhere func=\n\"\n	def apply(name:String)={\n		name.toUpperCase\n	}\n\";\nregister udf.`mylower`\nwhere func=\n\"\n	def apply(name:String)={\n		name.toLowerCase\n	}\n\"',0,1,'mc.spark.udf.myupper'),(736,'Use UDF',724,'register udf.`myupper`\nwhere func=\n\"\n	def apply(name:String)={\n		name.toUpperCase\n	}\n\";\n\nload jsonStr.\'\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n\' as tb1;\n\nselect myupper(name) as newName from tb1;',0,11,'mc.demo.Use UDF'),(762,'t2',733,'import mc.spark.udf.myupper;\n\n\nload jsonStr.\'\n{\"name\":\"ufo\"}\n{\"name\":\"uu\"}\n{\"name\":\"HIN\"}\n\' as tb1;\n\nselect myupper(name) as newName from tb1;',0,6,'mc.test.t2');


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
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`name`,`pid`,`url`,`icon`,`reorder`) values (8,'系统管理',-1,'#','fa fa-gears',99),(35,'修改密码',8,'system/changepw','',1009),(36,'用户管理',8,'system/user','',1),(37,'角色管理',8,'system/role','',10),(38,'功能管理',8,'system/menu','',20),(114,'Swagger',8,'http://localhost:8888/swagger-ui.html#/','',600),(147,'开发平台',-1,'#','fa fa-desktop',1),(148,'IQL查询',147,'iql/iql','',1),(149,'实时日志',147,'iql/joblog','',2),(150,'监控',-1,'#','fa fa-video-camera',199),(151,'druid监控',150,'http://localhost:8888/druid/index.html','',1);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  `descs` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`rolename`,`descs`) values (1,'开发者账号','开发使用账号');

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `menuid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=22775 DEFAULT CHARSET=utf8;

/*Data for the table `t_role_menu` */

insert  into `t_role_menu`(`id`,`roleid`,`menuid`) values (22763,1,151),(22762,1,150),(22761,1,35),(22760,1,114),(22759,1,113),(22758,1,103),(22757,1,61),(22756,1,38),(22755,1,37),(22754,1,36),(22753,1,8),(22752,1,149),(22751,1,148),(22750,1,147);

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`real_name`,`phone`,`create_time`,`status`,`type`,`isfirstlogin`) values (1,'admin','e10adc3949ba59abbe56e057f20f883e','开发者账号','111111111111','2016-10-18 11:44:32',0,0,0001);

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=223 DEFAULT CHARSET=utf8;

/*Data for the table `t_user_role` */

insert  into `t_user_role`(`id`,`userid`,`roleid`) values (1,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
