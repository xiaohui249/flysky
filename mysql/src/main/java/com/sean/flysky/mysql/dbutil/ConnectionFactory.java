package com.sean.flysky.mysql.dbutil;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工厂类
 * Created by sean on 2016/6/24.
 */
public class ConnectionFactory {
    private final static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    private final static ThreadLocal<Connection> connInCurrThread = new ThreadLocal<>();

    private static DataSource dataSource = null;

    static {
       initDataSource();
    }

    private static void initDataSource() {
        Properties prop = new Properties();
        try {
            InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            if(in == null) {
                logger.error("未加载到DBCP配置文件！");
            } else {
                prop.load(in);
                if (!prop.isEmpty()) {
                    dataSource = BasicDataSourceFactory.createDataSource(prop);
                }
            }
        } catch (IOException ioe) {
            logger.error("DBCP配置加载失败！", ioe);
        } catch (Exception e) {
            logger.error("数据池创建失败！", e);
        }

        if(dataSource == null) {
            System.exit(1);
        }
    }

    public final static Connection getConnection() {
        Connection conn = connInCurrThread.get();
        try {
            if (conn == null || conn.isClosed()) {
                conn = dataSource.getConnection();
                connInCurrThread.set(conn);
            }
            return conn;
        }  catch (SQLException e) {
            logger.error("Unable to get connection!!!", e);
            return null;
        }
    }

    public final static void closeConnection() {
        Connection conn = connInCurrThread.get();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("Unable to close connection!!! ", e);
        }
        connInCurrThread.set(null);
    }

    public final static DataSource getDataSource() {
        return dataSource;
    }
}
