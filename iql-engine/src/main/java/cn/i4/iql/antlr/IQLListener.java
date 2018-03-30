// Generated from D:/DevInstall/git/Git/cloud/iql/iql-engine/src/main/resources\IQL.g4 by ANTLR 4.5.3

package cn.i4.iql.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IQLParser}.
 */
public interface IQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(IQLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(IQLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#sql}.
	 * @param ctx the parse tree
	 */
	void enterSql(IQLParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#sql}.
	 * @param ctx the parse tree
	 */
	void exitSql(IQLParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void enterOverwrite(IQLParser.OverwriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void exitOverwrite(IQLParser.OverwriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#append}.
	 * @param ctx the parse tree
	 */
	void enterAppend(IQLParser.AppendContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#append}.
	 * @param ctx the parse tree
	 */
	void exitAppend(IQLParser.AppendContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void enterErrorIfExists(IQLParser.ErrorIfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void exitErrorIfExists(IQLParser.ErrorIfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#ignore}.
	 * @param ctx the parse tree
	 */
	void enterIgnore(IQLParser.IgnoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#ignore}.
	 * @param ctx the parse tree
	 */
	void exitIgnore(IQLParser.IgnoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(IQLParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(IQLParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(IQLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(IQLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#ender}.
	 * @param ctx the parse tree
	 */
	void enterEnder(IQLParser.EnderContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#ender}.
	 * @param ctx the parse tree
	 */
	void exitEnder(IQLParser.EnderContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#format}.
	 * @param ctx the parse tree
	 */
	void enterFormat(IQLParser.FormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#format}.
	 * @param ctx the parse tree
	 */
	void exitFormat(IQLParser.FormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(IQLParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(IQLParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#db}.
	 * @param ctx the parse tree
	 */
	void enterDb(IQLParser.DbContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#db}.
	 * @param ctx the parse tree
	 */
	void exitDb(IQLParser.DbContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(IQLParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(IQLParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(IQLParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(IQLParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#col}.
	 * @param ctx the parse tree
	 */
	void enterCol(IQLParser.ColContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#col}.
	 * @param ctx the parse tree
	 */
	void exitCol(IQLParser.ColContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(IQLParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(IQLParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(IQLParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(IQLParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterStrictIdentifier(IQLParser.StrictIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitStrictIdentifier(IQLParser.StrictIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(IQLParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(IQLParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link IQLParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void enterNumPartition(IQLParser.NumPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IQLParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void exitNumPartition(IQLParser.NumPartitionContext ctx);
}