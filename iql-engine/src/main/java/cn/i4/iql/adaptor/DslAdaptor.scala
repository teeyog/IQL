package cn.i4.iql.adaptor

import cn.i4.iql.antlr.IQLParser.SqlContext

trait DslAdaptor {
  def parse(ctx: SqlContext): Unit

  def cleanStr(str: String) = {
    if (str.startsWith("`") || str.startsWith("\""))
      str.substring(1, str.length - 1)
    else str
  }

  def withPathPrefix(prefix: String, path: String): String = {

    val newPath = cleanStr(path)
    if (prefix.isEmpty) return newPath

    if (path.contains("..")) {
      throw new RuntimeException("path should not contains ..")
    }
    if (path.startsWith("/")) {
      return prefix + path.substring(1, path.length)
    }
    return prefix + newPath

  }
}
