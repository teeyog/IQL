package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.adaptor._
import cn.i4.iql.antlr.IQLListener
import cn.i4.iql.antlr.IQLParser._
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.{ErrorNode, TerminalNode}
import org.apache.spark.sql.SparkSession

class IQLSQLExecListener(var _sparkSession: SparkSession, _pathPrefix: String, resultMap:ConcurrentHashMap[String, String]) extends IQLListener{
  def sparkSession = _sparkSession
  def pathPrefix: String = {
    if (_pathPrefix == null || _pathPrefix.isEmpty) return ""
    if (!_pathPrefix.endsWith("/")) {
      return _pathPrefix + "/"
    }
    return _pathPrefix
  }

  override def exitSql(ctx: SqlContext): Unit = {
    ctx.getChild(0).getText.toLowerCase() match {
      case "load" =>
        new LoadAdaptor(this).parse(ctx)

      case "select" =>
        new SelectAdaptor(this,resultMap).parse(ctx)

      case "save" =>
        new SaveAdaptor(this).parse(ctx)

//      case "connect" =>
//        new ConnectAdaptor(this,dbMap).parse(ctx)

      case "create" =>
        new CreateAdaptor(this).parse(ctx)

      case "insert" =>
        new InsertAdaptor(this).parse(ctx)

      case "set" =>
        new SetAdaptor(this).parse(ctx)

      case "show" =>
        new ShowAdaptor(this,resultMap).parse(ctx)
//      case "train" =>
//        new TrainAdaptor(this).parse(ctx)
//      case "register" =>
//        new RegisterAdaptor(this).parse(ctx)
    }

  }

  override def enterStatement(ctx: StatementContext): Unit = {}

  override def exitStatement(ctx: StatementContext): Unit = {
    sparkSession.catalog.listTables().collect().foreach(r => sparkSession.catalog.dropTempView(r.name))
    //刷新元数据
    sparkSession.catalog.listDatabases().rdd.map(_.name).collect().foreach(d =>
      sparkSession.catalog.listTables(d).rdd.map(_.name).collect().foreach(t =>
        sparkSession.catalog.refreshTable(d + "." + t))
    )
  }

  override def enterSql(ctx: SqlContext): Unit = {}

  override def enterFormat(ctx: FormatContext): Unit = {}

  override def exitFormat(ctx: FormatContext): Unit = {}

  override def enterPath(ctx: PathContext): Unit = {}

  override def exitPath(ctx: PathContext): Unit = {}

  override def enterTableName(ctx: TableNameContext): Unit = {}

  override def exitTableName(ctx: TableNameContext): Unit = {}

  override def enterCol(ctx: ColContext): Unit = {}

  override def exitCol(ctx: ColContext): Unit = {}

  override def enterQualifiedName(ctx: QualifiedNameContext): Unit = {}

  override def exitQualifiedName(ctx: QualifiedNameContext): Unit = {}

  override def enterIdentifier(ctx: IdentifierContext): Unit = {}

  override def exitIdentifier(ctx: IdentifierContext): Unit = {}

  override def enterStrictIdentifier(ctx: StrictIdentifierContext): Unit = {}

  override def exitStrictIdentifier(ctx: StrictIdentifierContext): Unit = {}

  override def enterQuotedIdentifier(ctx: QuotedIdentifierContext): Unit = {}

  override def exitQuotedIdentifier(ctx: QuotedIdentifierContext): Unit = {}

  override def visitTerminal(node: TerminalNode): Unit = {}

  override def visitErrorNode(node: ErrorNode): Unit = {}

  override def exitEveryRule(ctx: ParserRuleContext): Unit = {}

  override def enterEveryRule(ctx: ParserRuleContext): Unit = {}

  override def enterEnder(ctx: EnderContext): Unit = {}

  override def exitEnder(ctx: EnderContext): Unit = {}

  override def enterExpression(ctx: ExpressionContext): Unit = {}

  override def exitExpression(ctx: ExpressionContext): Unit = {}

  override def enterBooleanExpression(ctx: BooleanExpressionContext): Unit = {}

  override def exitBooleanExpression(ctx: BooleanExpressionContext): Unit = {}

  override def enterDb(ctx: DbContext): Unit = {}

  override def exitDb(ctx: DbContext): Unit = {}

  override def enterOverwrite(ctx: OverwriteContext): Unit = {}

  override def exitOverwrite(ctx: OverwriteContext): Unit = {}

  override def enterAppend(ctx: AppendContext): Unit = {}

  override def exitAppend(ctx: AppendContext): Unit = {}

  override def enterErrorIfExists(ctx: ErrorIfExistsContext): Unit = {}

  override def exitErrorIfExists(ctx: ErrorIfExistsContext): Unit = {}

  override def enterIgnore(ctx: IgnoreContext): Unit = {}

  override def exitIgnore(ctx: IgnoreContext): Unit = {}

  override def enterFunctionName(ctx: FunctionNameContext): Unit = {}

  override def exitFunctionName(ctx: FunctionNameContext): Unit = {}

  /**
    * Enter a parse tree produced by {@link IQLParser#numPartition}.
    *
    * @param ctx the parse tree
    */
  override def enterNumPartition(ctx: NumPartitionContext): Unit = {}

  /**
    * Exit a parse tree produced by {@link IQLParser#numPartition}.
    *
    * @param ctx the parse tree
    */
  override def exitNumPartition(ctx: NumPartitionContext): Unit = {}

  /**
    * Enter a parse tree produced by {@link IQLParser#update}.
    *
    * @param ctx the parse tree
    */
  override def enterUpdate(ctx: UpdateContext): Unit = {}

  /**
    * Exit a parse tree produced by {@link IQLParser#update}.
    *
    * @param ctx the parse tree
    */
  override def exitUpdate(ctx: UpdateContext): Unit = {}
}
