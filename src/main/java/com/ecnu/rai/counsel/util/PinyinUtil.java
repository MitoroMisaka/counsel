package com.ecnu.rai.counsel.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinUtil {

    public static String convertToPinyin(String hanzi) {
        String result = null;
        if(null != hanzi && !"".equals(hanzi)) {
            char[] charArray = hanzi.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (char ch : charArray) {
                // 逐个汉字进行转换， 每个汉字返回值为一个String数组（因为有多音字）
                String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(ch);
                if(null != stringArray) {
                    // 把第几声这个数字给去掉
                    sb.append(stringArray[0].replaceAll("\\d", ""));
                }
            }
            if(sb.length() > 0) {
                result = sb.toString();
            }
        }
        return result;
    }
}
