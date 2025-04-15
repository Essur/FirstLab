package com.deqi.firstlab.gui;

import com.deqi.firstlab.service.DistributedTransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DistributedTransactionDialog {

    public static void show(JFrame parent) {
        JTextField usernameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField passwordField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Distributed Insert",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                DistributedTransactionService.executeDistributedInsert(username, email, password);

                ResultSet pgResults = DistributedTransactionService.fetchPostgreSQLUsers();
                ResultSet myResults = DistributedTransactionService.fetchMySQLUsers();

                showTable("PostgreSQL Users", pgResults);
                showTable("MySQL Users", myResults);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(parent, "Error during transaction: " + ex.getMessage(),
                        "Transaction Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showTable(String title, ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
