/* Copyright (c) 2016 lib4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.lib4j.xml;

import java.util.HashMap;
import java.util.Map;

public class XMLText {
  private static final Map<String,Integer> characterMap = new HashMap<String,Integer>();

  static {
    characterMap.put("nbsp", 160);
    characterMap.put("iexcl", 161);
    characterMap.put("cent", 162);
    characterMap.put("pound", 163);
    characterMap.put("curren", 164);
    characterMap.put("yen", 165);
    characterMap.put("brvbar", 166);
    characterMap.put("sect", 167);
    characterMap.put("uml", 168);
    characterMap.put("copy", 169);
    characterMap.put("ordf", 170);
    characterMap.put("laquo", 171);
    characterMap.put("not", 172);
    characterMap.put("shy", 173);
    characterMap.put("reg", 174);
    characterMap.put("macr", 175);
    characterMap.put("deg", 176);
    characterMap.put("plusmn", 177);
    characterMap.put("sup2", 178);
    characterMap.put("sup3", 179);
    characterMap.put("acute", 180);
    characterMap.put("micro", 181);
    characterMap.put("para", 182);
    characterMap.put("middot", 183);
    characterMap.put("cedil", 184);
    characterMap.put("sup1", 185);
    characterMap.put("ordm", 186);
    characterMap.put("raquo", 187);
    characterMap.put("frac14", 188);
    characterMap.put("frac12", 189);
    characterMap.put("frac34", 190);
    characterMap.put("iquest", 191);
    characterMap.put("Agrave", 192);
    characterMap.put("Aacute", 193);
    characterMap.put("Acirc", 194);
    characterMap.put("Atilde", 195);
    characterMap.put("Auml", 196);
    characterMap.put("Aring", 197);
    characterMap.put("AElig", 198);
    characterMap.put("Ccedil", 199);
    characterMap.put("Egrave", 200);
    characterMap.put("Eacute", 201);
    characterMap.put("Ecirc", 202);
    characterMap.put("Euml", 203);
    characterMap.put("Igrave", 204);
    characterMap.put("Iacute", 205);
    characterMap.put("Icirc", 206);
    characterMap.put("Iuml", 207);
    characterMap.put("ETH", 208);
    characterMap.put("Ntilde", 209);
    characterMap.put("Ograve", 210);
    characterMap.put("Oacute", 211);
    characterMap.put("Ocirc", 212);
    characterMap.put("Otilde", 213);
    characterMap.put("Ouml", 214);
    characterMap.put("times", 215);
    characterMap.put("Oslash", 216);
    characterMap.put("Ugrave", 217);
    characterMap.put("Uacute", 218);
    characterMap.put("Ucirc", 219);
    characterMap.put("Uuml", 220);
    characterMap.put("Yacute", 221);
    characterMap.put("THORN", 222);
    characterMap.put("szlig", 223);
    characterMap.put("agrave", 224);
    characterMap.put("aacute", 225);
    characterMap.put("acirc", 226);
    characterMap.put("atilde", 227);
    characterMap.put("auml", 228);
    characterMap.put("aring", 229);
    characterMap.put("aelig", 230);
    characterMap.put("ccedil", 231);
    characterMap.put("egrave", 232);
    characterMap.put("eacute", 233);
    characterMap.put("ecirc", 234);
    characterMap.put("euml", 235);
    characterMap.put("igrave", 236);
    characterMap.put("iacute", 237);
    characterMap.put("icirc", 238);
    characterMap.put("iuml", 239);
    characterMap.put("eth", 240);
    characterMap.put("ntilde", 241);
    characterMap.put("ograve", 242);
    characterMap.put("oacute", 243);
    characterMap.put("ocirc", 244);
    characterMap.put("otilde", 245);
    characterMap.put("ouml", 246);
    characterMap.put("divide", 247);
    characterMap.put("oslash", 248);
    characterMap.put("ugrave", 249);
    characterMap.put("uacute", 250);
    characterMap.put("ucirc", 251);
    characterMap.put("uuml", 252);
    characterMap.put("yacute", 253);
    characterMap.put("thorn", 254);
    characterMap.put("yuml", 255);
    characterMap.put("fnof", 402);
    characterMap.put("Alpha", 913);
    characterMap.put("Beta", 914);
    characterMap.put("Gamma", 915);
    characterMap.put("Delta", 916);
    characterMap.put("Epsilon", 917);
    characterMap.put("Zeta", 918);
    characterMap.put("Eta", 919);
    characterMap.put("Theta", 920);
    characterMap.put("Iota", 921);
    characterMap.put("Kappa", 922);
    characterMap.put("Lambda", 923);
    characterMap.put("Mu", 924);
    characterMap.put("Nu", 925);
    characterMap.put("Xi", 926);
    characterMap.put("Omicron", 927);
    characterMap.put("Pi", 928);
    characterMap.put("Rho", 929);
    characterMap.put("Sigma", 931);
    characterMap.put("Tau", 932);
    characterMap.put("Upsilon", 933);
    characterMap.put("Phi", 934);
    characterMap.put("Chi", 935);
    characterMap.put("Psi", 936);
    characterMap.put("Omega", 937);
    characterMap.put("alpha", 945);
    characterMap.put("beta", 946);
    characterMap.put("gamma", 947);
    characterMap.put("delta", 948);
    characterMap.put("epsilon", 949);
    characterMap.put("zeta", 950);
    characterMap.put("eta", 951);
    characterMap.put("theta", 952);
    characterMap.put("iota", 953);
    characterMap.put("kappa", 954);
    characterMap.put("lambda", 955);
    characterMap.put("mu", 956);
    characterMap.put("nu", 957);
    characterMap.put("xi", 958);
    characterMap.put("omicron", 959);
    characterMap.put("pi", 960);
    characterMap.put("rho", 961);
    characterMap.put("sigmaf", 962);
    characterMap.put("sigma", 963);
    characterMap.put("tau", 964);
    characterMap.put("upsilon", 965);
    characterMap.put("phi", 966);
    characterMap.put("chi", 967);
    characterMap.put("psi", 968);
    characterMap.put("omega", 969);
    characterMap.put("thetasym", 977);
    characterMap.put("upsih", 978);
    characterMap.put("piv", 982);
    characterMap.put("bull", 8226);
    characterMap.put("hellip", 8230);
    characterMap.put("prime", 8242);
    characterMap.put("Prime", 8243);
    characterMap.put("oline", 8254);
    characterMap.put("frasl", 8260);
    characterMap.put("weierp", 8472);
    characterMap.put("image", 8465);
    characterMap.put("real", 8476);
    characterMap.put("trade", 8482);
    characterMap.put("alefsym", 8501);
    characterMap.put("larr", 8592);
    characterMap.put("uarr", 8593);
    characterMap.put("rarr", 8594);
    characterMap.put("darr", 8595);
    characterMap.put("harr", 8596);
    characterMap.put("crarr", 8629);
    characterMap.put("lArr", 8656);
    characterMap.put("uArr", 8657);
    characterMap.put("rArr", 8658);
    characterMap.put("dArr", 8659);
    characterMap.put("hArr", 8660);
    characterMap.put("forall", 8704);
    characterMap.put("part", 8706);
    characterMap.put("exist", 8707);
    characterMap.put("empty", 8709);
    characterMap.put("nabla", 8711);
    characterMap.put("isin", 8712);
    characterMap.put("notin", 8713);
    characterMap.put("ni", 8715);
    characterMap.put("prod", 8719);
    characterMap.put("sum", 8721);
    characterMap.put("minus", 8722);
    characterMap.put("lowast", 8727);
    characterMap.put("radic", 8730);
    characterMap.put("prop", 8733);
    characterMap.put("infin", 8734);
    characterMap.put("ang", 8736);
    characterMap.put("and", 8743);
    characterMap.put("or", 8744);
    characterMap.put("cap", 8745);
    characterMap.put("cup", 8746);
    characterMap.put("int", 8747);
    characterMap.put("there4", 8756);
    characterMap.put("sim", 8764);
    characterMap.put("cong", 8773);
    characterMap.put("asymp", 8776);
    characterMap.put("ne", 8800);
    characterMap.put("equiv", 8801);
    characterMap.put("le", 8804);
    characterMap.put("ge", 8805);
    characterMap.put("sub", 8834);
    characterMap.put("sup", 8835);
    characterMap.put("nsub", 8836);
    characterMap.put("sube", 8838);
    characterMap.put("supe", 8839);
    characterMap.put("oplus", 8853);
    characterMap.put("otimes", 8855);
    characterMap.put("perp", 8869);
    characterMap.put("sdot", 8901);
    characterMap.put("lceil", 8968);
    characterMap.put("rceil", 8969);
    characterMap.put("lfloor", 8970);
    characterMap.put("rfloor", 8971);
    characterMap.put("lang", 9001);
    characterMap.put("rang", 9002);
    characterMap.put("loz", 9674);
    characterMap.put("spades", 9824);
    characterMap.put("clubs", 9827);
    characterMap.put("hearts", 9829);
    characterMap.put("diams", 9830);
    characterMap.put("quot", 34);
    characterMap.put("amp", 38);
    characterMap.put("lt", 60);
    characterMap.put("gt", 62);
    characterMap.put("OElig", 338);
    characterMap.put("oelig", 339);
    characterMap.put("Scaron", 352);
    characterMap.put("scaron", 353);
    characterMap.put("Yuml", 376);
    characterMap.put("circ", 710);
    characterMap.put("tilde", 732);
    characterMap.put("ensp", 8194);
    characterMap.put("emsp", 8195);
    characterMap.put("thinsp", 8201);
    characterMap.put("zwnj", 8204);
    characterMap.put("zwj", 8205);
    characterMap.put("lrm", 8206);
    characterMap.put("rlm", 8207);
    characterMap.put("ndash", 8211);
    characterMap.put("mdash", 8212);
    characterMap.put("lsquo", 8216);
    characterMap.put("rsquo", 8217);
    characterMap.put("sbquo", 8218);
    characterMap.put("ldquo", 8220);
    characterMap.put("rdquo", 8221);
    characterMap.put("bdquo", 8222);
    characterMap.put("dagger", 8224);
    characterMap.put("Dagger", 8225);
    characterMap.put("permil", 8240);
    characterMap.put("lsaquo", 8249);
    characterMap.put("rsaquo", 8250);
    characterMap.put("euro", 8364);
  }

  /**
   * Turn any HTML escape entities in the string into characters and return the resulting string.
   *
   * @param text
   *          String to be unescaped.
   * @return unescaped String.
   * @throws NullPointerException
   *           if s is null.
   */
  public static String unescapeXMLText(final String text) {
    final StringBuilder result = new StringBuilder(text.length());
    int ampInd = text.indexOf("&");
    int lastEnd = 0;
    while (ampInd >= 0) {
      final int nextAmp = text.indexOf("&", ampInd + 1);
      final int nextSemi = text.indexOf(";", ampInd + 1);
      if (nextSemi != -1 && (nextAmp == -1 || nextSemi < nextAmp)) {
        int value = -1;
        final String escape = text.substring(ampInd + 1, nextSemi);
        try {
          if (escape.startsWith("#")) {
            value = Integer.parseInt(escape.substring(1), 10);
          }
          else if (characterMap.containsKey(escape)) {
            value = characterMap.get(escape).intValue();
          }
        }
        catch (final NumberFormatException e) {
        }

        result.append(text.substring(lastEnd, ampInd));
        lastEnd = nextSemi + 1;
        if (0 <= value && value <= 0xffff) {
          result.append((char)value);
        }
        else {
          result.append("&").append(escape).append(";");
        }
      }

      ampInd = nextAmp;
    }

    result.append(text.substring(lastEnd));
    return result.toString();
  }
}