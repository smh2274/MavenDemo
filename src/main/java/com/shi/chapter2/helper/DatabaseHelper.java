package com.shi.chapter2.helper;


import com.shi.chapter2.util.CollectionUtil;
import com.shi.chapter2.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Bran-Shi on 5/21/2017.
 * 数据库操作助手
 */
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

    static {
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load mysql driver", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getconnect() {
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("sql connect failure");
            } finally {
                CONNECTION_THREAD_LOCAL.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnect() {
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                //e.printStackTrace();
                LOGGER.error("lose sql connection failure");
            } finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 查询实体列表,
     */
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getconnect();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnect();
        }
        return entityList;
    }

    /**
     * 查询实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getconnect();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnect();
        }
        return entity;
    }

    /**
     *执行查询语句
     */
    public static List<Map<String,Object>> executeQuery(String sql,Object...params){
        List<Map<String,Object>> result;
        Connection conn=getconnect();
        try {
            result=QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            //e.printStackTrace();
            LOGGER.error("execute query failure",e);
            throw new RuntimeException(e);
        }finally {
            closeConnect();
        }
        return result;
    }

    /**
     *执行更新语句
     */
    public static int executeUpdate(String sql,Object...params){
        int rows=0;
        try {
            Connection conn = getconnect();
            rows = QUERY_RUNNER.update(conn, sql, params);
        }catch (SQLException e){
            LOGGER.error("execute query failure",e);
            throw new RuntimeException(e);
        }finally {
            closeConnect();
        }
        return rows;
    }

    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("Can not insert entity:fieldMap is empty");
            return false;
        }

        String sql="INSERT INTO "+getTableName(entityClass);
        StringBuilder columns=new StringBuilder("(");
        StringBuilder values=new StringBuilder("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql+=columns+"VALUES"+values;

        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;

    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> feildMap){
        if(CollectionUtil.isEmpty(feildMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }

        String sql="UPDATE "+getTableName(entityClass)+"SET";
        StringBuilder columns=new StringBuilder();
        for(String feildName:feildMap.keySet()){
            columns.append(feildName).append("=? ");
        }
        sql+=columns.substring(0,columns.lastIndexOf(","))+"WHERE id=?";

        List<Object> paramList=new ArrayList<Object>();
        paramList.addAll(feildMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();

        return executeUpdate(sql,params)==1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM "+getTableName(entityClass)+"WHERE id=?";
        return executeUpdate(sql,id)==1;
    }
}
