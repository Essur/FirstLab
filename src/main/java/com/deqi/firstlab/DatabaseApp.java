package com.deqi.firstlab;

import com.deqi.firstlab.connection.DatabaseConnectionFactory;
import com.deqi.firstlab.gui.DistributedTransactionDialog;
import com.deqi.firstlab.service.QueryExecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseApp extends JFrame {
    private final JComboBox<String> dbTypeCombo;
    private final JTextField queryField;
    private final JTable resultTable;
    private final JLabel timeLabel;

    public DatabaseApp() {
        setTitle("Database Query Executor");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(900, 400));

        JPanel topPanel = new JPanel(new FlowLayout());

        dbTypeCombo = new JComboBox<>(new String[]{"PostgreSQL", "MySQL"});
        topPanel.add(new JLabel("Database Type:"));
        topPanel.add(dbTypeCombo);

        queryField = new JTextField(30);
        topPanel.add(new JLabel("SQL Query:"));
        topPanel.add(queryField);

        JButton executeButton = new JButton("Execute Query");
        executeButton.addActionListener(this::executeQuery);
        topPanel.add(executeButton);

        JButton transactionButton = new JButton("Execute Transaction");
        transactionButton.addActionListener(e -> DistributedTransactionDialog.show(this));
        topPanel.add(transactionButton);

        add(topPanel, BorderLayout.NORTH);

        resultTable = new JTable();
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        timeLabel = new JLabel("Execution Time: 0 ms");
        add(timeLabel, BorderLayout.SOUTH);

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void executeQuery(ActionEvent e) {
        String query = queryField.getText();
        String dbType = (String) dbTypeCombo.getSelectedItem();
        long executionTime = 0;

        try (Connection connection = DatabaseConnectionFactory.getConnection(dbType)) {
            long startTime = System.currentTimeMillis();
            ResultSet resultSet = QueryExecutor.executeQuery(connection, query);
            executionTime = System.currentTimeMillis() - startTime;
            displayResultsInTable(resultSet);
        } catch (SQLException ex) {
            showErrorDialog("Error: " + ex.getMessage());
        } finally {
            timeLabel.setText("Execution Time: " + executionTime + " ms");
        }
    }


    private void displayResultsInTable(ResultSet resultSet) throws SQLException {
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
        resultTable.setModel(tableModel);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DatabaseApp::new);
    }
}
