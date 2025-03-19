package com.deqi.firstlab.connection;

import java.sql.*;

public class DatabaseConnectionFactory {
    public static Connection getConnection(String connectionType) throws SQLException {
        if ("JDBC".equals(connectionType)) {
            return new JDBCConnection().createConnection();
        } else if ("ODBC".equals(connectionType)) {
            return new ODBCConnection().createConnection();
        } else {
            throw new IllegalArgumentException("Invalid connection type: " + connectionType);
        }
    }

    // Nested class for JDBC connection handling
    private static class JDBCConnection {
        public Connection createConnection() throws SQLException {
            String dbURL = "jdbc:postgresql://localhost/mydatabase";
            String user = "testuser";
            String password = "12345";
            return DriverManager.getConnection(dbURL, user, password);
        }
    }

    // Nested class for ODBC connection handling
    private static class ODBCConnection {
        public Connection createConnection() throws SQLException {
            String dsn = "PostgreSQL_DSN";
            String user = "testuser";
            String password = "12345";
            String url = "jdbc:odbc:" + dsn;

            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // Java's ODBC driver class
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC-ODBC Driver not found", e);
            }

            return DriverManager.getConnection(url, user, password);
        }
    }
}
