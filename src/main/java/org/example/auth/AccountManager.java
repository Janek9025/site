package org.example.auth;

import org.example.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {

    DatabaseConnection databaseConnection;

    public AccountManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean register(String username, String password) {
        Connection connection = databaseConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts (username, password) VALUES (?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String createSQLTable = "CREATE TABLE IF NOT EXISTS accounts( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";

        PreparedStatement statement = connection.prepareStatement(createSQLTable);

        statement.executeUpdate();

        databaseConnection.disconnect();
    }

    public boolean authenticate(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement("SELECT password from accounts where username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            String storedPassword = resultSet.getString("password");
            return password.equals(storedPassword);
        }
        return false;
    }

    public Account getAccount(String username) throws SQLException {
        int id;
        String usernameAccount;
        String url = "SELECT * FROM accounts WHERE username=?";
        PreparedStatement statement = databaseConnection.getConnection().
                prepareStatement(url);
        statement.setString(1,username);
        ResultSet resultSet = statement.executeQuery(url);
        if(resultSet.next()){
            id = resultSet.getInt(1);
            usernameAccount = resultSet.getString(2);
            return new Account(id,usernameAccount);
        } else return  null;
    }

    public Account getAccount(int userID) throws SQLException {
        int idAccount;
        String usernameAccount;
        PreparedStatement statement = databaseConnection.getConnection().
                prepareStatement("SELECT * FROM accounts WHERE ID=?");
        statement.setInt(1,userID);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            idAccount = resultSet.getInt(1);
            usernameAccount = resultSet.getString(2);
            return new Account(idAccount,usernameAccount);
        } else return null;
    }

}
