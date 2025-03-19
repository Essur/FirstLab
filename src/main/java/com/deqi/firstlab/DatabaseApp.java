package com.deqi.firstlab;

import com.deqi.firstlab.connection.DatabaseConnectionFactory;
import com.deqi.firstlab.service.QueryExecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseApp extends JFrame {
    private final JComboBox<String> connectionTypeCombo;
    private final JTextField queryField;
    private final JTable resultTable;
    private final JLabel timeLabel;
    private Connection connection = null;

    public DatabaseApp() {
        setTitle("Database Query Executor");
        setLayout(new BorderLayout());

        // Set minimum width and height for the window
        setMinimumSize(new Dimension(600, 400));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // Connection type dropdown
        connectionTypeCombo = new JComboBox<>(new String[]{"JDBC", "ODBC"});
        topPanel.add(new JLabel("Connection Type:"));
        topPanel.add(connectionTypeCombo);

        // Query field
        queryField = new JTextField(30);
        topPanel.add(new JLabel("SQL Query:"));
        topPanel.add(queryField);

        // Execute button
        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeQuery();
            }
        });
        topPanel.add(executeButton);

        add(topPanel, BorderLayout.NORTH);

        // Create JTable to display query results
        resultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Time label
        timeLabel = new JLabel("Execution Time: 0 ms");
        add(timeLabel, BorderLayout.SOUTH);

        // Set window size and behavior
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void executeQuery() {
        String query = queryField.getText();
        String connectionType = (String) connectionTypeCombo.getSelectedItem();
        long executionTime = 0;

        try {
            connection = DatabaseConnectionFactory.getConnection(connectionType);
            ResultSet resultSet = QueryExecutor.executeQuery(connection, query);
            executionTime = displayResultsInTable(resultSet);
        } catch (SQLException e) {
            showErrorDialog("Error: " + e.getMessage());
        } finally {
            timeLabel.setText("Execution Time: " + executionTime + " ms");
        }
    }

    // Displays query results in JTable or shows a message if empty
    private long displayResultsInTable(ResultSet resultSet) throws SQLException {
        long startTime = System.currentTimeMillis();

        // Get metadata to determine column names
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Create column names array
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Create a DefaultTableModel with column names
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        boolean hasResults = false;
        while (resultSet.next()) {
            hasResults = true;
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(row);
        }

        if (!hasResults) {
            showInfoDialog("No results found for the given query.");
        }

        resultTable.setModel(tableModel);

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    // Shows an error message in a pop-up dialog
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Shows an informational message in a pop-up dialog
    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new DatabaseApp();
    }
}
