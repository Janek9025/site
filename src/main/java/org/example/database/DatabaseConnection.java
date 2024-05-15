package org.example.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;


    public Connection getConnection() {
        return connection;
    }


    public void connect(String filepath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
        System.out.println("Udało się Połączyć!");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


}
