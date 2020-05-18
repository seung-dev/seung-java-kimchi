package seung.java.kimchi.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

/**
 * <pre>
 * charsets enum
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public enum SCharset {

	Big5("Big5", "csBig5"),
	Big5_HKSCS("Big5-HKSCS", "big5-hkscs", "big5hk", "Big5_HKSCS", "big5hkscs"),
	CESU_8("CESU-8", "CESU8", "csCESU-8"),
	EUC_JP("EUC-JP", "csEUCPkdFmtjapanese", "x-euc-jp", "eucjis", "Extended_UNIX_Code_Packed_Format_for_Japanese", "euc_jp", "eucjp", "x-eucjp"),
	EUC_KR("EUC-KR", "ksc5601-1987", "csEUCKR", "ksc5601_1987", "ksc5601", "5601", "euc_kr", "ksc_5601", "ks_c_5601-1987", "euckr"),
	GB18030("GB18030", "gb18030-2000"),
	GB2312("GB2312", "gb2312", "euc-cn", "x-EUC-CN", "euccn", "EUC_CN", "gb2312-80", "gb2312-1980"),
	GBK("GBK", "CP936", "windows-936"),
	IBM_Thai("IBM-Thai", "ibm-838", "ibm838", "838", "cp838"),
	IBM00858("IBM00858", "cp858", "858", "PC-Multilingual-850+euro", "cp00858", "ccsid00858"),
	IBM01140("IBM01140", "cp1140", "1140", "cp01140", "ebcdic-us-037+euro", "ccsid01140"),
	IBM01141("IBM01141", "1141", "cp1141", "cp01141", "ccsid01141", "ebcdic-de-273+euro"),
	IBM01142("IBM01142", "1142", "cp1142", "cp01142", "ccsid01142", "ebcdic-no-277+euro", "ebcdic-dk-277+euro"),
	IBM01143("IBM01143", "1143", "cp01143", "ccsid01143", "cp1143", "ebcdic-fi-278+euro", "ebcdic-se-278+euro"),
	IBM01144("IBM01144", "cp01144", "ccsid01144", "ebcdic-it-280+euro", "cp1144", "1144"),
	IBM01145("IBM01145", "ccsid01145", "ebcdic-es-284+euro", "1145", "cp1145", "cp01145"),
	IBM01146("IBM01146", "ebcdic-gb-285+euro", "1146", "cp1146", "cp01146", "ccsid01146"),
	IBM01147("IBM01147", "cp1147", "1147", "cp01147", "ccsid01147", "ebcdic-fr-277+euro"),
	IBM01148("IBM01148", "cp1148", "ebcdic-international-500+euro", "1148", "cp01148", "ccsid01148"),
	IBM01149("IBM01149", "ebcdic-s-871+euro", "1149", "cp1149", "cp01149", "ccsid01149"),
	IBM037("IBM037", "cp037", "ibm037", "ibm-037", "csIBM037", "ebcdic-cp-us", "ebcdic-cp-ca", "ebcdic-cp-nl", "ebcdic-cp-wt", "037", "cpibm37", "cs-ebcdic-cp-wt", "ibm-37", "cs-ebcdic-cp-us", "cs-ebcdic-cp-ca", "cs-ebcdic-cp-nl"),
	IBM1026("IBM1026", "cp1026", "ibm-1026", "1026", "ibm1026"),
	IBM1047("IBM1047", "ibm-1047", "1047", "cp1047"),
	IBM273("IBM273", "ibm-273", "ibm273", "273", "cp273"),
	IBM277("IBM277", "ibm277", "277", "cp277", "ibm-277"),
	IBM278("IBM278", "cp278", "278", "ibm-278", "ebcdic-cp-se", "csIBM278", "ibm278", "ebcdic-sv"),
	IBM280("IBM280", "ibm280", "280", "cp280", "ibm-280"),
	IBM284("IBM284", "csIBM284", "ibm-284", "cpibm284", "ibm284", "284", "cp284"),
	IBM285("IBM285", "csIBM285", "cp285", "ebcdic-gb", "ibm-285", "cpibm285", "ibm285", "285", "ebcdic-cp-gb"),
	IBM290("IBM290", "ibm290", "290", "cp290", "EBCDIC-JP-kana", "csIBM290", "ibm-290"),
	IBM297("IBM297", "297", "csIBM297", "cp297", "ibm297", "ibm-297", "cpibm297", "ebcdic-cp-fr"),
	IBM420("IBM420", "ibm420", "420", "cp420", "csIBM420", "ibm-420", "ebcdic-cp-ar1"),
	IBM424("IBM424", "ebcdic-cp-he", "csIBM424", "ibm-424", "ibm424", "424", "cp424"),
	IBM437("IBM437", "ibm437", "437", "ibm-437", "cspc8codepage437", "cp437", "windows-437"),
	IBM500("IBM500", "ibm-500", "ibm500", "500", "ebcdic-cp-bh", "ebcdic-cp-ch", "csIBM500", "cp500"),
	IBM775("IBM775", "ibm-775", "ibm775", "775", "cp775"),
	IBM850("IBM850", "cp850", "cspc850multilingual", "ibm850", "850", "ibm-850"),
	IBM852("IBM852", "csPCp852", "ibm-852", "ibm852", "852", "cp852"),
	IBM855("IBM855", "ibm855", "855", "ibm-855", "cp855", "cspcp855"),
	IBM857("IBM857", "ibm857", "857", "cp857", "csIBM857", "ibm-857"),
	IBM860("IBM860", "ibm860", "860", "cp860", "csIBM860", "ibm-860"),
	IBM861("IBM861", "cp861", "ibm861", "861", "ibm-861", "cp-is", "csIBM861"),
	IBM862("IBM862", "csIBM862", "cp862", "ibm862", "862", "cspc862latinhebrew", "ibm-862"),
	IBM863("IBM863", "csIBM863", "ibm-863", "ibm863", "863", "cp863"),
	IBM864("IBM864", "csIBM864", "ibm-864", "ibm864", "864", "cp864"),
	IBM865("IBM865", "ibm-865", "csIBM865", "cp865", "ibm865", "865"),
	IBM866("IBM866", "ibm866", "866", "ibm-866", "csIBM866", "cp866"),
	IBM868("IBM868", "ibm868", "868", "cp868", "csIBM868", "ibm-868", "cp-ar"),
	IBM869("IBM869", "cp869", "ibm869", "869", "ibm-869", "cp-gr", "csIBM869"),
	IBM870("IBM870", "870", "cp870", "csIBM870", "ibm-870", "ibm870", "ebcdic-cp-roece", "ebcdic-cp-yu"),
	IBM871("IBM871", "ibm871", "871", "cp871", "ebcdic-cp-is", "csIBM871", "ibm-871"),
	IBM918("IBM918", "918", "ibm-918", "ebcdic-cp-ar2", "cp918"),
	ISO_2022_JP("ISO-2022-JP", "csjisencoding", "iso2022jp", "jis_encoding", "jis", "csISO2022JP"),
	ISO_2022_JP_2("ISO-2022-JP-2", "csISO2022JP2", "iso2022jp2"),
	ISO_2022_KR("ISO-2022-KR", "csISO2022KR", "ISO2022KR"),
	ISO_8859_1("ISO-8859-1", "819", "ISO8859-1", "l1", "ISO_8859-1:1987", "ISO_8859-1", "8859_1", "iso-ir-100", "latin1", "cp819", "ISO8859_1", "IBM819", "ISO_8859_1", "IBM-819", "csISOLatin1"),
	ISO_8859_13("ISO-8859-13", "iso_8859-13", "ISO8859-13", "iso8859_13", "8859_13"),
	ISO_8859_15("ISO-8859-15", "ISO8859-15", "LATIN0", "ISO8859_15_FDIS", "ISO8859_15", "cp923", "8859_15", "L9", "ISO-8859-15", "IBM923", "csISOlatin9", "ISO_8859-15", "IBM-923", "csISOlatin0", "923", "LATIN9"),
	ISO_8859_2("ISO-8859-2", "ISO8859-2", "ibm912", "l2", "ISO_8859-2", "8859_2", "cp912", "ISO_8859-2:1987", "iso8859_2", "iso-ir-101", "latin2", "912", "csISOLatin2", "ibm-912"),
	ISO_8859_3("ISO-8859-3", "ISO8859-3", "ibm913", "8859_3", "l3", "cp913", "ISO_8859-3", "iso8859_3", "latin3", "csISOLatin3", "913", "ISO_8859-3:1988", "ibm-913", "iso-ir-109"),
	ISO_8859_4("ISO-8859-4", "8859_4", "latin4", "l4", "cp914", "ISO_8859-4:1988", "ibm914", "ISO_8859-4", "iso-ir-110", "iso8859_4", "csISOLatin4", "iso8859-4", "914", "ibm-914"),
	ISO_8859_5("ISO-8859-5", "ISO_8859-5:1988", "csISOLatinCyrillic", "iso-ir-144", "iso8859_5", "cp915", "8859_5", "ibm-915", "ISO_8859-5", "ibm915", "915", "cyrillic", "ISO8859-5"),
	ISO_8859_6("ISO-8859-6", "ASMO-708", "8859_6", "iso8859_6", "ISO_8859-6", "csISOLatinArabic", "ibm1089", "arabic", "ibm-1089", "1089", "ECMA-114", "iso-ir-127", "ISO_8859-6:1987", "ISO8859-6", "cp1089"),
	ISO_8859_7("ISO-8859-7", "greek", "8859_7", "greek8", "ibm813", "ISO_8859-7", "iso8859_7", "ELOT_928", "cp813", "ISO_8859-7:1987", "sun_eu_greek", "csISOLatinGreek", "iso-ir-126", "813", "iso8859-7", "ECMA-118", "ibm-813"),
	ISO_8859_8("ISO-8859-8", "8859_8", "ISO_8859-8", "ISO_8859-8:1988", "cp916", "iso-ir-138", "ISO8859-8", "hebrew", "iso8859_8", "ibm-916", "csISOLatinHebrew", "916", "ibm916"),
	ISO_8859_9("ISO-8859-9", "ibm-920", "ISO_8859-9", "8859_9", "ISO_8859-9:1989", "ibm920", "latin5", "l5", "iso8859_9", "cp920", "920", "iso-ir-148", "ISO8859-9", "csISOLatin5"),
	JIS_X0201("JIS_X0201", "JIS0201", "csHalfWidthKatakana", "X0201", "JIS_X0201"),
	JIS_X0212_1990("JIS_X0212-1990", "JIS0212", "iso-ir-159", "x0212", "jis_x0212-1990", "csISO159JISX02121990"),
	KOI8_R("KOI8-R", "koi8_r", "koi8", "cskoi8r"),
	KOI8_U("KOI8-U", "koi8_u"),
	Shift_JIS("Shift_JIS", "shift_jis", "x-sjis", "sjis", "shift-jis", "ms_kanji", "csShiftJIS"),
	TIS_620("TIS-620", "tis620", "tis620.2533"),
	US_ASCII("US-ASCII", "ANSI_X3.4-1968", "cp367", "csASCII", "iso-ir-6", "ASCII", "iso_646.irv:1983", "ANSI_X3.4-1986", "ascii7", "default", "ISO_646.irv:1991", "ISO646-US", "IBM367", "646", "us"),
	UTF_16("UTF-16", "UTF_16", "unicode", "utf16", "UnicodeBig"),
	UTF_16BE("UTF-16BE", "X-UTF-16BE", "UTF_16BE", "ISO-10646-UCS-2", "UnicodeBigUnmarked"),
	UTF_16LE("UTF-16LE", "UnicodeLittleUnmarked", "UTF_16LE", "X-UTF-16LE"),
	UTF_32("UTF-32", "UTF_32", "UTF32"),
	UTF_32BE("UTF-32BE", "X-UTF-32BE", "UTF_32BE"),
	UTF_32LE("UTF-32LE", "X-UTF-32LE", "UTF_32LE"),
	UTF_8("UTF-8", "unicode-1-1-utf-8", "UTF8"),
	windows_1250("windows-1250", "cp1250", "cp5346"),
	windows_1251("windows-1251", "cp5347", "ansi-1251", "cp1251"),
	windows_1252("windows-1252", "cp5348", "cp1252"),
	windows_1253("windows-1253", "cp1253", "cp5349"),
	windows_1254("windows-1254", "cp1254", "cp5350"),
	windows_1255("windows-1255", "cp1255"),
	windows_1256("windows-1256", "cp1256"),
	windows_1257("windows-1257", "cp1257", "cp5353"),
	windows_1258("windows-1258", "cp1258"),
	windows_31j("windows-31j", "MS932", "windows-932", "csWindows31J"),
	x_Big5_HKSCS_2001("x-Big5-HKSCS-2001", "Big5_HKSCS_2001", "big5-hkscs-2001", "big5hk-2001", "big5-hkscs:unicode3.0", "big5hkscs-2001"),
	x_Big5_Solaris("x-Big5-Solaris", "Big5_Solaris"),
	x_euc_jp_linux("x-euc-jp-linux", "euc_jp_linux", "euc-jp-linux"),
	x_EUC_TW("x-EUC-TW", "euctw", "cns11643", "EUC-TW", "euc_tw"),
	x_eucJP_Open("x-eucJP-Open", "eucJP-open", "EUC_JP_Solaris"),
	x_IBM1006("x-IBM1006", "ibm1006", "ibm-1006", "1006", "cp1006"),
	x_IBM1025("x-IBM1025", "ibm-1025", "1025", "cp1025", "ibm1025"),
	x_IBM1046("x-IBM1046", "ibm1046", "ibm-1046", "1046", "cp1046"),
	x_IBM1097("x-IBM1097", "ibm1097", "ibm-1097", "1097", "cp1097"),
	x_IBM1098("x-IBM1098", "ibm-1098", "1098", "cp1098", "ibm1098"),
	x_IBM1112("x-IBM1112", "ibm1112", "ibm-1112", "1112", "cp1112"),
	x_IBM1122("x-IBM1122", "cp1122", "ibm1122", "ibm-1122", "1122"),
	x_IBM1123("x-IBM1123", "ibm1123", "ibm-1123", "1123", "cp1123"),
	x_IBM1124("x-IBM1124", "ibm-1124", "1124", "cp1124", "ibm1124"),
	x_IBM1166("x-IBM1166", "cp1166", "ibm1166", "ibm-1166", "1166"),
	x_IBM1364("x-IBM1364", "cp1364", "ibm1364", "ibm-1364", "1364"),
	x_IBM1381("x-IBM1381", "cp1381", "ibm-1381", "1381", "ibm1381"),
	x_IBM1383("x-IBM1383", "ibm1383", "ibm-1383", "1383", "cp1383"),
	x_IBM300("x-IBM300", "cp300", "ibm300", "300", "ibm-300"),
	x_IBM33722("x-IBM33722", "33722", "ibm-33722", "cp33722", "ibm33722", "ibm-5050", "ibm-33722_vascii_vpua"),
	x_IBM737("x-IBM737", "cp737", "ibm737", "737", "ibm-737"),
	x_IBM833("x-IBM833", "ibm833", "cp833", "ibm-833"),
	x_IBM834("x-IBM834", "ibm834", "834", "cp834", "ibm-834"),
	x_IBM856("x-IBM856", "ibm856", "856", "cp856", "ibm-856"),
	x_IBM874("x-IBM874", "ibm-874", "ibm874", "874", "cp874"),
	x_IBM875("x-IBM875", "ibm-875", "ibm875", "875", "cp875"),
	x_IBM921("x-IBM921", "ibm921", "921", "ibm-921", "cp921"),
	x_IBM922("x-IBM922", "ibm922", "922", "cp922", "ibm-922"),
	x_IBM930("x-IBM930", "ibm-930", "ibm930", "930", "cp930"),
	x_IBM933("x-IBM933", "ibm933", "933", "cp933", "ibm-933"),
	x_IBM935("x-IBM935", "cp935", "ibm935", "935", "ibm-935"),
	x_IBM937("x-IBM937", "ibm-937", "ibm937", "937", "cp937"),
	x_IBM939("x-IBM939", "ibm-939", "cp939", "ibm939", "939"),
	x_IBM942("x-IBM942", "ibm-942", "cp942", "ibm942", "942"),
	x_IBM942C("x-IBM942C", "ibm942C", "cp942C", "ibm-942C", "942C"),
	x_IBM943("x-IBM943", "ibm943", "943", "ibm-943", "cp943"),
	x_IBM943C("x-IBM943C", "943C", "cp943C", "ibm943C", "ibm-943C"),
	x_IBM948("x-IBM948", "ibm-948", "ibm948", "948", "cp948"),
	x_IBM949("x-IBM949", "ibm-949", "ibm949", "949", "cp949"),
	x_IBM949C("x-IBM949C", "ibm949C", "ibm-949C", "cp949C", "949C"),
	x_IBM950("x-IBM950", "cp950", "ibm950", "950", "ibm-950"),
	x_IBM964("x-IBM964", "ibm-964", "cp964", "ibm964", "964"),
	x_IBM970("x-IBM970", "ibm970", "ibm-eucKR", "970", "cp970", "ibm-970"),
	x_ISCII91("x-ISCII91", "ISCII91", "iso-ir-153", "iscii", "ST_SEV_358-88", "csISO153GOST1976874"),
	x_ISO_2022_CN_CNS("x-ISO-2022-CN-CNS", "ISO2022CN_CNS", "ISO-2022-CN-CNS"),
	x_ISO_2022_CN_GB("x-ISO-2022-CN-GB", "ISO2022CN_GB", "ISO-2022-CN-GB"),
	x_iso_8859_11("x-iso-8859-11", "iso-8859-11", "iso8859_11"),
	x_JIS0208("x-JIS0208", "JIS0208", "JIS_C6226-1983", "iso-ir-87", "x0208", "JIS_X0208-1983", "csISO87JISX0208"),
	x_Johab("x-Johab", "ms1361", "ksc5601_1992", "johab", "ksc5601-1992"),
	x_MacArabic("x-MacArabic", "MacArabic"),
	x_MacCentralEurope("x-MacCentralEurope", "MacCentralEurope"),
	x_MacCroatian("x-MacCroatian", "MacCroatian"),
	x_MacCyrillic("x-MacCyrillic", "MacCyrillic"),
	x_MacDingbat("x-MacDingbat", "MacDingbat"),
	x_MacGreek("x-MacGreek", "MacGreek"),
	x_MacHebrew("x-MacHebrew", "MacHebrew"),
	x_MacIceland("x-MacIceland", "MacIceland"),
	x_MacRoman("x-MacRoman", "MacRoman"),
	x_MacRomania("x-MacRomania", "MacRomania"),
	x_MacSymbol("x-MacSymbol", "MacSymbol"),
	x_MacThai("x-MacThai", "MacThai"),
	x_MacTurkish("x-MacTurkish", "MacTurkish"),
	x_MacUkraine("x-MacUkraine", "MacUkraine"),
	x_MS932_0213("x-MS932_0213"),
	x_MS950_HKSCS("x-MS950-HKSCS", "MS950_HKSCS"),
	x_MS950_HKSCS_XP("x-MS950-HKSCS-XP", "MS950_HKSCS_XP"),
	x_mswin_936("x-mswin-936", "ms936", "ms_936"),
	x_PCK("x-PCK", "pck"),
	x_SJIS_0213("x-SJIS_0213"),
	x_UTF_16LE_BOM("x-UTF-16LE-BOM", "UnicodeLittle"),
	X_UTF_32BE_BOM("X-UTF-32BE-BOM", "UTF_32BE_BOM", "UTF-32BE-BOM"),
	X_UTF_32LE_BOM("X-UTF-32LE-BOM", "UTF_32LE_BOM", "UTF-32LE-BOM"),
	x_windows_50220("x-windows-50220", "cp50220", "ms50220"),
	x_windows_50221("x-windows-50221", "cp50221", "ms50221"),
	x_windows_874("x-windows-874", "ms-874", "ms874", "windows-874"),
	x_windows_949("x-windows-949", "windows949", "ms949", "windows-949", "ms_949"),
	x_windows_950("x-windows-950", "ms950", "windows-950"),
	x_windows_iso2022jp("x-windows-iso2022jp", "windows-iso2022jp")
	;
	
	private final List<String> aliases;
	
	private SCharset(String... aliases) {
		String[] clone = new String[aliases.length + 1];
		clone[0] = this.name();
		if(aliases.length > 0) {
			System.arraycopy(aliases, 0, clone, 1, aliases.length);
		}
		this.aliases = Arrays.asList(clone);
	}
	
	public String text() {
		return aliases().get(1);
	}
	
	public List<String> aliases() {
		return aliases;
	}
	
	public static boolean hasValue(String alias) {
		for(SCharset sCharset : SCharset.values()) {
			for(String compare : sCharset.aliases()) {
				if(compare.toLowerCase().replaceAll("-", "_").equals(alias.toLowerCase().replaceAll("-", "_"))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static SCharset forAlias(String alias) {
		for(SCharset sCharset : SCharset.values()) {
			for(String compare : sCharset.aliases()) {
				if(compare.toLowerCase().replaceAll("-", "_").equals(alias.toLowerCase().replaceAll("-", "_"))) {
					return sCharset;
				}
			}
		}
		return null;
	}
	
	public static List<List<String>> availableCharsetList() {
		
		List<List<String>> availableCharsetList = new ArrayList<List<String>>();
		
		SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
		
//		Charset charset = null;
//		StringBuffer stringBuffer = null;
//		for(String key : availableCharsets.keySet()) {
//			charset = availableCharsets.get(key);
//			stringBuffer = new StringBuffer();
//			if(!charset.canEncode()) {
//				continue;
//			}
//			stringBuffer.append(String.format("\"%s\"", charset.displayName()));
//			if(charset.aliases().size() > 0) {
//				for(String alias : charset.aliases()) {
//					stringBuffer.append(String.format(", \"%s\"", alias));
//				}
//			}
//			System.out.println(String.format("%s(%s),", charset.displayName().replaceAll("-", "_"), stringBuffer.toString()));
//		}
		
		List<String> availableCharset = null;
		Charset charset = null;
		for(String key : availableCharsets.keySet()) {
			charset = availableCharsets.get(key);
			if(!charset.canEncode()) {
				continue;
			}
			availableCharset = new ArrayList<String>();
			availableCharset.add(charset.name());
			if(charset.aliases().size() > 0) {
				for(String alias : charset.aliases()) {
					availableCharset.add(alias);
				}
			}
			availableCharsetList.add(availableCharset);
		}
		
		return availableCharsetList;
	}
	
}
