package cn.i4.iql.adaptor

import cn.i4.iql.Logging
import cn.i4.iql.antlr.IQLParser.SqlContext

trait DslAdaptor extends Logging {
    def parse(ctx: SqlContext): Unit
}

trait DslTool {
    def cleanStr(str: String) = {
        if (str.startsWith("'") || str.startsWith("\""))
            str.substring(1, str.length - 1)
        else if (str.startsWith("'''") && str.endsWith("'''"))
          str.substring(3, str.length - 3)
        else str
    }

    def cleanBlockStr(str: String) = {
        if (str.startsWith("'''") && str.endsWith("'''"))
            str.substring(3, str.length - 3)
        else str
    }
}

