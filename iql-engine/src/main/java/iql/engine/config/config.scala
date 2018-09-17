package iql.engine

package object config {

	val INIT_HIVE_CATALOG = ConfigBuilder("iql.hiveCatalog.init.enable")
		.doc("是否获取hive元数据显示在web端")
		.booleanConf
		.createWithDefault(true)

	val HIVE_CATALOG_AUTO_COMPLETE = ConfigBuilder("iql.hiveCatalog.autoComplete")
		.doc("SQL边界框是否获取hive元数据以自动补全")
		.booleanConf
		.createWithDefault(true)


}
