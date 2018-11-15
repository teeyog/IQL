package iql.engine

package object config {

	val INIT_HIVE_CATALOG = ConfigBuilder("iql.hiveCatalog.init.enable")
		.doc("是否获取hive元数据显示在web端")
		.booleanConf
		.createWithDefault(true)

	val HIVE_CATALOG_AUTO_COMPLETE = ConfigBuilder("iql.hiveCatalog.autoComplete")
		.doc("SQL编辑框是否获取hive元数据以自动补全")
		.booleanConf
		.createWithDefault(true)

	val IQL_AUTH_ENABLE = ConfigBuilder("iql.auth.enable")
		.doc("是否开启权限验证")
		.booleanConf
		.createWithDefault(false)

	val IQL_PARALLELISM = ConfigBuilder("iql.parallelism ")
		.doc("iql并行度")
		.intConf
		.createWithDefault(3)


}
