// Generated from /home/daniel/WACC_22/antlr_config/BasicLexer.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BasicLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COMMENT=1, IS=2, S_SKIP=3, READ=4, FREE=5, RETURN=6, EXIT=7, PRINT=8, 
		PRINTLN=9, IF=10, THEN=11, ELSE=12, FI=13, WHILE=14, DO=15, DONE=16, BEGIN=17, 
		END=18, CALL=19, INTEGER=20, PAIR=21, NEW_PAIR=22, FST=23, SND=24, INT=25, 
		BOOL=26, CHAR=27, STRING=28, P_OPEN=29, P_CLOSE=30, SB_OPEN=31, SB_CLOSE=32, 
		NOT=33, LEN=34, ORD=35, CHR=36, ASSIGN=37, PLUS=38, MINUS=39, MULTIPLY=40, 
		DIVIDE=41, MODULO=42, GREATER_THAN=43, GREATER_THAN_OR_EQUAL=44, LESS_THAN=45, 
		LESS_THAN_OR_EQUAL=46, EQUAL=47, NOT_EQUAL=48, AND=49, OR=50, SEMI_COLON=51, 
		COMMA=52, EOL=53, CHAR_LITER=54, STR_LITER=55, BOOL_LITER=56, NULL=57, 
		IDENT=58;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"COMMENT", "IS", "S_SKIP", "READ", "FREE", "RETURN", "EXIT", "PRINT", 
			"PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", "DO", "DONE", "BEGIN", 
			"END", "CALL", "DIGIT", "INTEGER", "PAIR", "NEW_PAIR", "FST", "SND", 
			"INT", "BOOL", "CHAR", "STRING", "P_OPEN", "P_CLOSE", "SB_OPEN", "SB_CLOSE", 
			"NOT", "LEN", "ORD", "CHR", "ASSIGN", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", 
			"MODULO", "GREATER_THAN", "GREATER_THAN_OR_EQUAL", "LESS_THAN", "LESS_THAN_OR_EQUAL", 
			"EQUAL", "NOT_EQUAL", "AND", "OR", "SEMI_COLON", "COMMA", "EOL", "ESC_CHAR", 
			"CHARACTER", "CHAR_LITER", "STR_LITER", "BOOL_LITER", "NULL", "IDENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'is'", "'skip'", "'read'", "'free'", "'return'", "'exit'", 
			"'print'", "'println'", "'if'", "'then'", "'else'", "'fi'", "'while'", 
			"'do'", "'done'", "'begin'", "'end'", "'call'", null, "'pair'", "'newpair'", 
			"'fst'", "'snd'", "'int'", "'bool'", "'char'", "'string'", "'('", "')'", 
			"'['", "']'", "'!'", "'len'", "'ord'", "'chr'", "'='", "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", "'&&'", 
			"'||'", "';'", "','", null, null, null, null, "'null'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COMMENT", "IS", "S_SKIP", "READ", "FREE", "RETURN", "EXIT", "PRINT", 
			"PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", "DO", "DONE", "BEGIN", 
			"END", "CALL", "INTEGER", "PAIR", "NEW_PAIR", "FST", "SND", "INT", "BOOL", 
			"CHAR", "STRING", "P_OPEN", "P_CLOSE", "SB_OPEN", "SB_CLOSE", "NOT", 
			"LEN", "ORD", "CHR", "ASSIGN", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", 
			"MODULO", "GREATER_THAN", "GREATER_THAN_OR_EQUAL", "LESS_THAN", "LESS_THAN_OR_EQUAL", 
			"EQUAL", "NOT_EQUAL", "AND", "OR", "SEMI_COLON", "COMMA", "EOL", "CHAR_LITER", 
			"STR_LITER", "BOOL_LITER", "NULL", "IDENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public BasicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BasicLexer.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2<\u0180\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\3\2\3\2\7\2\u0080\n\2\f\2\16\2\u0083\13\2\3\2\3\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\26\6\26\u00e5\n\26\r\26"+
		"\16\26\u00e6\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3"+
		"\30\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3"+
		"\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3$\3%\3%\3"+
		"%\3%\3&\3&\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3."+
		"\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62\3\63\3\63\3\63\3\64"+
		"\3\64\3\64\3\65\3\65\3\66\3\66\3\67\6\67\u0150\n\67\r\67\16\67\u0151\3"+
		"\67\3\67\38\38\39\39\39\59\u015b\n9\3:\3:\3:\3:\3;\3;\7;\u0163\n;\f;\16"+
		";\u0166\13;\3;\3;\3<\3<\3<\3<\3<\3<\3<\3<\3<\5<\u0173\n<\3=\3=\3=\3=\3"+
		"=\3>\3>\7>\u017c\n>\f>\16>\u017f\13>\2\2?\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\2+\26"+
		"-\27/\30\61\31\63\32\65\33\67\349\35;\36=\37? A!C\"E#G$I%K&M\'O(Q)S*U"+
		"+W,Y-[.]/_\60a\61c\62e\63g\64i\65k\66m\67o\2q\2s8u9w:y;{<\3\2\b\3\2\f"+
		"\f\5\2\13\f\17\17\"\"\13\2$$))\62\62^^ddhhppttvv\4\2$$))\5\2C\\aac|\6"+
		"\2\62;C\\aac|\2\u0183\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2"+
		"\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2"+
		"S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3"+
		"\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2"+
		"\2\2m\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\3"+
		"}\3\2\2\2\5\u0088\3\2\2\2\7\u008b\3\2\2\2\t\u0090\3\2\2\2\13\u0095\3\2"+
		"\2\2\r\u009a\3\2\2\2\17\u00a1\3\2\2\2\21\u00a6\3\2\2\2\23\u00ac\3\2\2"+
		"\2\25\u00b4\3\2\2\2\27\u00b7\3\2\2\2\31\u00bc\3\2\2\2\33\u00c1\3\2\2\2"+
		"\35\u00c4\3\2\2\2\37\u00ca\3\2\2\2!\u00cd\3\2\2\2#\u00d2\3\2\2\2%\u00d8"+
		"\3\2\2\2\'\u00dc\3\2\2\2)\u00e1\3\2\2\2+\u00e4\3\2\2\2-\u00e8\3\2\2\2"+
		"/\u00ed\3\2\2\2\61\u00f5\3\2\2\2\63\u00f9\3\2\2\2\65\u00fd\3\2\2\2\67"+
		"\u0101\3\2\2\29\u0106\3\2\2\2;\u010b\3\2\2\2=\u0112\3\2\2\2?\u0114\3\2"+
		"\2\2A\u0116\3\2\2\2C\u0118\3\2\2\2E\u011a\3\2\2\2G\u011c\3\2\2\2I\u0120"+
		"\3\2\2\2K\u0124\3\2\2\2M\u0128\3\2\2\2O\u012a\3\2\2\2Q\u012c\3\2\2\2S"+
		"\u012e\3\2\2\2U\u0130\3\2\2\2W\u0132\3\2\2\2Y\u0134\3\2\2\2[\u0136\3\2"+
		"\2\2]\u0139\3\2\2\2_\u013b\3\2\2\2a\u013e\3\2\2\2c\u0141\3\2\2\2e\u0144"+
		"\3\2\2\2g\u0147\3\2\2\2i\u014a\3\2\2\2k\u014c\3\2\2\2m\u014f\3\2\2\2o"+
		"\u0155\3\2\2\2q\u015a\3\2\2\2s\u015c\3\2\2\2u\u0160\3\2\2\2w\u0172\3\2"+
		"\2\2y\u0174\3\2\2\2{\u0179\3\2\2\2}\u0081\7%\2\2~\u0080\n\2\2\2\177~\3"+
		"\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0084\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\7\f\2\2\u0085\u0086\3\2"+
		"\2\2\u0086\u0087\b\2\2\2\u0087\4\3\2\2\2\u0088\u0089\7k\2\2\u0089\u008a"+
		"\7u\2\2\u008a\6\3\2\2\2\u008b\u008c\7u\2\2\u008c\u008d\7m\2\2\u008d\u008e"+
		"\7k\2\2\u008e\u008f\7r\2\2\u008f\b\3\2\2\2\u0090\u0091\7t\2\2\u0091\u0092"+
		"\7g\2\2\u0092\u0093\7c\2\2\u0093\u0094\7f\2\2\u0094\n\3\2\2\2\u0095\u0096"+
		"\7h\2\2\u0096\u0097\7t\2\2\u0097\u0098\7g\2\2\u0098\u0099\7g\2\2\u0099"+
		"\f\3\2\2\2\u009a\u009b\7t\2\2\u009b\u009c\7g\2\2\u009c\u009d\7v\2\2\u009d"+
		"\u009e\7w\2\2\u009e\u009f\7t\2\2\u009f\u00a0\7p\2\2\u00a0\16\3\2\2\2\u00a1"+
		"\u00a2\7g\2\2\u00a2\u00a3\7z\2\2\u00a3\u00a4\7k\2\2\u00a4\u00a5\7v\2\2"+
		"\u00a5\20\3\2\2\2\u00a6\u00a7\7r\2\2\u00a7\u00a8\7t\2\2\u00a8\u00a9\7"+
		"k\2\2\u00a9\u00aa\7p\2\2\u00aa\u00ab\7v\2\2\u00ab\22\3\2\2\2\u00ac\u00ad"+
		"\7r\2\2\u00ad\u00ae\7t\2\2\u00ae\u00af\7k\2\2\u00af\u00b0\7p\2\2\u00b0"+
		"\u00b1\7v\2\2\u00b1\u00b2\7n\2\2\u00b2\u00b3\7p\2\2\u00b3\24\3\2\2\2\u00b4"+
		"\u00b5\7k\2\2\u00b5\u00b6\7h\2\2\u00b6\26\3\2\2\2\u00b7\u00b8\7v\2\2\u00b8"+
		"\u00b9\7j\2\2\u00b9\u00ba\7g\2\2\u00ba\u00bb\7p\2\2\u00bb\30\3\2\2\2\u00bc"+
		"\u00bd\7g\2\2\u00bd\u00be\7n\2\2\u00be\u00bf\7u\2\2\u00bf\u00c0\7g\2\2"+
		"\u00c0\32\3\2\2\2\u00c1\u00c2\7h\2\2\u00c2\u00c3\7k\2\2\u00c3\34\3\2\2"+
		"\2\u00c4\u00c5\7y\2\2\u00c5\u00c6\7j\2\2\u00c6\u00c7\7k\2\2\u00c7\u00c8"+
		"\7n\2\2\u00c8\u00c9\7g\2\2\u00c9\36\3\2\2\2\u00ca\u00cb\7f\2\2\u00cb\u00cc"+
		"\7q\2\2\u00cc \3\2\2\2\u00cd\u00ce\7f\2\2\u00ce\u00cf\7q\2\2\u00cf\u00d0"+
		"\7p\2\2\u00d0\u00d1\7g\2\2\u00d1\"\3\2\2\2\u00d2\u00d3\7d\2\2\u00d3\u00d4"+
		"\7g\2\2\u00d4\u00d5\7i\2\2\u00d5\u00d6\7k\2\2\u00d6\u00d7\7p\2\2\u00d7"+
		"$\3\2\2\2\u00d8\u00d9\7g\2\2\u00d9\u00da\7p\2\2\u00da\u00db\7f\2\2\u00db"+
		"&\3\2\2\2\u00dc\u00dd\7e\2\2\u00dd\u00de\7c\2\2\u00de\u00df\7n\2\2\u00df"+
		"\u00e0\7n\2\2\u00e0(\3\2\2\2\u00e1\u00e2\4\62;\2\u00e2*\3\2\2\2\u00e3"+
		"\u00e5\5)\25\2\u00e4\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e4\3\2"+
		"\2\2\u00e6\u00e7\3\2\2\2\u00e7,\3\2\2\2\u00e8\u00e9\7r\2\2\u00e9\u00ea"+
		"\7c\2\2\u00ea\u00eb\7k\2\2\u00eb\u00ec\7t\2\2\u00ec.\3\2\2\2\u00ed\u00ee"+
		"\7p\2\2\u00ee\u00ef\7g\2\2\u00ef\u00f0\7y\2\2\u00f0\u00f1\7r\2\2\u00f1"+
		"\u00f2\7c\2\2\u00f2\u00f3\7k\2\2\u00f3\u00f4\7t\2\2\u00f4\60\3\2\2\2\u00f5"+
		"\u00f6\7h\2\2\u00f6\u00f7\7u\2\2\u00f7\u00f8\7v\2\2\u00f8\62\3\2\2\2\u00f9"+
		"\u00fa\7u\2\2\u00fa\u00fb\7p\2\2\u00fb\u00fc\7f\2\2\u00fc\64\3\2\2\2\u00fd"+
		"\u00fe\7k\2\2\u00fe\u00ff\7p\2\2\u00ff\u0100\7v\2\2\u0100\66\3\2\2\2\u0101"+
		"\u0102\7d\2\2\u0102\u0103\7q\2\2\u0103\u0104\7q\2\2\u0104\u0105\7n\2\2"+
		"\u01058\3\2\2\2\u0106\u0107\7e\2\2\u0107\u0108\7j\2\2\u0108\u0109\7c\2"+
		"\2\u0109\u010a\7t\2\2\u010a:\3\2\2\2\u010b\u010c\7u\2\2\u010c\u010d\7"+
		"v\2\2\u010d\u010e\7t\2\2\u010e\u010f\7k\2\2\u010f\u0110\7p\2\2\u0110\u0111"+
		"\7i\2\2\u0111<\3\2\2\2\u0112\u0113\7*\2\2\u0113>\3\2\2\2\u0114\u0115\7"+
		"+\2\2\u0115@\3\2\2\2\u0116\u0117\7]\2\2\u0117B\3\2\2\2\u0118\u0119\7_"+
		"\2\2\u0119D\3\2\2\2\u011a\u011b\7#\2\2\u011bF\3\2\2\2\u011c\u011d\7n\2"+
		"\2\u011d\u011e\7g\2\2\u011e\u011f\7p\2\2\u011fH\3\2\2\2\u0120\u0121\7"+
		"q\2\2\u0121\u0122\7t\2\2\u0122\u0123\7f\2\2\u0123J\3\2\2\2\u0124\u0125"+
		"\7e\2\2\u0125\u0126\7j\2\2\u0126\u0127\7t\2\2\u0127L\3\2\2\2\u0128\u0129"+
		"\7?\2\2\u0129N\3\2\2\2\u012a\u012b\7-\2\2\u012bP\3\2\2\2\u012c\u012d\7"+
		"/\2\2\u012dR\3\2\2\2\u012e\u012f\7,\2\2\u012fT\3\2\2\2\u0130\u0131\7\61"+
		"\2\2\u0131V\3\2\2\2\u0132\u0133\7\'\2\2\u0133X\3\2\2\2\u0134\u0135\7@"+
		"\2\2\u0135Z\3\2\2\2\u0136\u0137\7@\2\2\u0137\u0138\7?\2\2\u0138\\\3\2"+
		"\2\2\u0139\u013a\7>\2\2\u013a^\3\2\2\2\u013b\u013c\7>\2\2\u013c\u013d"+
		"\7?\2\2\u013d`\3\2\2\2\u013e\u013f\7?\2\2\u013f\u0140\7?\2\2\u0140b\3"+
		"\2\2\2\u0141\u0142\7#\2\2\u0142\u0143\7?\2\2\u0143d\3\2\2\2\u0144\u0145"+
		"\7(\2\2\u0145\u0146\7(\2\2\u0146f\3\2\2\2\u0147\u0148\7~\2\2\u0148\u0149"+
		"\7~\2\2\u0149h\3\2\2\2\u014a\u014b\7=\2\2\u014bj\3\2\2\2\u014c\u014d\7"+
		".\2\2\u014dl\3\2\2\2\u014e\u0150\t\3\2\2\u014f\u014e\3\2\2\2\u0150\u0151"+
		"\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0154\b\67\2\2\u0154n\3\2\2\2\u0155\u0156\t\4\2\2\u0156p\3\2\2\2\u0157"+
		"\u015b\n\5\2\2\u0158\u0159\7^\2\2\u0159\u015b\5o8\2\u015a\u0157\3\2\2"+
		"\2\u015a\u0158\3\2\2\2\u015br\3\2\2\2\u015c\u015d\7)\2\2\u015d\u015e\5"+
		"q9\2\u015e\u015f\7)\2\2\u015ft\3\2\2\2\u0160\u0164\7$\2\2\u0161\u0163"+
		"\5q9\2\u0162\u0161\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2\2\2\u0164"+
		"\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u0168\7$"+
		"\2\2\u0168v\3\2\2\2\u0169\u016a\7v\2\2\u016a\u016b\7t\2\2\u016b\u016c"+
		"\7w\2\2\u016c\u0173\7g\2\2\u016d\u016e\7h\2\2\u016e\u016f\7c\2\2\u016f"+
		"\u0170\7n\2\2\u0170\u0171\7u\2\2\u0171\u0173\7g\2\2\u0172\u0169\3\2\2"+
		"\2\u0172\u016d\3\2\2\2\u0173x\3\2\2\2\u0174\u0175\7p\2\2\u0175\u0176\7"+
		"w\2\2\u0176\u0177\7n\2\2\u0177\u0178\7n\2\2\u0178z\3\2\2\2\u0179\u017d"+
		"\t\6\2\2\u017a\u017c\t\7\2\2\u017b\u017a\3\2\2\2\u017c\u017f\3\2\2\2\u017d"+
		"\u017b\3\2\2\2\u017d\u017e\3\2\2\2\u017e|\3\2\2\2\u017f\u017d\3\2\2\2"+
		"\n\2\u0081\u00e6\u0151\u015a\u0164\u0172\u017d\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}