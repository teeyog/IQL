// Generated from D:/DevInstall/git/Git/cloud/github/IQL/iql-engine/src/main/resources\IQL.g4 by ANTLR 4.7

package cn.i4.iql.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IQLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, STRING=36, IDENTIFIER=37, BACKQUOTED_IDENTIFIER=38, 
		SIMPLE_COMMENT=39, BRACKETED_EMPTY_COMMENT=40, BRACKETED_COMMENT=41, WS=42, 
		UNRECOGNIZED=43;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
		"T__33", "T__34", "STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", "DIGIT", 
		"LETTER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", 
		"WS", "UNRECOGNIZED"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'load'", "'LOAD'", "'.'", "'where'", "'as'", "'save'", "'SAVE'", 
		"'partitionBy'", "'coalesce'", "'select'", "'SELECT'", "';'", "'insert'", 
		"'INSERT'", "'create'", "'CREATE'", "'set'", "'SET'", "'connect'", "'CONNECT'", 
		"'train'", "'TRAIN'", "'register'", "'REGISTER'", "'show'", "'SHOW'", 
		"'describe'", "'DESCRIBE'", "'overwrite'", "'append'", "'errorIfExists'", 
		"'ignore'", "'update'", "'and'", "'='", null, null, null, null, "'/**/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
		"BRACKETED_COMMENT", "WS", "UNRECOGNIZED"
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


	public IQLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "IQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2-\u019e\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3"+
		"!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3%\3%\3%\3"+
		"%\7%\u0147\n%\f%\16%\u014a\13%\3%\3%\3%\3%\3%\7%\u0151\n%\f%\16%\u0154"+
		"\13%\3%\5%\u0157\n%\3&\3&\3&\6&\u015c\n&\r&\16&\u015d\3\'\3\'\3\'\3\'"+
		"\7\'\u0164\n\'\f\'\16\'\u0167\13\'\3\'\3\'\3(\3(\3)\3)\3*\3*\3*\3*\7*"+
		"\u0173\n*\f*\16*\u0176\13*\3*\5*\u0179\n*\3*\5*\u017c\n*\3*\3*\3+\3+\3"+
		"+\3+\3+\3+\3+\3,\3,\3,\3,\3,\7,\u018c\n,\f,\16,\u018f\13,\3,\3,\3,\3,"+
		"\3,\3-\6-\u0197\n-\r-\16-\u0198\3-\3-\3.\3.\3\u018d\2/\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
		"\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K"+
		"\'M(O\2Q\2S)U*W+Y,[-\3\2\n\4\2))^^\4\2$$^^\3\2bb\3\2\62;\4\2C\\c|\4\2"+
		"\f\f\17\17\3\2--\5\2\13\f\17\17\"\"\2\u01aa\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2"+
		"\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2"+
		"\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2"+
		"\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2"+
		"M\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\3]\3"+
		"\2\2\2\5b\3\2\2\2\7g\3\2\2\2\ti\3\2\2\2\13o\3\2\2\2\rr\3\2\2\2\17w\3\2"+
		"\2\2\21|\3\2\2\2\23\u0088\3\2\2\2\25\u0091\3\2\2\2\27\u0098\3\2\2\2\31"+
		"\u009f\3\2\2\2\33\u00a1\3\2\2\2\35\u00a8\3\2\2\2\37\u00af\3\2\2\2!\u00b6"+
		"\3\2\2\2#\u00bd\3\2\2\2%\u00c1\3\2\2\2\'\u00c5\3\2\2\2)\u00cd\3\2\2\2"+
		"+\u00d5\3\2\2\2-\u00db\3\2\2\2/\u00e1\3\2\2\2\61\u00ea\3\2\2\2\63\u00f3"+
		"\3\2\2\2\65\u00f8\3\2\2\2\67\u00fd\3\2\2\29\u0106\3\2\2\2;\u010f\3\2\2"+
		"\2=\u0119\3\2\2\2?\u0120\3\2\2\2A\u012e\3\2\2\2C\u0135\3\2\2\2E\u013c"+
		"\3\2\2\2G\u0140\3\2\2\2I\u0156\3\2\2\2K\u015b\3\2\2\2M\u015f\3\2\2\2O"+
		"\u016a\3\2\2\2Q\u016c\3\2\2\2S\u016e\3\2\2\2U\u017f\3\2\2\2W\u0186\3\2"+
		"\2\2Y\u0196\3\2\2\2[\u019c\3\2\2\2]^\7n\2\2^_\7q\2\2_`\7c\2\2`a\7f\2\2"+
		"a\4\3\2\2\2bc\7N\2\2cd\7Q\2\2de\7C\2\2ef\7F\2\2f\6\3\2\2\2gh\7\60\2\2"+
		"h\b\3\2\2\2ij\7y\2\2jk\7j\2\2kl\7g\2\2lm\7t\2\2mn\7g\2\2n\n\3\2\2\2op"+
		"\7c\2\2pq\7u\2\2q\f\3\2\2\2rs\7u\2\2st\7c\2\2tu\7x\2\2uv\7g\2\2v\16\3"+
		"\2\2\2wx\7U\2\2xy\7C\2\2yz\7X\2\2z{\7G\2\2{\20\3\2\2\2|}\7r\2\2}~\7c\2"+
		"\2~\177\7t\2\2\177\u0080\7v\2\2\u0080\u0081\7k\2\2\u0081\u0082\7v\2\2"+
		"\u0082\u0083\7k\2\2\u0083\u0084\7q\2\2\u0084\u0085\7p\2\2\u0085\u0086"+
		"\7D\2\2\u0086\u0087\7{\2\2\u0087\22\3\2\2\2\u0088\u0089\7e\2\2\u0089\u008a"+
		"\7q\2\2\u008a\u008b\7c\2\2\u008b\u008c\7n\2\2\u008c\u008d\7g\2\2\u008d"+
		"\u008e\7u\2\2\u008e\u008f\7e\2\2\u008f\u0090\7g\2\2\u0090\24\3\2\2\2\u0091"+
		"\u0092\7u\2\2\u0092\u0093\7g\2\2\u0093\u0094\7n\2\2\u0094\u0095\7g\2\2"+
		"\u0095\u0096\7e\2\2\u0096\u0097\7v\2\2\u0097\26\3\2\2\2\u0098\u0099\7"+
		"U\2\2\u0099\u009a\7G\2\2\u009a\u009b\7N\2\2\u009b\u009c\7G\2\2\u009c\u009d"+
		"\7E\2\2\u009d\u009e\7V\2\2\u009e\30\3\2\2\2\u009f\u00a0\7=\2\2\u00a0\32"+
		"\3\2\2\2\u00a1\u00a2\7k\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7u\2\2\u00a4"+
		"\u00a5\7g\2\2\u00a5\u00a6\7t\2\2\u00a6\u00a7\7v\2\2\u00a7\34\3\2\2\2\u00a8"+
		"\u00a9\7K\2\2\u00a9\u00aa\7P\2\2\u00aa\u00ab\7U\2\2\u00ab\u00ac\7G\2\2"+
		"\u00ac\u00ad\7T\2\2\u00ad\u00ae\7V\2\2\u00ae\36\3\2\2\2\u00af\u00b0\7"+
		"e\2\2\u00b0\u00b1\7t\2\2\u00b1\u00b2\7g\2\2\u00b2\u00b3\7c\2\2\u00b3\u00b4"+
		"\7v\2\2\u00b4\u00b5\7g\2\2\u00b5 \3\2\2\2\u00b6\u00b7\7E\2\2\u00b7\u00b8"+
		"\7T\2\2\u00b8\u00b9\7G\2\2\u00b9\u00ba\7C\2\2\u00ba\u00bb\7V\2\2\u00bb"+
		"\u00bc\7G\2\2\u00bc\"\3\2\2\2\u00bd\u00be\7u\2\2\u00be\u00bf\7g\2\2\u00bf"+
		"\u00c0\7v\2\2\u00c0$\3\2\2\2\u00c1\u00c2\7U\2\2\u00c2\u00c3\7G\2\2\u00c3"+
		"\u00c4\7V\2\2\u00c4&\3\2\2\2\u00c5\u00c6\7e\2\2\u00c6\u00c7\7q\2\2\u00c7"+
		"\u00c8\7p\2\2\u00c8\u00c9\7p\2\2\u00c9\u00ca\7g\2\2\u00ca\u00cb\7e\2\2"+
		"\u00cb\u00cc\7v\2\2\u00cc(\3\2\2\2\u00cd\u00ce\7E\2\2\u00ce\u00cf\7Q\2"+
		"\2\u00cf\u00d0\7P\2\2\u00d0\u00d1\7P\2\2\u00d1\u00d2\7G\2\2\u00d2\u00d3"+
		"\7E\2\2\u00d3\u00d4\7V\2\2\u00d4*\3\2\2\2\u00d5\u00d6\7v\2\2\u00d6\u00d7"+
		"\7t\2\2\u00d7\u00d8\7c\2\2\u00d8\u00d9\7k\2\2\u00d9\u00da\7p\2\2\u00da"+
		",\3\2\2\2\u00db\u00dc\7V\2\2\u00dc\u00dd\7T\2\2\u00dd\u00de\7C\2\2\u00de"+
		"\u00df\7K\2\2\u00df\u00e0\7P\2\2\u00e0.\3\2\2\2\u00e1\u00e2\7t\2\2\u00e2"+
		"\u00e3\7g\2\2\u00e3\u00e4\7i\2\2\u00e4\u00e5\7k\2\2\u00e5\u00e6\7u\2\2"+
		"\u00e6\u00e7\7v\2\2\u00e7\u00e8\7g\2\2\u00e8\u00e9\7t\2\2\u00e9\60\3\2"+
		"\2\2\u00ea\u00eb\7T\2\2\u00eb\u00ec\7G\2\2\u00ec\u00ed\7I\2\2\u00ed\u00ee"+
		"\7K\2\2\u00ee\u00ef\7U\2\2\u00ef\u00f0\7V\2\2\u00f0\u00f1\7G\2\2\u00f1"+
		"\u00f2\7T\2\2\u00f2\62\3\2\2\2\u00f3\u00f4\7u\2\2\u00f4\u00f5\7j\2\2\u00f5"+
		"\u00f6\7q\2\2\u00f6\u00f7\7y\2\2\u00f7\64\3\2\2\2\u00f8\u00f9\7U\2\2\u00f9"+
		"\u00fa\7J\2\2\u00fa\u00fb\7Q\2\2\u00fb\u00fc\7Y\2\2\u00fc\66\3\2\2\2\u00fd"+
		"\u00fe\7f\2\2\u00fe\u00ff\7g\2\2\u00ff\u0100\7u\2\2\u0100\u0101\7e\2\2"+
		"\u0101\u0102\7t\2\2\u0102\u0103\7k\2\2\u0103\u0104\7d\2\2\u0104\u0105"+
		"\7g\2\2\u01058\3\2\2\2\u0106\u0107\7F\2\2\u0107\u0108\7G\2\2\u0108\u0109"+
		"\7U\2\2\u0109\u010a\7E\2\2\u010a\u010b\7T\2\2\u010b\u010c\7K\2\2\u010c"+
		"\u010d\7D\2\2\u010d\u010e\7G\2\2\u010e:\3\2\2\2\u010f\u0110\7q\2\2\u0110"+
		"\u0111\7x\2\2\u0111\u0112\7g\2\2\u0112\u0113\7t\2\2\u0113\u0114\7y\2\2"+
		"\u0114\u0115\7t\2\2\u0115\u0116\7k\2\2\u0116\u0117\7v\2\2\u0117\u0118"+
		"\7g\2\2\u0118<\3\2\2\2\u0119\u011a\7c\2\2\u011a\u011b\7r\2\2\u011b\u011c"+
		"\7r\2\2\u011c\u011d\7g\2\2\u011d\u011e\7p\2\2\u011e\u011f\7f\2\2\u011f"+
		">\3\2\2\2\u0120\u0121\7g\2\2\u0121\u0122\7t\2\2\u0122\u0123\7t\2\2\u0123"+
		"\u0124\7q\2\2\u0124\u0125\7t\2\2\u0125\u0126\7K\2\2\u0126\u0127\7h\2\2"+
		"\u0127\u0128\7G\2\2\u0128\u0129\7z\2\2\u0129\u012a\7k\2\2\u012a\u012b"+
		"\7u\2\2\u012b\u012c\7v\2\2\u012c\u012d\7u\2\2\u012d@\3\2\2\2\u012e\u012f"+
		"\7k\2\2\u012f\u0130\7i\2\2\u0130\u0131\7p\2\2\u0131\u0132\7q\2\2\u0132"+
		"\u0133\7t\2\2\u0133\u0134\7g\2\2\u0134B\3\2\2\2\u0135\u0136\7w\2\2\u0136"+
		"\u0137\7r\2\2\u0137\u0138\7f\2\2\u0138\u0139\7c\2\2\u0139\u013a\7v\2\2"+
		"\u013a\u013b\7g\2\2\u013bD\3\2\2\2\u013c\u013d\7c\2\2\u013d\u013e\7p\2"+
		"\2\u013e\u013f\7f\2\2\u013fF\3\2\2\2\u0140\u0141\7?\2\2\u0141H\3\2\2\2"+
		"\u0142\u0148\7)\2\2\u0143\u0147\n\2\2\2\u0144\u0145\7^\2\2\u0145\u0147"+
		"\13\2\2\2\u0146\u0143\3\2\2\2\u0146\u0144\3\2\2\2\u0147\u014a\3\2\2\2"+
		"\u0148\u0146\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014b\3\2\2\2\u014a\u0148"+
		"\3\2\2\2\u014b\u0157\7)\2\2\u014c\u0152\7$\2\2\u014d\u0151\n\3\2\2\u014e"+
		"\u014f\7^\2\2\u014f\u0151\13\2\2\2\u0150\u014d\3\2\2\2\u0150\u014e\3\2"+
		"\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0157\7$\2\2\u0156\u0142\3\2"+
		"\2\2\u0156\u014c\3\2\2\2\u0157J\3\2\2\2\u0158\u015c\5Q)\2\u0159\u015c"+
		"\5O(\2\u015a\u015c\7a\2\2\u015b\u0158\3\2\2\2\u015b\u0159\3\2\2\2\u015b"+
		"\u015a\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2"+
		"\2\2\u015eL\3\2\2\2\u015f\u0165\7b\2\2\u0160\u0164\n\4\2\2\u0161\u0162"+
		"\7b\2\2\u0162\u0164\7b\2\2\u0163\u0160\3\2\2\2\u0163\u0161\3\2\2\2\u0164"+
		"\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168\3\2"+
		"\2\2\u0167\u0165\3\2\2\2\u0168\u0169\7b\2\2\u0169N\3\2\2\2\u016a\u016b"+
		"\t\5\2\2\u016bP\3\2\2\2\u016c\u016d\t\6\2\2\u016dR\3\2\2\2\u016e\u016f"+
		"\7/\2\2\u016f\u0170\7/\2\2\u0170\u0174\3\2\2\2\u0171\u0173\n\7\2\2\u0172"+
		"\u0171\3\2\2\2\u0173\u0176\3\2\2\2\u0174\u0172\3\2\2\2\u0174\u0175\3\2"+
		"\2\2\u0175\u0178\3\2\2\2\u0176\u0174\3\2\2\2\u0177\u0179\7\17\2\2\u0178"+
		"\u0177\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u017b\3\2\2\2\u017a\u017c\7\f"+
		"\2\2\u017b\u017a\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017d\3\2\2\2\u017d"+
		"\u017e\b*\2\2\u017eT\3\2\2\2\u017f\u0180\7\61\2\2\u0180\u0181\7,\2\2\u0181"+
		"\u0182\7,\2\2\u0182\u0183\7\61\2\2\u0183\u0184\3\2\2\2\u0184\u0185\b+"+
		"\2\2\u0185V\3\2\2\2\u0186\u0187\7\61\2\2\u0187\u0188\7,\2\2\u0188\u0189"+
		"\3\2\2\2\u0189\u018d\n\b\2\2\u018a\u018c\13\2\2\2\u018b\u018a\3\2\2\2"+
		"\u018c\u018f\3\2\2\2\u018d\u018e\3\2\2\2\u018d\u018b\3\2\2\2\u018e\u0190"+
		"\3\2\2\2\u018f\u018d\3\2\2\2\u0190\u0191\7,\2\2\u0191\u0192\7\61\2\2\u0192"+
		"\u0193\3\2\2\2\u0193\u0194\b,\2\2\u0194X\3\2\2\2\u0195\u0197\t\t\2\2\u0196"+
		"\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2"+
		"\2\2\u0199\u019a\3\2\2\2\u019a\u019b\b-\2\2\u019bZ\3\2\2\2\u019c\u019d"+
		"\13\2\2\2\u019d\\\3\2\2\2\21\2\u0146\u0148\u0150\u0152\u0156\u015b\u015d"+
		"\u0163\u0165\u0174\u0178\u017b\u018d\u0198\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}