package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import java.io.Closeable;
import java.sql.*;

public class Util implements Closeable {
    // реализуйте настройку соеденения с БД
    private final String MYSQL_URL = "jdbc:mysql://localhost/";

    private final String MYSQL_USER = "root";
    private final String MYSQL_PASS = "memgnptc";
    private final String DB_NAME = "UsersDB";
    private final String DB_USER_NAME = "user";
    private final String DB_USER_PASS = "1234";
    final String DB_TABLE_NAME = "users_table";

    Connection dbConnection;
    Statement dbStatement;

    public Util() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class \"com.mysql.cj.jdbc.Driver\" not found");
            System.exit(1);
        }

        try{
            dbConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            dbStatement = dbConnection.createStatement();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public Statement getStatement() {
        return dbStatement;
    }

    public void close() {
        try {
            dbStatement.close();
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
