package com.daxton.unrealchest.application;

import java.util.List;
import java.util.regex.Pattern;

public class ListUtil {

    //列表 正表達比對 轉為列表名稱
    public static String toPatternName(List<String> list, String text) {
        for (String regex : list) {
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(text).matches()) {
                return regex;
            }
        }
        return text;
    }

}
