package iql.engine.adaptor

import iql.engine.antlr.IQLParser.SqlContext
import iql.common.Logging
import iql.engine.auth.TableAuthResult

trait DslAdaptor extends Logging {
    def parse(ctx: SqlContext): Unit
}


trait IQLAuth {
    def auth(_ctx: SqlContext): Unit
}

trait DslTool {
    def cleanStr(str: String) = {
        if (str.startsWith("```") && str.endsWith("```"))
            str.substring(3, str.length - 3)
        else if (str.startsWith("'") || str.startsWith("\"") || str.startsWith("`"))
            str.substring(1, str.length - 1)
        else str
    }
}

