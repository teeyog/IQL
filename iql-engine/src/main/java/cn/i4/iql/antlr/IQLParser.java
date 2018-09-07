// Generated from D:/DevInstall/git/Git/cloud/github/IQL/iql-engine/src/main/resources\IQL.g4 by ANTLR 4.7

package cn.i4.iql.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IQLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, STRING=38, 
		BLOCK_STRING=39, IDENTIFIER=40, BACKQUOTED_IDENTIFIER=41, SIMPLE_COMMENT=42, 
		BRACKETED_EMPTY_COMMENT=43, BRACKETED_COMMENT=44, WS=45, UNRECOGNIZED=46, 
		DIGIT=47;
	public static final int
		RULE_statement = 0, RULE_sql = 1, RULE_overwrite = 2, RULE_append = 3, 
		RULE_errorIfExists = 4, RULE_ignore = 5, RULE_update = 6, RULE_booleanExpression = 7, 
		RULE_expression = 8, RULE_ender = 9, RULE_format = 10, RULE_path = 11, 
		RULE_db = 12, RULE_tableName = 13, RULE_functionName = 14, RULE_col = 15, 
		RULE_qualifiedName = 16, RULE_identifier = 17, RULE_strictIdentifier = 18, 
		RULE_quotedIdentifier = 19, RULE_numPartition = 20;
	public static final String[] ruleNames = {
		"statement", "sql", "overwrite", "append", "errorIfExists", "ignore", 
		"update", "booleanExpression", "expression", "ender", "format", "path", 
		"db", "tableName", "functionName", "col", "qualifiedName", "identifier", 
		"strictIdentifier", "quotedIdentifier", "numPartition"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'load'", "'LOAD'", "'.'", "'where'", "'as'", "'save'", "'SAVE'", 
		"'partitionBy'", "'coalesce'", "'select'", "'SELECT'", "';'", "'insert'", 
		"'INSERT'", "'create'", "'CREATE'", "'set'", "'SET'", "'train'", "'TRAIN'", 
		"'register'", "'REGISTER'", "'show'", "'SHOW'", "'describe'", "'DESCRIBE'", 
		"'import'", "'IMPORT'", "'include'", "'INCLUDE'", "'overwrite'", "'append'", 
		"'errorIfExists'", "'ignore'", "'update'", "'and'", "'='", null, null, 
		null, null, null, "'/**/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "STRING", "BLOCK_STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", 
		"SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", "WS", 
		"UNRECOGNIZED", "DIGIT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "IQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public IQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StatementContext extends ParserRuleContext {
		public List<SqlContext> sql() {
			return getRuleContexts(SqlContext.class);
		}
		public SqlContext sql(int i) {
			return getRuleContext(SqlContext.class,i);
		}
		public List<EnderContext> ender() {
			return getRuleContexts(EnderContext.class);
		}
		public EnderContext ender(int i) {
			return getRuleContext(EnderContext.class,i);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__5) | (1L << T__6) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << SIMPLE_COMMENT))) != 0)) {
				{
				{
				setState(42);
				sql();
				setState(43);
				ender();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SqlContext extends ParserRuleContext {
		public FormatContext format() {
			return getRuleContext(FormatContext.class,0);
		}
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public List<OverwriteContext> overwrite() {
			return getRuleContexts(OverwriteContext.class);
		}
		public OverwriteContext overwrite(int i) {
			return getRuleContext(OverwriteContext.class,i);
		}
		public List<AppendContext> append() {
			return getRuleContexts(AppendContext.class);
		}
		public AppendContext append(int i) {
			return getRuleContext(AppendContext.class,i);
		}
		public List<ErrorIfExistsContext> errorIfExists() {
			return getRuleContexts(ErrorIfExistsContext.class);
		}
		public ErrorIfExistsContext errorIfExists(int i) {
			return getRuleContext(ErrorIfExistsContext.class,i);
		}
		public List<IgnoreContext> ignore() {
			return getRuleContexts(IgnoreContext.class);
		}
		public IgnoreContext ignore(int i) {
			return getRuleContext(IgnoreContext.class,i);
		}
		public List<UpdateContext> update() {
			return getRuleContexts(UpdateContext.class);
		}
		public UpdateContext update(int i) {
			return getRuleContext(UpdateContext.class,i);
		}
		public ColContext col() {
			return getRuleContext(ColContext.class,0);
		}
		public NumPartitionContext numPartition() {
			return getRuleContext(NumPartitionContext.class,0);
		}
		public TerminalNode SIMPLE_COMMENT() { return getToken(IQLParser.SIMPLE_COMMENT, 0); }
		public SqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterSql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitSql(this);
		}
	}

	public final SqlContext sql() throws RecognitionException {
		SqlContext _localctx = new SqlContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sql);
		int _la;
		try {
			int _alt;
			setState(195);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				_la = _input.LA(1);
				if ( !(_la==T__0 || _la==T__1) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(51);
				format();
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(52);
					match(T__2);
					}
				}

				setState(55);
				path();
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(56);
					match(T__3);
					}
				}

				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER || _la==BACKQUOTED_IDENTIFIER) {
					{
					setState(59);
					expression();
					}
				}

				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__35) {
					{
					{
					setState(62);
					booleanExpression();
					}
					}
					setState(67);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(68);
				match(T__4);
				setState(69);
				tableName();
				}
				break;
			case T__5:
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				_la = _input.LA(1);
				if ( !(_la==T__5 || _la==T__6) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) {
					{
					setState(77);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__30:
						{
						setState(72);
						overwrite();
						}
						break;
					case T__31:
						{
						setState(73);
						append();
						}
						break;
					case T__32:
						{
						setState(74);
						errorIfExists();
						}
						break;
					case T__33:
						{
						setState(75);
						ignore();
						}
						break;
					case T__34:
						{
						setState(76);
						update();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(81);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(82);
				tableName();
				setState(83);
				match(T__4);
				setState(84);
				format();
				setState(85);
				match(T__2);
				setState(86);
				path();
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(87);
					match(T__3);
					}
				}

				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER || _la==BACKQUOTED_IDENTIFIER) {
					{
					setState(90);
					expression();
					}
				}

				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__35) {
					{
					{
					setState(93);
					booleanExpression();
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(99);
					match(T__7);
					setState(100);
					col();
					}
				}

				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(103);
					match(T__8);
					setState(104);
					numPartition();
					}
				}

				}
				break;
			case T__9:
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(107);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(108);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__11) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						} 
					}
					setState(113);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(114);
					match(T__4);
					}
				}

				setState(117);
				tableName();
				}
				break;
			case T__12:
			case T__13:
				enterOuterAlt(_localctx, 4);
				{
				setState(118);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(119);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(124);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__14:
			case T__15:
				enterOuterAlt(_localctx, 5);
				{
				setState(125);
				_la = _input.LA(1);
				if ( !(_la==T__14 || _la==T__15) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(126);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__16:
			case T__17:
				enterOuterAlt(_localctx, 6);
				{
				setState(132);
				_la = _input.LA(1);
				if ( !(_la==T__16 || _la==T__17) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(133);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__18:
			case T__19:
				enterOuterAlt(_localctx, 7);
				{
				setState(139);
				_la = _input.LA(1);
				if ( !(_la==T__18 || _la==T__19) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(140);
				tableName();
				setState(141);
				match(T__4);
				setState(142);
				format();
				setState(143);
				match(T__2);
				setState(144);
				path();
				setState(146);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(145);
					match(T__3);
					}
				}

				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER || _la==BACKQUOTED_IDENTIFIER) {
					{
					setState(148);
					expression();
					}
				}

				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__35) {
					{
					{
					setState(151);
					booleanExpression();
					}
					}
					setState(156);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__20:
			case T__21:
				enterOuterAlt(_localctx, 8);
				{
				setState(157);
				_la = _input.LA(1);
				if ( !(_la==T__20 || _la==T__21) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(158);
				format();
				setState(159);
				match(T__2);
				setState(160);
				path();
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(161);
					match(T__3);
					}
				}

				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER || _la==BACKQUOTED_IDENTIFIER) {
					{
					setState(164);
					expression();
					}
				}

				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__35) {
					{
					{
					setState(167);
					booleanExpression();
					}
					}
					setState(172);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__22:
			case T__23:
				enterOuterAlt(_localctx, 9);
				{
				setState(173);
				_la = _input.LA(1);
				if ( !(_la==T__22 || _la==T__23) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(177);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(174);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(179);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__24:
			case T__25:
				enterOuterAlt(_localctx, 10);
				{
				setState(180);
				_la = _input.LA(1);
				if ( !(_la==T__24 || _la==T__25) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(181);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(186);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__26:
			case T__27:
			case T__28:
			case T__29:
				enterOuterAlt(_localctx, 11);
				{
				setState(187);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << STRING) | (1L << BLOCK_STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER) | (1L << SIMPLE_COMMENT) | (1L << BRACKETED_EMPTY_COMMENT) | (1L << BRACKETED_COMMENT) | (1L << WS) | (1L << UNRECOGNIZED) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(188);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__11) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(193);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case SIMPLE_COMMENT:
				enterOuterAlt(_localctx, 12);
				{
				setState(194);
				match(SIMPLE_COMMENT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OverwriteContext extends ParserRuleContext {
		public OverwriteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_overwrite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterOverwrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitOverwrite(this);
		}
	}

	public final OverwriteContext overwrite() throws RecognitionException {
		OverwriteContext _localctx = new OverwriteContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_overwrite);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(T__30);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AppendContext extends ParserRuleContext {
		public AppendContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_append; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterAppend(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitAppend(this);
		}
	}

	public final AppendContext append() throws RecognitionException {
		AppendContext _localctx = new AppendContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_append);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(T__31);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ErrorIfExistsContext extends ParserRuleContext {
		public ErrorIfExistsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_errorIfExists; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterErrorIfExists(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitErrorIfExists(this);
		}
	}

	public final ErrorIfExistsContext errorIfExists() throws RecognitionException {
		ErrorIfExistsContext _localctx = new ErrorIfExistsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_errorIfExists);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(T__32);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IgnoreContext extends ParserRuleContext {
		public IgnoreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ignore; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterIgnore(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitIgnore(this);
		}
	}

	public final IgnoreContext ignore() throws RecognitionException {
		IgnoreContext _localctx = new IgnoreContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ignore);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(T__33);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateContext extends ParserRuleContext {
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitUpdate(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_update);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(T__34);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitBooleanExpression(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_booleanExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(T__35);
			setState(208);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(IQLParser.STRING, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			identifier();
			setState(211);
			match(T__36);
			setState(212);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnderContext extends ParserRuleContext {
		public EnderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ender; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterEnder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitEnder(this);
		}
	}

	public final EnderContext ender() throws RecognitionException {
		EnderContext _localctx = new EnderContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_ender);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormatContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_format; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitFormat(this);
		}
	}

	public final FormatContext format() throws RecognitionException {
		FormatContext _localctx = new FormatContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_format);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathContext extends ParserRuleContext {
		public QuotedIdentifierContext quotedIdentifier() {
			return getRuleContext(QuotedIdentifierContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitPath(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_path);
		try {
			setState(220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(218);
				quotedIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(219);
				identifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DbContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_db; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterDb(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitDb(this);
		}
	}

	public final DbContext db() throws RecognitionException {
		DbContext _localctx = new DbContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_db);
		try {
			setState(224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(222);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(223);
				identifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableNameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitTableName(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionNameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitFunctionName(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_col; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterCol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitCol(this);
		}
	}

	public final ColContext col() throws RecognitionException {
		ColContext _localctx = new ColContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_col);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			identifier();
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(233);
				match(T__2);
				setState(234);
				identifier();
				}
				}
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public StrictIdentifierContext strictIdentifier() {
			return getRuleContext(StrictIdentifierContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitIdentifier(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			strictIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StrictIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IQLParser.IDENTIFIER, 0); }
		public QuotedIdentifierContext quotedIdentifier() {
			return getRuleContext(QuotedIdentifierContext.class,0);
		}
		public StrictIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strictIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterStrictIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitStrictIdentifier(this);
		}
	}

	public final StrictIdentifierContext strictIdentifier() throws RecognitionException {
		StrictIdentifierContext _localctx = new StrictIdentifierContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_strictIdentifier);
		try {
			setState(244);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(242);
				match(IDENTIFIER);
				}
				break;
			case BACKQUOTED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(243);
				quotedIdentifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuotedIdentifierContext extends ParserRuleContext {
		public TerminalNode BACKQUOTED_IDENTIFIER() { return getToken(IQLParser.BACKQUOTED_IDENTIFIER, 0); }
		public QuotedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quotedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterQuotedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitQuotedIdentifier(this);
		}
	}

	public final QuotedIdentifierContext quotedIdentifier() throws RecognitionException {
		QuotedIdentifierContext _localctx = new QuotedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_quotedIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(BACKQUOTED_IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumPartitionContext extends ParserRuleContext {
		public List<TerminalNode> DIGIT() { return getTokens(IQLParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(IQLParser.DIGIT, i);
		}
		public NumPartitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numPartition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).enterNumPartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IQLListener ) ((IQLListener)listener).exitNumPartition(this);
		}
	}

	public final NumPartitionContext numPartition() throws RecognitionException {
		NumPartitionContext _localctx = new NumPartitionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_numPartition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(248);
				match(DIGIT);
				}
				}
				setState(251); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DIGIT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\61\u0100\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\7\2\60\n\2\f\2\16"+
		"\2\63\13\2\3\3\3\3\3\3\5\38\n\3\3\3\3\3\5\3<\n\3\3\3\5\3?\n\3\3\3\7\3"+
		"B\n\3\f\3\16\3E\13\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3P\n\3\f\3"+
		"\16\3S\13\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3[\n\3\3\3\5\3^\n\3\3\3\7\3a\n\3"+
		"\f\3\16\3d\13\3\3\3\3\3\5\3h\n\3\3\3\3\3\5\3l\n\3\3\3\3\3\7\3p\n\3\f\3"+
		"\16\3s\13\3\3\3\5\3v\n\3\3\3\3\3\3\3\7\3{\n\3\f\3\16\3~\13\3\3\3\3\3\7"+
		"\3\u0082\n\3\f\3\16\3\u0085\13\3\3\3\3\3\7\3\u0089\n\3\f\3\16\3\u008c"+
		"\13\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0095\n\3\3\3\5\3\u0098\n\3\3\3"+
		"\7\3\u009b\n\3\f\3\16\3\u009e\13\3\3\3\3\3\3\3\3\3\3\3\5\3\u00a5\n\3\3"+
		"\3\5\3\u00a8\n\3\3\3\7\3\u00ab\n\3\f\3\16\3\u00ae\13\3\3\3\3\3\7\3\u00b2"+
		"\n\3\f\3\16\3\u00b5\13\3\3\3\3\3\7\3\u00b9\n\3\f\3\16\3\u00bc\13\3\3\3"+
		"\3\3\7\3\u00c0\n\3\f\3\16\3\u00c3\13\3\3\3\5\3\u00c6\n\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\5\r\u00df\n\r\3\16\3\16\5\16\u00e3\n\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\22\7\22\u00ee\n\22\f\22\16\22\u00f1\13\22\3\23"+
		"\3\23\3\24\3\24\5\24\u00f7\n\24\3\25\3\25\3\26\6\26\u00fc\n\26\r\26\16"+
		"\26\u00fd\3\26\2\2\27\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*\2"+
		"\16\3\2\3\4\3\2\b\t\3\2\f\r\3\2\16\16\3\2\17\20\3\2\21\22\3\2\23\24\3"+
		"\2\25\26\3\2\27\30\3\2\31\32\3\2\33\34\3\2\35 \2\u0117\2\61\3\2\2\2\4"+
		"\u00c5\3\2\2\2\6\u00c7\3\2\2\2\b\u00c9\3\2\2\2\n\u00cb\3\2\2\2\f\u00cd"+
		"\3\2\2\2\16\u00cf\3\2\2\2\20\u00d1\3\2\2\2\22\u00d4\3\2\2\2\24\u00d8\3"+
		"\2\2\2\26\u00da\3\2\2\2\30\u00de\3\2\2\2\32\u00e2\3\2\2\2\34\u00e4\3\2"+
		"\2\2\36\u00e6\3\2\2\2 \u00e8\3\2\2\2\"\u00ea\3\2\2\2$\u00f2\3\2\2\2&\u00f6"+
		"\3\2\2\2(\u00f8\3\2\2\2*\u00fb\3\2\2\2,-\5\4\3\2-.\5\24\13\2.\60\3\2\2"+
		"\2/,\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\3\3\2\2\2\63\61"+
		"\3\2\2\2\64\65\t\2\2\2\65\67\5\26\f\2\668\7\5\2\2\67\66\3\2\2\2\678\3"+
		"\2\2\289\3\2\2\29;\5\30\r\2:<\7\6\2\2;:\3\2\2\2;<\3\2\2\2<>\3\2\2\2=?"+
		"\5\22\n\2>=\3\2\2\2>?\3\2\2\2?C\3\2\2\2@B\5\20\t\2A@\3\2\2\2BE\3\2\2\2"+
		"CA\3\2\2\2CD\3\2\2\2DF\3\2\2\2EC\3\2\2\2FG\7\7\2\2GH\5\34\17\2H\u00c6"+
		"\3\2\2\2IQ\t\3\2\2JP\5\6\4\2KP\5\b\5\2LP\5\n\6\2MP\5\f\7\2NP\5\16\b\2"+
		"OJ\3\2\2\2OK\3\2\2\2OL\3\2\2\2OM\3\2\2\2ON\3\2\2\2PS\3\2\2\2QO\3\2\2\2"+
		"QR\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TU\5\34\17\2UV\7\7\2\2VW\5\26\f\2WX\7\5"+
		"\2\2XZ\5\30\r\2Y[\7\6\2\2ZY\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\^\5\22\n\2]\\"+
		"\3\2\2\2]^\3\2\2\2^b\3\2\2\2_a\5\20\t\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2"+
		"bc\3\2\2\2cg\3\2\2\2db\3\2\2\2ef\7\n\2\2fh\5 \21\2ge\3\2\2\2gh\3\2\2\2"+
		"hk\3\2\2\2ij\7\13\2\2jl\5*\26\2ki\3\2\2\2kl\3\2\2\2l\u00c6\3\2\2\2mq\t"+
		"\4\2\2np\n\5\2\2on\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3\2\2\2ru\3\2\2\2sq\3"+
		"\2\2\2tv\7\7\2\2ut\3\2\2\2uv\3\2\2\2vw\3\2\2\2w\u00c6\5\34\17\2x|\t\6"+
		"\2\2y{\n\5\2\2zy\3\2\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\u00c6\3\2\2\2"+
		"~|\3\2\2\2\177\u0083\t\7\2\2\u0080\u0082\n\5\2\2\u0081\u0080\3\2\2\2\u0082"+
		"\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u00c6\3\2"+
		"\2\2\u0085\u0083\3\2\2\2\u0086\u008a\t\b\2\2\u0087\u0089\n\5\2\2\u0088"+
		"\u0087\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2"+
		"\2\2\u008b\u00c6\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u008e\t\t\2\2\u008e"+
		"\u008f\5\34\17\2\u008f\u0090\7\7\2\2\u0090\u0091\5\26\f\2\u0091\u0092"+
		"\7\5\2\2\u0092\u0094\5\30\r\2\u0093\u0095\7\6\2\2\u0094\u0093\3\2\2\2"+
		"\u0094\u0095\3\2\2\2\u0095\u0097\3\2\2\2\u0096\u0098\5\22\n\2\u0097\u0096"+
		"\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009c\3\2\2\2\u0099\u009b\5\20\t\2"+
		"\u009a\u0099\3\2\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d"+
		"\3\2\2\2\u009d\u00c6\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\t\n\2\2\u00a0"+
		"\u00a1\5\26\f\2\u00a1\u00a2\7\5\2\2\u00a2\u00a4\5\30\r\2\u00a3\u00a5\7"+
		"\6\2\2\u00a4\u00a3\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a7\3\2\2\2\u00a6"+
		"\u00a8\5\22\n\2\u00a7\u00a6\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00ac\3"+
		"\2\2\2\u00a9\u00ab\5\20\t\2\u00aa\u00a9\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac"+
		"\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00c6\3\2\2\2\u00ae\u00ac\3\2"+
		"\2\2\u00af\u00b3\t\13\2\2\u00b0\u00b2\n\5\2\2\u00b1\u00b0\3\2\2\2\u00b2"+
		"\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00c6\3\2"+
		"\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00ba\t\f\2\2\u00b7\u00b9\n\5\2\2\u00b8"+
		"\u00b7\3\2\2\2\u00b9\u00bc\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2"+
		"\2\2\u00bb\u00c6\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bd\u00c1\t\r\2\2\u00be"+
		"\u00c0\n\5\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2"+
		"\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c6\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c4"+
		"\u00c6\7,\2\2\u00c5\64\3\2\2\2\u00c5I\3\2\2\2\u00c5m\3\2\2\2\u00c5x\3"+
		"\2\2\2\u00c5\177\3\2\2\2\u00c5\u0086\3\2\2\2\u00c5\u008d\3\2\2\2\u00c5"+
		"\u009f\3\2\2\2\u00c5\u00af\3\2\2\2\u00c5\u00b6\3\2\2\2\u00c5\u00bd\3\2"+
		"\2\2\u00c5\u00c4\3\2\2\2\u00c6\5\3\2\2\2\u00c7\u00c8\7!\2\2\u00c8\7\3"+
		"\2\2\2\u00c9\u00ca\7\"\2\2\u00ca\t\3\2\2\2\u00cb\u00cc\7#\2\2\u00cc\13"+
		"\3\2\2\2\u00cd\u00ce\7$\2\2\u00ce\r\3\2\2\2\u00cf\u00d0\7%\2\2\u00d0\17"+
		"\3\2\2\2\u00d1\u00d2\7&\2\2\u00d2\u00d3\5\22\n\2\u00d3\21\3\2\2\2\u00d4"+
		"\u00d5\5$\23\2\u00d5\u00d6\7\'\2\2\u00d6\u00d7\7(\2\2\u00d7\23\3\2\2\2"+
		"\u00d8\u00d9\7\16\2\2\u00d9\25\3\2\2\2\u00da\u00db\5$\23\2\u00db\27\3"+
		"\2\2\2\u00dc\u00df\5(\25\2\u00dd\u00df\5$\23\2\u00de\u00dc\3\2\2\2\u00de"+
		"\u00dd\3\2\2\2\u00df\31\3\2\2\2\u00e0\u00e3\5\"\22\2\u00e1\u00e3\5$\23"+
		"\2\u00e2\u00e0\3\2\2\2\u00e2\u00e1\3\2\2\2\u00e3\33\3\2\2\2\u00e4\u00e5"+
		"\5$\23\2\u00e5\35\3\2\2\2\u00e6\u00e7\5$\23\2\u00e7\37\3\2\2\2\u00e8\u00e9"+
		"\5$\23\2\u00e9!\3\2\2\2\u00ea\u00ef\5$\23\2\u00eb\u00ec\7\5\2\2\u00ec"+
		"\u00ee\5$\23\2\u00ed\u00eb\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2"+
		"\2\2\u00ef\u00f0\3\2\2\2\u00f0#\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00f3"+
		"\5&\24\2\u00f3%\3\2\2\2\u00f4\u00f7\7*\2\2\u00f5\u00f7\5(\25\2\u00f6\u00f4"+
		"\3\2\2\2\u00f6\u00f5\3\2\2\2\u00f7\'\3\2\2\2\u00f8\u00f9\7+\2\2\u00f9"+
		")\3\2\2\2\u00fa\u00fc\7\61\2\2\u00fb\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2"+
		"\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe+\3\2\2\2\"\61\67;>COQZ"+
		"]bgkqu|\u0083\u008a\u0094\u0097\u009c\u00a4\u00a7\u00ac\u00b3\u00ba\u00c1"+
		"\u00c5\u00de\u00e2\u00ef\u00f6\u00fd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}