package com.deqi.firstlab.connection;

import java.sql.*;

public class DatabaseConnectionFactory {
    public static Connection getConnection(String dbType) throws SQLException {
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return getPostgresConnection();
        } else if ("MySQL".equalsIgnoreCase(dbType)) {
            return getMySQLConnection();
        } else {
            throw new IllegalArgumentException("Unsupported DB type: " + dbType);
        }
    }

    private static Connection getPostgresConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5433/myapp_db?currentSchema=my_schema";
        String user = "postgres";
        String password = "12345password";
        return DriverManager.getConnection(url, user, password);
    }

    private static Connection getMySQLConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/myapp_db";
        String user = "root";
        String password = "12345password";
        return DriverManager.getConnection(url, user, password);
    }
}
