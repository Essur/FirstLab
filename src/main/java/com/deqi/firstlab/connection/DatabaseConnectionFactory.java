package com.deqi.firstlab.connection;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;

public class DatabaseConnectionFactory {
    public static Connection getConnection(String connectionType) throws SQLException {
        if ("JDBC".equals(connectionType)) {
            return new JDBCConnection().createConnection();
        } else if ("DBCP2".equals(connectionType)) {
            return new DataSourceConnection().createConnection();
        } else {
            throw new IllegalArgumentException("Invalid connection type: " + connectionType);
        }
    }

    private static class JDBCConnection {
        public Connection createConnection() throws SQLException {
            String dbURL = "jdbc:postgresql://localhost/myapp_db";
            String user = "postgres";
            String password = "12345password";
            return DriverManager.getConnection(dbURL, user, password);
        }
    }

    private static class DataSourceConnection {
        public Connection createConnection() throws SQLException {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost/myapp_db");
            dataSource.setUsername("postgres");
            dataSource.setPassword("12345password");
            dataSource.setMaxTotal(10);
            return dataSource.getConnection();
        }
    }
}
