package br.com.iresult.gmfmobile.utils

import org.apache.commons.lang3.StringUtils


class NumberUtils {
    companion object {

        val MAX_DOUBLE_DIGITS = 15
        val DOUBLE_MAX_NON_EXP = 9.007199254740992E15; // 2^53
        val DOUBLE_MIN_NON_EXP = 1.1102230246251565E-16; // 2^-53
        val DOUBLE_POSITIVE_INFINITY_BITS = 0x7ff0000000000000L
        val DOUBLE_NEGATIVE_INFINITY_BITS = -0x10000000000000L
        val DOUBLE_NAN_BITS = 0x7ff8000000000000L
        val spaces = ""
        val zerosp:String = "0000000000" // guich@tc100: optimized the zeroPad method
        val LOWER = "\u0000\u0131\u0000\u03BC\u0000\u0101\u0000\u0103\u0000\u0105\u0000\u0107\u0000\u0109\u0000\u010B\u0000\u010D\u0000\u010F\u0000\u0111\u0000\u0113\u0000\u0115\u0000\u0117\u0000\u0119\u0000\u011B\u0000\u011D\u0000\u011F\u0000\u0121\u0000\u0123\u0000\u0125\u0000\u0127\u0000\u0129\u0000\u012B\u0000\u012D\u0000\u012F\u0000\u0069\u0000\u0133\u0000\u0135\u0000\u0137\u0000\u013A\u0000\u013C\u0000\u013E\u0000\u0140\u0000\u0142\u0000\u0144\u0000\u0146\u0000\u0148\u0000\u014B\u0000\u014D\u0000\u014F\u0000\u0151\u0000\u0153\u0000\u0155\u0000\u0157\u0000\u0159\u0000\u015B\u0000\u015D\u0000\u015F\u0000\u0161\u0000\u0163\u0000\u0165\u0000\u0167\u0000\u0169\u0000\u016B\u0000\u016D\u0000\u016F\u0000\u0171\u0000\u0173\u0000\u0175\u0000\u0177\u0000\u00FF\u0000\u017A\u0000\u017C\u0000\u017E\u0000\u0073\u0000\u0253\u0000\u0183\u0000\u0185\u0000\u0254\u0000\u0188\u0000\u0256\u0000\u0257\u0000\u018C\u0000\u01DD\u0000\u0259\u0000\u025B\u0000\u0192\u0000\u0260\u0000\u0263\u0000\u0269\u0000\u0268\u0000\u0199\u0000\u026F\u0000\u0272\u0000\u0275\u0000\u01A1\u0000\u01A3\u0000\u01A5\u0000\u0280\u0000\u01A8\u0000\u0283\u0000\u01AD\u0000\u0288\u0000\u01B0\u0000\u028A\u0000\u028B\u0000\u01B4\u0000\u01B6\u0000\u0292\u0000\u01B9\u0000\u01BD\u0000\u01C6\u0000\u01C6\u0000\u01C9\u0000\u01C9\u0000\u01CC\u0000\u01CC\u0000\u01CE\u0000\u01D0\u0000\u01D2\u0000\u01D4\u0000\u01D6\u0000\u01D8\u0000\u01DA\u0000\u01DC\u0000\u01DF\u0000\u01E1\u0000\u01E3\u0000\u01E5\u0000\u01E7\u0000\u01E9\u0000\u01EB\u0000\u01ED\u0000\u01EF\u0000\u01F3\u0000\u01F3\u0000\u01F5\u0000\u0195\u0000\u01BF\u0000\u01F9\u0000\u01FB\u0000\u01FD\u0000\u01FF\u0000\u0201\u0000\u0203\u0000\u0205\u0000\u0207\u0000\u0209\u0000\u020B\u0000\u020D\u0000\u020F\u0000\u0211\u0000\u0213\u0000\u0215\u0000\u0217\u0000\u0219\u0000\u021B\u0000\u021D\u0000\u021F\u0000\u019E\u0000\u0223\u0000\u0225\u0000\u0227\u0000\u0229\u0000\u022B\u0000\u022D\u0000\u022F\u0000\u0231\u0000\u0233\u0000\u03B9\u0000\u03AC\u0000\u03AD\u0000\u03AE\u0000\u03AF\u0000\u03CC\u0000\u03CD\u0000\u03CE\u0000\u03B1\u0000\u03B2\u0000\u03B3\u0000\u03B4\u0000\u03B5\u0000\u03B6\u0000\u03B7\u0000\u03B8\u0000\u03B9\u0000\u03BA\u0000\u03BB\u0000\u03BC\u0000\u03BD\u0000\u03BE\u0000\u03BF\u0000\u03C0\u0000\u03C1\u0000\u03C3\u0000\u03C4\u0000\u03C5\u0000\u03C6\u0000\u03C7\u0000\u03C8\u0000\u03C9\u0000\u03CA\u0000\u03CB\u0000\u03C3\u0000\u03B2\u0000\u03B8\u0000\u03C6\u0000\u03C0\u0000\u03D9\u0000\u03DB\u0000\u03DD\u0000\u03DF\u0000\u03E1\u0000\u03E3\u0000\u03E5\u0000\u03E7\u0000\u03E9\u0000\u03EB\u0000\u03ED\u0000\u03EF\u0000\u03BA\u0000\u03C1\u0000\u03B8\u0000\u03B5\u0000\u03F8\u0000\u03F2\u0000\u03FB\u0000\u0450\u0000\u0451\u0000\u0452\u0000\u0453\u0000\u0454\u0000\u0455\u0000\u0456\u0000\u0457\u0000\u0458\u0000\u0459\u0000\u045A\u0000\u045B\u0000\u045C\u0000\u045D\u0000\u045E\u0000\u045F\u0000\u0430\u0000\u0431\u0000\u0432\u0000\u0433\u0000\u0434\u0000\u0435\u0000\u0436\u0000\u0437\u0000\u0438\u0000\u0439\u0000\u043A\u0000\u043B\u0000\u043C\u0000\u043D\u0000\u043E\u0000\u043F\u0000\u0440\u0000\u0441\u0000\u0442\u0000\u0443\u0000\u0444\u0000\u0445\u0000\u0446\u0000\u0447\u0000\u0448\u0000\u0449\u0000\u044A\u0000\u044B\u0000\u044C\u0000\u044D\u0000\u044E\u0000\u044F\u0000\u0461\u0000\u0463\u0000\u0465\u0000\u0467\u0000\u0469\u0000\u046B\u0000\u046D\u0000\u046F\u0000\u0471\u0000\u0473\u0000\u0475\u0000\u0477\u0000\u0479\u0000\u047B\u0000\u047D\u0000\u047F\u0000\u0481\u0000\u048B\u0000\u048D\u0000\u048F\u0000\u0491\u0000\u0493\u0000\u0495\u0000\u0497\u0000\u0499\u0000\u049B\u0000\u049D\u0000\u049F\u0000\u04A1\u0000\u04A3\u0000\u04A5\u0000\u04A7\u0000\u04A9\u0000\u04AB\u0000\u04AD\u0000\u04AF\u0000\u04B1\u0000\u04B3\u0000\u04B5\u0000\u04B7\u0000\u04B9\u0000\u04BB\u0000\u04BD\u0000\u04BF\u0000\u04C2\u0000\u04C4\u0000\u04C6\u0000\u04C8\u0000\u04CA\u0000\u04CC\u0000\u04CE\u0000\u04D1\u0000\u04D3\u0000\u04D5\u0000\u04D7\u0000\u04D9\u0000\u04DB\u0000\u04DD\u0000\u04DF\u0000\u04E1\u0000\u04E3\u0000\u04E5\u0000\u04E7\u0000\u04E9\u0000\u04EB\u0000\u04ED\u0000\u04EF\u0000\u04F1\u0000\u04F3\u0000\u04F5\u0000\u04F9\u0000\u0501\u0000\u0503\u0000\u0505\u0000\u0507\u0000\u0509\u0000\u050B\u0000\u050D\u0000\u050F\u0000\u0561\u0000\u0562\u0000\u0563\u0000\u0564\u0000\u0565\u0000\u0566\u0000\u0567\u0000\u0568\u0000\u0569\u0000\u056A\u0000\u056B\u0000\u056C\u0000\u056D\u0000\u056E\u0000\u056F\u0000\u0570\u0000\u0571\u0000\u0572\u0000\u0573\u0000\u0574\u0000\u0575\u0000\u0576\u0000\u0577\u0000\u0578\u0000\u0579\u0000\u057A\u0000\u057B\u0000\u057C\u0000\u057D\u0000\u057E\u0000\u057F\u0000\u0580\u0000\u0581\u0000\u0582\u0000\u0583\u0000\u0584\u0000\u0585\u0000\u0586\u0000\u1E01\u0000\u1E03\u0000\u1E05\u0000\u1E07\u0000\u1E09\u0000\u1E0B\u0000\u1E0D\u0000\u1E0F\u0000\u1E11\u0000\u1E13\u0000\u1E15\u0000\u1E17\u0000\u1E19\u0000\u1E1B\u0000\u1E1D\u0000\u1E1F\u0000\u1E21\u0000\u1E23\u0000\u1E25\u0000\u1E27\u0000\u1E29\u0000\u1E2B\u0000\u1E2D\u0000\u1E2F\u0000\u1E31\u0000\u1E33\u0000\u1E35\u0000\u1E37\u0000\u1E39\u0000\u1E3B\u0000\u1E3D\u0000\u1E3F\u0000\u1E41\u0000\u1E43\u0000\u1E45\u0000\u1E47\u0000\u1E49\u0000\u1E4B\u0000\u1E4D\u0000\u1E4F\u0000\u1E51\u0000\u1E53\u0000\u1E55\u0000\u1E57\u0000\u1E59\u0000\u1E5B\u0000\u1E5D\u0000\u1E5F\u0000\u1E61\u0000\u1E63\u0000\u1E65\u0000\u1E67\u0000\u1E69\u0000\u1E6B\u0000\u1E6D\u0000\u1E6F\u0000\u1E71\u0000\u1E73\u0000\u1E75\u0000\u1E77\u0000\u1E79\u0000\u1E7B\u0000\u1E7D\u0000\u1E7F\u0000\u1E81\u0000\u1E83\u0000\u1E85\u0000\u1E87\u0000\u1E89\u0000\u1E8B\u0000\u1E8D\u0000\u1E8F\u0000\u1E91\u0000\u1E93\u0000\u1E95\u0000\u1E61\u0000\u1EA1\u0000\u1EA3\u0000\u1EA5\u0000\u1EA7\u0000\u1EA9\u0000\u1EAB\u0000\u1EAD\u0000\u1EAF\u0000\u1EB1\u0000\u1EB3\u0000\u1EB5\u0000\u1EB7\u0000\u1EB9\u0000\u1EBB\u0000\u1EBD\u0000\u1EBF\u0000\u1EC1\u0000\u1EC3\u0000\u1EC5\u0000\u1EC7\u0000\u1EC9\u0000\u1ECB\u0000\u1ECD\u0000\u1ECF\u0000\u1ED1\u0000\u1ED3\u0000\u1ED5\u0000\u1ED7\u0000\u1ED9\u0000\u1EDB\u0000\u1EDD\u0000\u1EDF\u0000\u1EE1\u0000\u1EE3\u0000\u1EE5\u0000\u1EE7\u0000\u1EE9\u0000\u1EEB\u0000\u1EED\u0000\u1EEF\u0000\u1EF1\u0000\u1EF3\u0000\u1EF5\u0000\u1EF7\u0000\u1EF9\u0000\u1F00\u0000\u1F01\u0000\u1F02\u0000\u1F03\u0000\u1F04\u0000\u1F05\u0000\u1F06\u0000\u1F07\u0000\u1F10\u0000\u1F11\u0000\u1F12\u0000\u1F13\u0000\u1F14\u0000\u1F15\u0000\u1F20\u0000\u1F21\u0000\u1F22\u0000\u1F23\u0000\u1F24\u0000\u1F25\u0000\u1F26\u0000\u1F27\u0000\u1F30\u0000\u1F31\u0000\u1F32\u0000\u1F33\u0000\u1F34\u0000\u1F35\u0000\u1F36\u0000\u1F37\u0000\u1F40\u0000\u1F41\u0000\u1F42\u0000\u1F43\u0000\u1F44\u0000\u1F45\u0000\u1F51\u0000\u1F53\u0000\u1F55\u0000\u1F57\u0000\u1F60\u0000\u1F61\u0000\u1F62\u0000\u1F63\u0000\u1F64\u0000\u1F65\u0000\u1F66\u0000\u1F67\u0000\u1F80\u0000\u1F81\u0000\u1F82\u0000\u1F83\u0000\u1F84\u0000\u1F85\u0000\u1F86\u0000\u1F87\u0000\u1F90\u0000\u1F91\u0000\u1F92\u0000\u1F93\u0000\u1F94\u0000\u1F95\u0000\u1F96\u0000\u1F97\u0000\u1FA0\u0000\u1FA1\u0000\u1FA2\u0000\u1FA3\u0000\u1FA4\u0000\u1FA5\u0000\u1FA6\u0000\u1FA7\u0000\u1FB0\u0000\u1FB1\u0000\u1F70\u0000\u1F71\u0000\u1FB3\u0000\u03B9\u0000\u1F72\u0000\u1F73\u0000\u1F74\u0000\u1F75\u0000\u1FC3\u0000\u1FD0\u0000\u1FD1\u0000\u1F76\u0000\u1F77\u0000\u1FE0\u0000\u1FE1\u0000\u1F7A\u0000\u1F7B\u0000\u1FE5\u0000\u1F78\u0000\u1F79\u0000\u1F7C\u0000\u1F7D\u0000\u1FF3\u0000\u03C9\u0000\u006B\u0000\u00E5\u0000\u2170\u0000\u2171\u0000\u2172\u0000\u2173\u0000\u2174\u0000\u2175\u0000\u2176\u0000\u2177\u0000\u2178\u0000\u2179\u0000\u217A\u0000\u217B\u0000\u217C\u0000\u217D\u0000\u217E\u0000\u217F\u0000\u24D0\u0000\u24D1\u0000\u24D2\u0000\u24D3\u0000\u24D4\u0000\u24D5\u0000\u24D6\u0000\u24D7\u0000\u24D8\u0000\u24D9\u0000\u24DA\u0000\u24DB\u0000\u24DC\u0000\u24DD\u0000\u24DE\u0000\u24DF\u0000\u24E0\u0000\u24E1\u0000\u24E2\u0000\u24E3\u0000\u24E4\u0000\u24E5\u0000\u24E6\u0000\u24E7\u0000\u24E8\u0000\u24E9\u0000\uFF41\u0000\uFF42\u0000\uFF43\u0000\uFF44\u0000\uFF45\u0000\uFF46\u0000\uFF47\u0000\uFF48\u0000\uFF49\u0000\uFF4A\u0000\uFF4B\u0000\uFF4C\u0000\uFF4D\u0000\uFF4E\u0000\uFF4F\u0000\uFF50\u0000\uFF51\u0000\uFF52\u0000\uFF53\u0000\uFF54\u0000\uFF55\u0000\uFF56\u0000\uFF57\u0000\uFF58\u0000\uFF59\u0000\uFF5A"
        val UPPER = "\u0000\u0049\u0000\u00B5\u0000\u0100\u0000\u0102\u0000\u0104\u0000\u0106\u0000\u0108\u0000\u010A\u0000\u010C\u0000\u010E\u0000\u0110\u0000\u0112\u0000\u0114\u0000\u0116\u0000\u0118\u0000\u011A\u0000\u011C\u0000\u011E\u0000\u0120\u0000\u0122\u0000\u0124\u0000\u0126\u0000\u0128\u0000\u012A\u0000\u012C\u0000\u012E\u0000\u0130\u0000\u0132\u0000\u0134\u0000\u0136\u0000\u0139\u0000\u013B\u0000\u013D\u0000\u013F\u0000\u0141\u0000\u0143\u0000\u0145\u0000\u0147\u0000\u014A\u0000\u014C\u0000\u014E\u0000\u0150\u0000\u0152\u0000\u0154\u0000\u0156\u0000\u0158\u0000\u015A\u0000\u015C\u0000\u015E\u0000\u0160\u0000\u0162\u0000\u0164\u0000\u0166\u0000\u0168\u0000\u016A\u0000\u016C\u0000\u016E\u0000\u0170\u0000\u0172\u0000\u0174\u0000\u0176\u0000\u0178\u0000\u0179\u0000\u017B\u0000\u017D\u0000\u017F\u0000\u0181\u0000\u0182\u0000\u0184\u0000\u0186\u0000\u0187\u0000\u0189\u0000\u018A\u0000\u018B\u0000\u018E\u0000\u018F\u0000\u0190\u0000\u0191\u0000\u0193\u0000\u0194\u0000\u0196\u0000\u0197\u0000\u0198\u0000\u019C\u0000\u019D\u0000\u019F\u0000\u01A0\u0000\u01A2\u0000\u01A4\u0000\u01A6\u0000\u01A7\u0000\u01A9\u0000\u01AC\u0000\u01AE\u0000\u01AF\u0000\u01B1\u0000\u01B2\u0000\u01B3\u0000\u01B5\u0000\u01B7\u0000\u01B8\u0000\u01BC\u0000\u01C4\u0000\u01C5\u0000\u01C7\u0000\u01C8\u0000\u01CA\u0000\u01CB\u0000\u01CD\u0000\u01CF\u0000\u01D1\u0000\u01D3\u0000\u01D5\u0000\u01D7\u0000\u01D9\u0000\u01DB\u0000\u01DE\u0000\u01E0\u0000\u01E2\u0000\u01E4\u0000\u01E6\u0000\u01E8\u0000\u01EA\u0000\u01EC\u0000\u01EE\u0000\u01F1\u0000\u01F2\u0000\u01F4\u0000\u01F6\u0000\u01F7\u0000\u01F8\u0000\u01FA\u0000\u01FC\u0000\u01FE\u0000\u0200\u0000\u0202\u0000\u0204\u0000\u0206\u0000\u0208\u0000\u020A\u0000\u020C\u0000\u020E\u0000\u0210\u0000\u0212\u0000\u0214\u0000\u0216\u0000\u0218\u0000\u021A\u0000\u021C\u0000\u021E\u0000\u0220\u0000\u0222\u0000\u0224\u0000\u0226\u0000\u0228\u0000\u022A\u0000\u022C\u0000\u022E\u0000\u0230\u0000\u0232\u0000\u0345\u0000\u0386\u0000\u0388\u0000\u0389\u0000\u038A\u0000\u038C\u0000\u038E\u0000\u038F\u0000\u0391\u0000\u0392\u0000\u0393\u0000\u0394\u0000\u0395\u0000\u0396\u0000\u0397\u0000\u0398\u0000\u0399\u0000\u039A\u0000\u039B\u0000\u039C\u0000\u039D\u0000\u039E\u0000\u039F\u0000\u03A0\u0000\u03A1\u0000\u03A3\u0000\u03A4\u0000\u03A5\u0000\u03A6\u0000\u03A7\u0000\u03A8\u0000\u03A9\u0000\u03AA\u0000\u03AB\u0000\u03C2\u0000\u03D0\u0000\u03D1\u0000\u03D5\u0000\u03D6\u0000\u03D8\u0000\u03DA\u0000\u03DC\u0000\u03DE\u0000\u03E0\u0000\u03E2\u0000\u03E4\u0000\u03E6\u0000\u03E8\u0000\u03EA\u0000\u03EC\u0000\u03EE\u0000\u03F0\u0000\u03F1\u0000\u03F4\u0000\u03F5\u0000\u03F7\u0000\u03F9\u0000\u03FA\u0000\u0400\u0000\u0401\u0000\u0402\u0000\u0403\u0000\u0404\u0000\u0405\u0000\u0406\u0000\u0407\u0000\u0408\u0000\u0409\u0000\u040A\u0000\u040B\u0000\u040C\u0000\u040D\u0000\u040E\u0000\u040F\u0000\u0410\u0000\u0411\u0000\u0412\u0000\u0413\u0000\u0414\u0000\u0415\u0000\u0416\u0000\u0417\u0000\u0418\u0000\u0419\u0000\u041A\u0000\u041B\u0000\u041C\u0000\u041D\u0000\u041E\u0000\u041F\u0000\u0420\u0000\u0421\u0000\u0422\u0000\u0423\u0000\u0424\u0000\u0425\u0000\u0426\u0000\u0427\u0000\u0428\u0000\u0429\u0000\u042A\u0000\u042B\u0000\u042C\u0000\u042D\u0000\u042E\u0000\u042F\u0000\u0460\u0000\u0462\u0000\u0464\u0000\u0466\u0000\u0468\u0000\u046A\u0000\u046C\u0000\u046E\u0000\u0470\u0000\u0472\u0000\u0474\u0000\u0476\u0000\u0478\u0000\u047A\u0000\u047C\u0000\u047E\u0000\u0480\u0000\u048A\u0000\u048C\u0000\u048E\u0000\u0490\u0000\u0492\u0000\u0494\u0000\u0496\u0000\u0498\u0000\u049A\u0000\u049C\u0000\u049E\u0000\u04A0\u0000\u04A2\u0000\u04A4\u0000\u04A6\u0000\u04A8\u0000\u04AA\u0000\u04AC\u0000\u04AE\u0000\u04B0\u0000\u04B2\u0000\u04B4\u0000\u04B6\u0000\u04B8\u0000\u04BA\u0000\u04BC\u0000\u04BE\u0000\u04C1\u0000\u04C3\u0000\u04C5\u0000\u04C7\u0000\u04C9\u0000\u04CB\u0000\u04CD\u0000\u04D0\u0000\u04D2\u0000\u04D4\u0000\u04D6\u0000\u04D8\u0000\u04DA\u0000\u04DC\u0000\u04DE\u0000\u04E0\u0000\u04E2\u0000\u04E4\u0000\u04E6\u0000\u04E8\u0000\u04EA\u0000\u04EC\u0000\u04EE\u0000\u04F0\u0000\u04F2\u0000\u04F4\u0000\u04F8\u0000\u0500\u0000\u0502\u0000\u0504\u0000\u0506\u0000\u0508\u0000\u050A\u0000\u050C\u0000\u050E\u0000\u0531\u0000\u0532\u0000\u0533\u0000\u0534\u0000\u0535\u0000\u0536\u0000\u0537\u0000\u0538\u0000\u0539\u0000\u053A\u0000\u053B\u0000\u053C\u0000\u053D\u0000\u053E\u0000\u053F\u0000\u0540\u0000\u0541\u0000\u0542\u0000\u0543\u0000\u0544\u0000\u0545\u0000\u0546\u0000\u0547\u0000\u0548\u0000\u0549\u0000\u054A\u0000\u054B\u0000\u054C\u0000\u054D\u0000\u054E\u0000\u054F\u0000\u0550\u0000\u0551\u0000\u0552\u0000\u0553\u0000\u0554\u0000\u0555\u0000\u0556\u0000\u1E00\u0000\u1E02\u0000\u1E04\u0000\u1E06\u0000\u1E08\u0000\u1E0A\u0000\u1E0C\u0000\u1E0E\u0000\u1E10\u0000\u1E12\u0000\u1E14\u0000\u1E16\u0000\u1E18\u0000\u1E1A\u0000\u1E1C\u0000\u1E1E\u0000\u1E20\u0000\u1E22\u0000\u1E24\u0000\u1E26\u0000\u1E28\u0000\u1E2A\u0000\u1E2C\u0000\u1E2E\u0000\u1E30\u0000\u1E32\u0000\u1E34\u0000\u1E36\u0000\u1E38\u0000\u1E3A\u0000\u1E3C\u0000\u1E3E\u0000\u1E40\u0000\u1E42\u0000\u1E44\u0000\u1E46\u0000\u1E48\u0000\u1E4A\u0000\u1E4C\u0000\u1E4E\u0000\u1E50\u0000\u1E52\u0000\u1E54\u0000\u1E56\u0000\u1E58\u0000\u1E5A\u0000\u1E5C\u0000\u1E5E\u0000\u1E60\u0000\u1E62\u0000\u1E64\u0000\u1E66\u0000\u1E68\u0000\u1E6A\u0000\u1E6C\u0000\u1E6E\u0000\u1E70\u0000\u1E72\u0000\u1E74\u0000\u1E76\u0000\u1E78\u0000\u1E7A\u0000\u1E7C\u0000\u1E7E\u0000\u1E80\u0000\u1E82\u0000\u1E84\u0000\u1E86\u0000\u1E88\u0000\u1E8A\u0000\u1E8C\u0000\u1E8E\u0000\u1E90\u0000\u1E92\u0000\u1E94\u0000\u1E9B\u0000\u1EA0\u0000\u1EA2\u0000\u1EA4\u0000\u1EA6\u0000\u1EA8\u0000\u1EAA\u0000\u1EAC\u0000\u1EAE\u0000\u1EB0\u0000\u1EB2\u0000\u1EB4\u0000\u1EB6\u0000\u1EB8\u0000\u1EBA\u0000\u1EBC\u0000\u1EBE\u0000\u1EC0\u0000\u1EC2\u0000\u1EC4\u0000\u1EC6\u0000\u1EC8\u0000\u1ECA\u0000\u1ECC\u0000\u1ECE\u0000\u1ED0\u0000\u1ED2\u0000\u1ED4\u0000\u1ED6\u0000\u1ED8\u0000\u1EDA\u0000\u1EDC\u0000\u1EDE\u0000\u1EE0\u0000\u1EE2\u0000\u1EE4\u0000\u1EE6\u0000\u1EE8\u0000\u1EEA\u0000\u1EEC\u0000\u1EEE\u0000\u1EF0\u0000\u1EF2\u0000\u1EF4\u0000\u1EF6\u0000\u1EF8\u0000\u1F08\u0000\u1F09\u0000\u1F0A\u0000\u1F0B\u0000\u1F0C\u0000\u1F0D\u0000\u1F0E\u0000\u1F0F\u0000\u1F18\u0000\u1F19\u0000\u1F1A\u0000\u1F1B\u0000\u1F1C\u0000\u1F1D\u0000\u1F28\u0000\u1F29\u0000\u1F2A\u0000\u1F2B\u0000\u1F2C\u0000\u1F2D\u0000\u1F2E\u0000\u1F2F\u0000\u1F38\u0000\u1F39\u0000\u1F3A\u0000\u1F3B\u0000\u1F3C\u0000\u1F3D\u0000\u1F3E\u0000\u1F3F\u0000\u1F48\u0000\u1F49\u0000\u1F4A\u0000\u1F4B\u0000\u1F4C\u0000\u1F4D\u0000\u1F59\u0000\u1F5B\u0000\u1F5D\u0000\u1F5F\u0000\u1F68\u0000\u1F69\u0000\u1F6A\u0000\u1F6B\u0000\u1F6C\u0000\u1F6D\u0000\u1F6E\u0000\u1F6F\u0000\u1F88\u0000\u1F89\u0000\u1F8A\u0000\u1F8B\u0000\u1F8C\u0000\u1F8D\u0000\u1F8E\u0000\u1F8F\u0000\u1F98\u0000\u1F99\u0000\u1F9A\u0000\u1F9B\u0000\u1F9C\u0000\u1F9D\u0000\u1F9E\u0000\u1F9F\u0000\u1FA8\u0000\u1FA9\u0000\u1FAA\u0000\u1FAB\u0000\u1FAC\u0000\u1FAD\u0000\u1FAE\u0000\u1FAF\u0000\u1FB8\u0000\u1FB9\u0000\u1FBA\u0000\u1FBB\u0000\u1FBC\u0000\u1FBE\u0000\u1FC8\u0000\u1FC9\u0000\u1FCA\u0000\u1FCB\u0000\u1FCC\u0000\u1FD8\u0000\u1FD9\u0000\u1FDA\u0000\u1FDB\u0000\u1FE8\u0000\u1FE9\u0000\u1FEA\u0000\u1FEB\u0000\u1FEC\u0000\u1FF8\u0000\u1FF9\u0000\u1FFA\u0000\u1FFB\u0000\u1FFC\u0000\u2126\u0000\u212A\u0000\u212B\u0000\u2160\u0000\u2161\u0000\u2162\u0000\u2163\u0000\u2164\u0000\u2165\u0000\u2166\u0000\u2167\u0000\u2168\u0000\u2169\u0000\u216A\u0000\u216B\u0000\u216C\u0000\u216D\u0000\u216E\u0000\u216F\u0000\u24B6\u0000\u24B7\u0000\u24B8\u0000\u24B9\u0000\u24BA\u0000\u24BB\u0000\u24BC\u0000\u24BD\u0000\u24BE\u0000\u24BF\u0000\u24C0\u0000\u24C1\u0000\u24C2\u0000\u24C3\u0000\u24C4\u0000\u24C5\u0000\u24C6\u0000\u24C7\u0000\u24C8\u0000\u24C9\u0000\u24CA\u0000\u24CB\u0000\u24CC\u0000\u24CD\u0000\u24CE\u0000\u24CF\u0000\uFF21\u0000\uFF22\u0000\uFF23\u0000\uFF24\u0000\uFF25\u0000\uFF26\u0000\uFF27\u0000\uFF28\u0000\uFF29\u0000\uFF2A\u0000\uFF2B\u0000\uFF2C\u0000\uFF2D\u0000\uFF2E\u0000\uFF2F\u0000\uFF30\u0000\uFF31\u0000\uFF32\u0000\uFF33\u0000\uFF34\u0000\uFF35\u0000\uFF36\u0000\uFF37\u0000\uFF38\u0000\uFF39\u0000\uFF3A"
        val zeros = "00000000000000000000".toCharArray()
        val LN10 = 2.30258509299404568402;
        val p1 = doubleArrayOf(1.0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9, 1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19, 1e20, 1e21, 1e22, 1e23, 1e24, 1e25, 1e26, 1e27, 1e28, 1e29, 1e30, 1e31)

        val p32 = doubleArrayOf(1.0, 1e32, 1e64, 1e96, 1e128, 1e160, 1e192, 1e224, 1e256, 1e288)
        val np32 = doubleArrayOf(1e-0, 1e-32, 1e-64, 1e-96, 1e-128, 1e-160, 1e-192, 1e-224, 1e-256, 1e-288, 1e-320)

        val rounds5 = doubleArrayOf(5e-1, 5e-2, 5e-3, 5e-4, 5e-5, 5e-6, 5e-7, 5e-8, 5e-9, 5e-10, 5e-11, 5e-12, 5e-13, 5e-14, 5e-15, 5e-16, 5e-17, 5e-18)


        val dest = StringBuffer(25)
        /**
         * Método que arredonda um número double com n casas decimais
         *
         */
        fun round(value: Double, decimalPlace: Int): Double {
            var decimalPlace = decimalPlace
            var power_of_ten = 1.0
            while (decimalPlace-- > 0)
                power_of_ten *= 10.0

            return Math.round(value * power_of_ten) / power_of_ten
        }

        /**
         * Método que realiza a comparação entre dois valores double
         *
         * @param x -
         * Número Double.
         * @param y -
         * Número Double.
         * @return Inteiro representado o resultado da comparação. 0 - igual 1 - maior -1 - menor
         *
         */
        fun compare(x: Double, y: Double): Int {

            var i = 0 // Iguais
            val result = x - y

            if (Math.abs(result) > 0.000001) {

                if (result > 0) {
                    i = 1
                } else {
                    i = -1
                }
            }

            return i
        }

        /**
         * Método que trunca um número double com n casas decimais, utilizado para
         * truncar os valores. que estão no banco de dados (não calculados)
         *
         * @param dNumber -
         * Número Double.
         * @param iCasas -
         * Quantidade de casas decimais.
         * @return Double representando o valor truncado.
         */
        fun truncN(dNumber: Double, iCasas: Int): Double {

            var iachou: Int
            var sRetorno: String
            val sNumber = toString(dNumber, 10)

            if (iCasas < 0) {
                return dNumber
            }

            iachou = sNumber.indexOf(".")

            if (iachou <= 0) {
                iachou = sNumber.indexOf(",")
                if (iachou <= 0) {
                    var `val` = 0.0

                    try {
                        `val` = sNumber.toDouble()
                    } catch (e: Exception) {
                    }

                    return `val`
                }
            }

            if (sNumber.length >= iachou + iCasas + 1) {
                if (iCasas == 0) {
                    sRetorno = sNumber.substring(0, iachou)
                } else {
                    sRetorno = sNumber.substring(0, iachou + 1)
                    sRetorno = sRetorno + sNumber.substring(iachou + 1, iachou + 1 + iCasas) // Mid$(snumber,
                    // iachou
                    // + 1,
                    // iCasas);
                }
            } else {
                sRetorno = sNumber
            }

            var `val` = 0.0

            try {
                `val` = sRetorno.toDouble()
            } catch (e: Exception) {
            }

            return `val`

        }

        /** Converts the given double to a String, formatting it with the given number of decimal places.
         * @since SuperWaba 2.0
         */
        fun toString(`val`: Double, decimalCount: Int): String {
            var `val` = `val`

            var decimalCount = decimalCount
            if (decimalCount < -1)
                throw IllegalArgumentException("Invalid value '$decimalCount' for argument '$decimalCount'") // guich@tc123_9
            val dest = StringBuffer(25)

            //System.out.print(val+", "+decimalCount);
            val bits = java.lang.Double.doubleToLongBits(`val`)
            if (`val` == 0.0) {
                if (decimalCount == -1)
                    dest.append("0.0")
                else {
                    dest.append('0')
                    if (decimalCount > 0)
                        dest.append('.').append(zeros, 0, decimalCount)
                }
            } else if (bits == DOUBLE_NAN_BITS)
                dest.append("NaN")
            else if (bits == DOUBLE_POSITIVE_INFINITY_BITS || bits == DOUBLE_NEGATIVE_INFINITY_BITS)
                dest.append(if (`val` < 0) '-' else '+').append("Inf")
            else {
                // example: -1000.5432
                var integral: Long
                var fract: Long = 0
                var exponent: Int
                val floating = decimalCount < 0
                if (floating)
                    decimalCount = MAX_DOUBLE_DIGITS
                if (`val` < 0) {
                    `val` = -`val` // 1000.5432
                    dest.append('-')
                }

                exponent = (Math.log(`val`) / LN10).toInt()  // 3 : 1000.5432 = 1.0005432*10^3
                if (DOUBLE_MIN_NON_EXP <= `val` && `val` <= DOUBLE_MAX_NON_EXP)
                // does it fit without sci notation?
                {
                    if (decimalCount == 0)
                        `val` += 0.5
                    integral = `val`.toLong() // 1000
                    exponent = 0
                } else {
                    var adjusted = false
                    while (true)

                    {
                        // pow10: fast pow10
                        val pow10 = if (exponent >= 0) p1[exponent and 31] * p32[exponent shr 5] else np32[-exponent shr 5] / p1[-exponent and 31] // guich@570_51: when exp >= 320, p32 index is 10, and 1e320 is impossible
                        var mant = `val` / pow10
                        if (decimalCount < 18)
                            mant += rounds5[decimalCount] as Double
                        integral = mant.toLong()
                        if (integral == 0L && !adjusted) {
                            adjusted = true
                            exponent-- // 0.12345 ?
                        } else if (integral >= 10 && !adjusted) {
                            adjusted = true
                            exponent++ // 10.12345 ?
                        } else {
                            `val` = mant
                            break
                        }
                    }
                }
                if (decimalCount == 0)
                    dest.append(integral)
                else {
                    var i: Int
                    var firstNonZero = -1 // number of zeros between . and first non-zero
                    val pow10 = if (decimalCount >= 0) p1[decimalCount and 31] * p32[decimalCount shr 5] else np32[-decimalCount shr 5] / p1[-decimalCount and 31] // guich@570_51: when exp >= 320, p32 index is 10, and 1e320 is impossible
                    var ipow10 = pow10.toLong()
                    val f = `val` - integral // 1000.5432-1000 = 0.5432
                    if (f > 1.0e-16) {
                        var values: Double
                        if (exponent == 0){
                            values = 0.5
                        } else {
                            values = 0.0
                        }
                        fract =  (f * pow10 + (values)).toLong()
                        if (fract == ipow10)
                        // case of Convert.toString(49.999,2)
                        {
                            fract = 0
                            integral++
                        }
                    }
                    dest.append(integral)

                    do {
                        ipow10 /= 10
                        firstNonZero++
                    } while (ipow10 > fract)
                    val s = java.lang.Long.toString(fract)
                    i = decimalCount - s.length
                    dest.append('.')
                    if (0 < firstNonZero && firstNonZero < decimalCount) {
                        i -= firstNonZero
                        dest.append(zeros, 0, firstNonZero)
                    }
                    dest.append(s)
                    if (floating)
                        while (dest[dest.length - 2] != '.' && dest[dest.length - 1] == '0')
                            dest.setLength(dest.length - 1)
                    else if (i > 0)
                    // fill with zeros if needed
                        dest.append(zeros, 0, if (i < 20) i else 20) // this should not respect the maximum allowed width, because its just for user formatting
                }

                if (exponent != 0)
                    dest.append('E').append(exponent)
            }
            var ret = dest.toString()
            val l = ret.length
            if (l > 0 && ret[0] == '-')
            // guich@tc200b5: check if its -0.00... and change to 0.00...
            {
                var only0 = true
                var i = 1
                while (i < l && only0) {
                    val c = ret[i]
                    only0 = only0 and (c == '.' || c == '0')
                    i++
                }
                if (only0)
                    ret = ret.substring(1) // remove the -
            }
            //System.out.println(" -> "+dest);
            return ret
        }

        /** Pads the string with zeroes at left.  */
        fun zeroPad(s: String?, size: Int): String {
            if (s == null)
                throw java.lang.NullPointerException("Argument 's' cannot have a null value")
            val n = size - s.length
            if (n > 0) {
                if (n >= zerosp.length) {
                    // more zeros than we already have?
                    var  zerosp = dup('0', n + 1)
                    return zerosp.substring(0, n) + (s)
                }
            }
            return s
        }

        /** Converts the int to String and pads it with zeroes at left.
         * @since TotalCross 1.15
         * @see .zeroPad
         */
        fun zeroPad(s: Int, size: Int) // guich@tc115_22
                : String {
            return zeroPadS(s.toString(), size)
        }

        /** Pads the string with zeroes at left.  */
        fun zeroPadS(s: String?, size: Int): String {
            if (s == null)
                throw java.lang.NullPointerException("Argument 's' cannot have a null value")
            val n = size - s.length
            if (n > 0) {
                if (n >= zerosp.length)  {
                    // more zeros than we already have?
                    val zerosp = dup('0', n + 1)
                    return zerosp.substring(0, n) + s
                }
            }
            return s
        }

        fun dup(c: Char, count: Int) // guich@572_17
                : String {
            if (count < 0)
                throw IllegalArgumentException("count can't be below 0: $count")
            val buf = CharArray(count)
            fill(buf, 0, count, c)
            return String(buf)
        }


        /** Fills the given array, within the range, with the value specified. The array is filled as from &lt;= n &lt; to.  */
        fun fill(a: CharArray, from: Int, to: Int, value: Char) {
            var from = from
            try {
                java.util.Arrays.fill(a, from, to, value)
            } catch (t: Throwable) // jdk 1.x
            {
                while (from < to) {
                    a[from] = value
                    from++
                }
            }

        }


        fun formatNumber(nValor: Double, sTamanho: Int,
                         sCasasDecimais: Int, sTrim: String): String {

            val sValor: String
            val sDecimal: String
            val sNumero: String
            var i: Int
            val resto: Int
            val dividendo: Int
            var inicial = 0
            var x = ""
            val xTamanho: Int

            sValor = zeroPad(toString(nValor, sCasasDecimais),
                    sTamanho + sCasasDecimais + 1) 
            if (sCasasDecimais == 0) {
                sDecimal = ""
            } else {
                sDecimal = "," + sValor.substring(sTamanho + 1, sTamanho + sCasasDecimais
                        + 1)
            }

            sNumero = zeroPad(sValor.substring(0,
                    sValor.length - sDecimal.length).trim { it <= ' ' }, sTamanho)

            dividendo = sTamanho / 3

            resto = sTamanho % 3

            i = 0
            while (i <= dividendo) {

                if (i == 0 && resto > 0) {

                    x = sNumero.substring(0, inicial + resto) + "."

                    inicial = resto + 1
                } else if (i == 0 && resto == 0) {

                    inicial = 1
                } else if (i == dividendo) {

                    x = x + sNumero.substring(inicial - 1, inicial + 2)
                } else {

                    x = x + sNumero.substring(inicial - 1, inicial + 2) + "."
                    inicial = inicial + 3
                }
                i++

            }

            xTamanho = x.length

            i = 1
            while (i <= xTamanho - 1) {

                if (x.substring(i - 1, i) == "0" || x.substring(i - 1, i) == ".")
                    x = (x.substring(0, i - 1) + " "
                            + x.substring(i, i + xTamanho - i))
                else
                    break
                i++
            }

            if (sTrim == "S")
                x = x.trim { it <= ' ' } + sDecimal
            else
                x = x + sDecimal

            return x

        }

    }

}