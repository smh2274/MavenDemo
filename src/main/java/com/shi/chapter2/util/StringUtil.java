package com.shi.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bran-Shi on 5/19/2017.
 * 字符串工具类
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
