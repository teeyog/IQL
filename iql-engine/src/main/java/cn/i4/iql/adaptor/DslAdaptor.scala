package cn.i4.iql.adaptor

import cn.i4.iql.Logging
import cn.i4.iql.antlr.IQLParser.SqlContext

trait DslAdaptor extends Logging{
  def parse(ctx: SqlContext): Unit

  def cleanStr(str: String) = {
    if (str.startsWith("`") || str.startsWith("\"") || str.startsWith("'"))
      str.substring(1, str.length - 1)
    else str
  }

}
