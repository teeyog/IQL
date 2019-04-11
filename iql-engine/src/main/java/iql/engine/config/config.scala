package iql.engine

package object config {

	val INIT_HIVE_CATALOG = ConfigBuilder("spark.iql.hiveCatalog.init.enable")
		.doc("是否获取hive元数据显示在web端")
		.booleanConf
		.createWithDefault(true)

	val HIVE_CATALOG_AUTO_COMPLETE = ConfigBuilder("spark.iql.hiveCatalog.autoComplete")
		.doc("SQL编辑框是否获取hive元数据以自动补全")
		.booleanConf
		.createWithDefault(true)

	val IQL_AUTH_ENABLE = ConfigBuilder("spark.iql.auth.enable")
		.doc("是否开启权限验证")
		.booleanConf
		.createWithDefault(false)

	val IQL_PARALLELISM = ConfigBuilder("spark.iql.parallelism")
		.doc("iql并行度")
		.intConf
		.createWithDefault(3)

	val MAIL_ENABLE = ConfigBuilder("spark.mail.enable")
		.doc("是否启用邮件通知")
		.booleanConf
		.createWithDefault(true)

	val STREAMJOB_MAXATTRPTS = ConfigBuilder("spark.streamJobMaxAttempts")
		.doc("实时任务失败重启次数")
		.intConf
		.createWithDefault(3)

}
