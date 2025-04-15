package com.deqi.firstlab.service;

import com.deqi.firstlab.connection.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DistributedTransactionService {

    public static void executeDistributedInsert(String username, String email, String password) throws SQLException {
        Connection pgConn = null;
        Connection mySqlConn = null;

        try {
            pgConn = DatabaseConnectionFactory.getConnection("PostgreSQL");
            mySqlConn = DatabaseConnectionFactory.getConnection("MySQL");

            pgConn.setAutoCommit(false);
            mySqlConn.setAutoCommit(false);

            // Вставка пользователя в PostgreSQL
            String insertUserPg = "INSERT INTO my_schema.users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement pgStmt = pgConn.prepareStatement(insertUserPg);
            pgStmt.setString(1, username + "_pg");
            pgStmt.setString(2, email + "pg.com");
            pgStmt.setString(3, password + "_pg");
            pgStmt.executeUpdate();

            // Вставка пользователя в MySQL
            String insertUserMy = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement myStmt = mySqlConn.prepareStatement(insertUserMy);
            myStmt.setString(1, username + "_mysql");
            myStmt.setString(2, email + "mysql.com");
            myStmt.setString(3, password + "_mysql");
            myStmt.executeUpdate();

            // Коммитим обе транзакции
            pgConn.commit();
            mySqlConn.commit();

        } catch (SQLException e) {
            if (pgConn != null) pgConn.rollback();
            if (mySqlConn != null) mySqlConn.rollback();
            throw e;
        } finally {
            if (pgConn != null) pgConn.setAutoCommit(true);
            if (mySqlConn != null) mySqlConn.setAutoCommit(true);
            if (pgConn != null) pgConn.close();
            if (mySqlConn != null) mySqlConn.close();
        }
    }

    public static ResultSet fetchPostgreSQLUsers() throws SQLException {
        Connection pgConn = DatabaseConnectionFactory.getConnection("PostgreSQL");
        String query = "SELECT * FROM my_schema.users";
        return pgConn.createStatement().executeQuery(query);
    }

    public static ResultSet fetchMySQLUsers() throws SQLException {
        Connection myConn = DatabaseConnectionFactory.getConnection("MySQL");
        String query = "SELECT * FROM users";
        return myConn.createStatement().executeQuery(query);
    }
}