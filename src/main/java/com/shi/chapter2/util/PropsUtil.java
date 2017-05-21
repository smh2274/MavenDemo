package com.shi.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Bran-Shi on 5/19/2017.
 * 属性文件工具类
 */
public final class PropsUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        }finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error("lose input stream failure",e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性
     */
    public static String getString(Properties props,String key){
        return getString(props, key,"");
    }

    /**
     * 获取字符型属性（默认）
     */
    public static String getString(Properties props,String key,String defaultValue){
        String value=defaultValue;
        if (props.containsKey(key)){
            value=props.getProperty(key);
        }
        return  value;
    }

    /**
     * 获取整数型属性
     */
    public static int getInt(Properties props,String key){
        return getInt(props, key);
    }

    /**
     * 获取整数型属性（默认）
     */
    public static int getInt(Properties props,String key,int defaultValue){
        int value=defaultValue;
        if(props.containsKey(key)){
            value=CastUtil.castInt(props.getProperty(key));
        }
        return  value;
    }

    /**
     * 获取布尔型属性
     */
    public static boolean getBoolean(Properties props, String key){
        return getBoolean(props, key);
    }

    /**
     * 获取布尔属性（默认）
     */
    public static  boolean getBoolean(Properties props,String key,Boolean defaultValues){
        Boolean value=defaultValues;
        if(props.containsKey(key)){
            value=CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}
