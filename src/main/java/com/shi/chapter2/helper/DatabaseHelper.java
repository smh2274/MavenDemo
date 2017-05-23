package com.shi.chapter2.helper;


import com.shi.chapter2.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
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

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL=new ThreadLocal<Connection>();

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
        if(conn==null) {
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
         Connection conn=CONNECTION_THREAD_LOCAL.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                //e.printStackTrace();
                LOGGER.error("lose sql connection failure");
            }finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 查询实体列表,
     */
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object...params) {
        List<T> entityList;
        try {
            Connection conn=getconnect();
            //noinspection deprecation
            entityList = QUERY_RUNNER.query(conn, sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnect();
        }
        return entityList;
    }

}
