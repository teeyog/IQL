// Generated from E:/BigData/spark/workspace/iql/iql-engine/src/main/resources\IQL.g4 by ANTLR 4.5.3

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
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, STRING=35, IDENTIFIER=36, BACKQUOTED_IDENTIFIER=37, 
		SIMPLE_COMMENT=38, BRACKETED_EMPTY_COMMENT=39, BRACKETED_COMMENT=40, WS=41, 
		UNRECOGNIZED=42;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
		"T__33", "STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", "DIGIT", "LETTER", 
		"SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", "WS", 
		"UNRECOGNIZED"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'load'", "'LOAD'", "'.'", "'where'", "'as'", "'save'", "'SAVE'", 
		"'partitionBy'", "'coalesce'", "'select'", "'SELECT'", "';'", "'insert'", 
		"'INSERT'", "'create'", "'CREATE'", "'set'", "'SET'", "'connect'", "'CONNECT'", 
		"'train'", "'TRAIN'", "'register'", "'REGISTER'", "'show'", "'SHOW'", 
		"'describe'", "'DESCRIBE'", "'overwrite'", "'append'", "'errorIfExists'", 
		"'ignore'", "'and'", "'='", null, null, null, null, "'/**/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, "STRING", 
		"IDENTIFIER", "BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2,\u0195\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!"+
		"\3!\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3$\3$\3$\3$\7$\u013e\n$\f$\16$\u0141\13"+
		"$\3$\3$\3$\3$\3$\7$\u0148\n$\f$\16$\u014b\13$\3$\5$\u014e\n$\3%\3%\3%"+
		"\6%\u0153\n%\r%\16%\u0154\3&\3&\3&\3&\7&\u015b\n&\f&\16&\u015e\13&\3&"+
		"\3&\3\'\3\'\3(\3(\3)\3)\3)\3)\7)\u016a\n)\f)\16)\u016d\13)\3)\5)\u0170"+
		"\n)\3)\5)\u0173\n)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\7+\u0183"+
		"\n+\f+\16+\u0186\13+\3+\3+\3+\3+\3+\3,\6,\u018e\n,\r,\16,\u018f\3,\3,"+
		"\3-\3-\3\u0184\2.\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M\2O\2Q(S)U*W+Y,\3\2\n\4\2))^^\4\2"+
		"$$^^\3\2bb\3\2\62;\4\2C\\c|\4\2\f\f\17\17\3\2--\5\2\13\f\17\17\"\"\u01a1"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\3[\3\2\2\2\5`\3\2\2\2\7e\3\2\2\2\tg\3\2\2\2\13m\3\2\2\2\rp\3"+
		"\2\2\2\17u\3\2\2\2\21z\3\2\2\2\23\u0086\3\2\2\2\25\u008f\3\2\2\2\27\u0096"+
		"\3\2\2\2\31\u009d\3\2\2\2\33\u009f\3\2\2\2\35\u00a6\3\2\2\2\37\u00ad\3"+
		"\2\2\2!\u00b4\3\2\2\2#\u00bb\3\2\2\2%\u00bf\3\2\2\2\'\u00c3\3\2\2\2)\u00cb"+
		"\3\2\2\2+\u00d3\3\2\2\2-\u00d9\3\2\2\2/\u00df\3\2\2\2\61\u00e8\3\2\2\2"+
		"\63\u00f1\3\2\2\2\65\u00f6\3\2\2\2\67\u00fb\3\2\2\29\u0104\3\2\2\2;\u010d"+
		"\3\2\2\2=\u0117\3\2\2\2?\u011e\3\2\2\2A\u012c\3\2\2\2C\u0133\3\2\2\2E"+
		"\u0137\3\2\2\2G\u014d\3\2\2\2I\u0152\3\2\2\2K\u0156\3\2\2\2M\u0161\3\2"+
		"\2\2O\u0163\3\2\2\2Q\u0165\3\2\2\2S\u0176\3\2\2\2U\u017d\3\2\2\2W\u018d"+
		"\3\2\2\2Y\u0193\3\2\2\2[\\\7n\2\2\\]\7q\2\2]^\7c\2\2^_\7f\2\2_\4\3\2\2"+
		"\2`a\7N\2\2ab\7Q\2\2bc\7C\2\2cd\7F\2\2d\6\3\2\2\2ef\7\60\2\2f\b\3\2\2"+
		"\2gh\7y\2\2hi\7j\2\2ij\7g\2\2jk\7t\2\2kl\7g\2\2l\n\3\2\2\2mn\7c\2\2no"+
		"\7u\2\2o\f\3\2\2\2pq\7u\2\2qr\7c\2\2rs\7x\2\2st\7g\2\2t\16\3\2\2\2uv\7"+
		"U\2\2vw\7C\2\2wx\7X\2\2xy\7G\2\2y\20\3\2\2\2z{\7r\2\2{|\7c\2\2|}\7t\2"+
		"\2}~\7v\2\2~\177\7k\2\2\177\u0080\7v\2\2\u0080\u0081\7k\2\2\u0081\u0082"+
		"\7q\2\2\u0082\u0083\7p\2\2\u0083\u0084\7D\2\2\u0084\u0085\7{\2\2\u0085"+
		"\22\3\2\2\2\u0086\u0087\7e\2\2\u0087\u0088\7q\2\2\u0088\u0089\7c\2\2\u0089"+
		"\u008a\7n\2\2\u008a\u008b\7g\2\2\u008b\u008c\7u\2\2\u008c\u008d\7e\2\2"+
		"\u008d\u008e\7g\2\2\u008e\24\3\2\2\2\u008f\u0090\7u\2\2\u0090\u0091\7"+
		"g\2\2\u0091\u0092\7n\2\2\u0092\u0093\7g\2\2\u0093\u0094\7e\2\2\u0094\u0095"+
		"\7v\2\2\u0095\26\3\2\2\2\u0096\u0097\7U\2\2\u0097\u0098\7G\2\2\u0098\u0099"+
		"\7N\2\2\u0099\u009a\7G\2\2\u009a\u009b\7E\2\2\u009b\u009c\7V\2\2\u009c"+
		"\30\3\2\2\2\u009d\u009e\7=\2\2\u009e\32\3\2\2\2\u009f\u00a0\7k\2\2\u00a0"+
		"\u00a1\7p\2\2\u00a1\u00a2\7u\2\2\u00a2\u00a3\7g\2\2\u00a3\u00a4\7t\2\2"+
		"\u00a4\u00a5\7v\2\2\u00a5\34\3\2\2\2\u00a6\u00a7\7K\2\2\u00a7\u00a8\7"+
		"P\2\2\u00a8\u00a9\7U\2\2\u00a9\u00aa\7G\2\2\u00aa\u00ab\7T\2\2\u00ab\u00ac"+
		"\7V\2\2\u00ac\36\3\2\2\2\u00ad\u00ae\7e\2\2\u00ae\u00af\7t\2\2\u00af\u00b0"+
		"\7g\2\2\u00b0\u00b1\7c\2\2\u00b1\u00b2\7v\2\2\u00b2\u00b3\7g\2\2\u00b3"+
		" \3\2\2\2\u00b4\u00b5\7E\2\2\u00b5\u00b6\7T\2\2\u00b6\u00b7\7G\2\2\u00b7"+
		"\u00b8\7C\2\2\u00b8\u00b9\7V\2\2\u00b9\u00ba\7G\2\2\u00ba\"\3\2\2\2\u00bb"+
		"\u00bc\7u\2\2\u00bc\u00bd\7g\2\2\u00bd\u00be\7v\2\2\u00be$\3\2\2\2\u00bf"+
		"\u00c0\7U\2\2\u00c0\u00c1\7G\2\2\u00c1\u00c2\7V\2\2\u00c2&\3\2\2\2\u00c3"+
		"\u00c4\7e\2\2\u00c4\u00c5\7q\2\2\u00c5\u00c6\7p\2\2\u00c6\u00c7\7p\2\2"+
		"\u00c7\u00c8\7g\2\2\u00c8\u00c9\7e\2\2\u00c9\u00ca\7v\2\2\u00ca(\3\2\2"+
		"\2\u00cb\u00cc\7E\2\2\u00cc\u00cd\7Q\2\2\u00cd\u00ce\7P\2\2\u00ce\u00cf"+
		"\7P\2\2\u00cf\u00d0\7G\2\2\u00d0\u00d1\7E\2\2\u00d1\u00d2\7V\2\2\u00d2"+
		"*\3\2\2\2\u00d3\u00d4\7v\2\2\u00d4\u00d5\7t\2\2\u00d5\u00d6\7c\2\2\u00d6"+
		"\u00d7\7k\2\2\u00d7\u00d8\7p\2\2\u00d8,\3\2\2\2\u00d9\u00da\7V\2\2\u00da"+
		"\u00db\7T\2\2\u00db\u00dc\7C\2\2\u00dc\u00dd\7K\2\2\u00dd\u00de\7P\2\2"+
		"\u00de.\3\2\2\2\u00df\u00e0\7t\2\2\u00e0\u00e1\7g\2\2\u00e1\u00e2\7i\2"+
		"\2\u00e2\u00e3\7k\2\2\u00e3\u00e4\7u\2\2\u00e4\u00e5\7v\2\2\u00e5\u00e6"+
		"\7g\2\2\u00e6\u00e7\7t\2\2\u00e7\60\3\2\2\2\u00e8\u00e9\7T\2\2\u00e9\u00ea"+
		"\7G\2\2\u00ea\u00eb\7I\2\2\u00eb\u00ec\7K\2\2\u00ec\u00ed\7U\2\2\u00ed"+
		"\u00ee\7V\2\2\u00ee\u00ef\7G\2\2\u00ef\u00f0\7T\2\2\u00f0\62\3\2\2\2\u00f1"+
		"\u00f2\7u\2\2\u00f2\u00f3\7j\2\2\u00f3\u00f4\7q\2\2\u00f4\u00f5\7y\2\2"+
		"\u00f5\64\3\2\2\2\u00f6\u00f7\7U\2\2\u00f7\u00f8\7J\2\2\u00f8\u00f9\7"+
		"Q\2\2\u00f9\u00fa\7Y\2\2\u00fa\66\3\2\2\2\u00fb\u00fc\7f\2\2\u00fc\u00fd"+
		"\7g\2\2\u00fd\u00fe\7u\2\2\u00fe\u00ff\7e\2\2\u00ff\u0100\7t\2\2\u0100"+
		"\u0101\7k\2\2\u0101\u0102\7d\2\2\u0102\u0103\7g\2\2\u01038\3\2\2\2\u0104"+
		"\u0105\7F\2\2\u0105\u0106\7G\2\2\u0106\u0107\7U\2\2\u0107\u0108\7E\2\2"+
		"\u0108\u0109\7T\2\2\u0109\u010a\7K\2\2\u010a\u010b\7D\2\2\u010b\u010c"+
		"\7G\2\2\u010c:\3\2\2\2\u010d\u010e\7q\2\2\u010e\u010f\7x\2\2\u010f\u0110"+
		"\7g\2\2\u0110\u0111\7t\2\2\u0111\u0112\7y\2\2\u0112\u0113\7t\2\2\u0113"+
		"\u0114\7k\2\2\u0114\u0115\7v\2\2\u0115\u0116\7g\2\2\u0116<\3\2\2\2\u0117"+
		"\u0118\7c\2\2\u0118\u0119\7r\2\2\u0119\u011a\7r\2\2\u011a\u011b\7g\2\2"+
		"\u011b\u011c\7p\2\2\u011c\u011d\7f\2\2\u011d>\3\2\2\2\u011e\u011f\7g\2"+
		"\2\u011f\u0120\7t\2\2\u0120\u0121\7t\2\2\u0121\u0122\7q\2\2\u0122\u0123"+
		"\7t\2\2\u0123\u0124\7K\2\2\u0124\u0125\7h\2\2\u0125\u0126\7G\2\2\u0126"+
		"\u0127\7z\2\2\u0127\u0128\7k\2\2\u0128\u0129\7u\2\2\u0129\u012a\7v\2\2"+
		"\u012a\u012b\7u\2\2\u012b@\3\2\2\2\u012c\u012d\7k\2\2\u012d\u012e\7i\2"+
		"\2\u012e\u012f\7p\2\2\u012f\u0130\7q\2\2\u0130\u0131\7t\2\2\u0131\u0132"+
		"\7g\2\2\u0132B\3\2\2\2\u0133\u0134\7c\2\2\u0134\u0135\7p\2\2\u0135\u0136"+
		"\7f\2\2\u0136D\3\2\2\2\u0137\u0138\7?\2\2\u0138F\3\2\2\2\u0139\u013f\7"+
		")\2\2\u013a\u013e\n\2\2\2\u013b\u013c\7^\2\2\u013c\u013e\13\2\2\2\u013d"+
		"\u013a\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3\2"+
		"\2\2\u013f\u0140\3\2\2\2\u0140\u0142\3\2\2\2\u0141\u013f\3\2\2\2\u0142"+
		"\u014e\7)\2\2\u0143\u0149\7$\2\2\u0144\u0148\n\3\2\2\u0145\u0146\7^\2"+
		"\2\u0146\u0148\13\2\2\2\u0147\u0144\3\2\2\2\u0147\u0145\3\2\2\2\u0148"+
		"\u014b\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014c\3\2"+
		"\2\2\u014b\u0149\3\2\2\2\u014c\u014e\7$\2\2\u014d\u0139\3\2\2\2\u014d"+
		"\u0143\3\2\2\2\u014eH\3\2\2\2\u014f\u0153\5O(\2\u0150\u0153\5M\'\2\u0151"+
		"\u0153\7a\2\2\u0152\u014f\3\2\2\2\u0152\u0150\3\2\2\2\u0152\u0151\3\2"+
		"\2\2\u0153\u0154\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155"+
		"J\3\2\2\2\u0156\u015c\7b\2\2\u0157\u015b\n\4\2\2\u0158\u0159\7b\2\2\u0159"+
		"\u015b\7b\2\2\u015a\u0157\3\2\2\2\u015a\u0158\3\2\2\2\u015b\u015e\3\2"+
		"\2\2\u015c\u015a\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015f\3\2\2\2\u015e"+
		"\u015c\3\2\2\2\u015f\u0160\7b\2\2\u0160L\3\2\2\2\u0161\u0162\t\5\2\2\u0162"+
		"N\3\2\2\2\u0163\u0164\t\6\2\2\u0164P\3\2\2\2\u0165\u0166\7/\2\2\u0166"+
		"\u0167\7/\2\2\u0167\u016b\3\2\2\2\u0168\u016a\n\7\2\2\u0169\u0168\3\2"+
		"\2\2\u016a\u016d\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c"+
		"\u016f\3\2\2\2\u016d\u016b\3\2\2\2\u016e\u0170\7\17\2\2\u016f\u016e\3"+
		"\2\2\2\u016f\u0170\3\2\2\2\u0170\u0172\3\2\2\2\u0171\u0173\7\f\2\2\u0172"+
		"\u0171\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0175\b)"+
		"\2\2\u0175R\3\2\2\2\u0176\u0177\7\61\2\2\u0177\u0178\7,\2\2\u0178\u0179"+
		"\7,\2\2\u0179\u017a\7\61\2\2\u017a\u017b\3\2\2\2\u017b\u017c\b*\2\2\u017c"+
		"T\3\2\2\2\u017d\u017e\7\61\2\2\u017e\u017f\7,\2\2\u017f\u0180\3\2\2\2"+
		"\u0180\u0184\n\b\2\2\u0181\u0183\13\2\2\2\u0182\u0181\3\2\2\2\u0183\u0186"+
		"\3\2\2\2\u0184\u0185\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0187\3\2\2\2\u0186"+
		"\u0184\3\2\2\2\u0187\u0188\7,\2\2\u0188\u0189\7\61\2\2\u0189\u018a\3\2"+
		"\2\2\u018a\u018b\b+\2\2\u018bV\3\2\2\2\u018c\u018e\t\t\2\2\u018d\u018c"+
		"\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\u0191\3\2\2\2\u0191\u0192\b,\2\2\u0192X\3\2\2\2\u0193\u0194\13\2\2\2"+
		"\u0194Z\3\2\2\2\21\2\u013d\u013f\u0147\u0149\u014d\u0152\u0154\u015a\u015c"+
		"\u016b\u016f\u0172\u0184\u018f\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}