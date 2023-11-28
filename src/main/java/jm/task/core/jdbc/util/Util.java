package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import java.io.Closeable;
import java.sql.*;

public class Util implements Closeable {
    // реализуйте настройку соеденения с БД
    final String MYSQL_URL = "jdbc:mysql://localhost/";
    final String MYSQL_USER;
    final String MYSQL_PASS;

    final String DB_NAME;
    final String DB_USER_NAME ;
    final String DB_USER_PASS;
    final String DB_TABLE_NAME = "users_table";

    Connection dbConnection;
    Statement dbStatement;

    public Util(String userName, String userPass, String dbName, String dbUser, String dbUserPass) {
        MYSQL_USER = userName;
        MYSQL_PASS = userPass;
        DB_NAME = dbName;
        DB_USER_NAME = dbUser;
        DB_USER_PASS = dbUserPass;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class \"com.mysql.cj.jdbc.Driver\" not found");
            System.exit(1);
        }

        try{
            dbConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            dbStatement = dbConnection.createStatement();

            dbStatement.addBatch("CREATE DATABASE if not exists %s;".formatted(DB_NAME));
            dbStatement.addBatch("CREATE USER IF NOT EXISTS '%s' IDENTIFIED BY '%s';".formatted(DB_USER_NAME, DB_USER_PASS));
            dbStatement.addBatch("GRANT ALL on %s.* to '%s';".formatted(DB_NAME, DB_USER_NAME));
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return dbConnection;
    }

    public void createTable() {
//        dbTableName = tableName;
        try{
            String sqlQuery = "CREATE TABLE IF NOT exists %s.%s (\n`id` INT NOT NULL AUTO_INCREMENT,\n`name` VARCHAR(45) NOT NULL,\n`lastName` VARCHAR(45) NOT NULL,\n`age` INT(3) NOT NULL,\nPRIMARY KEY (`id`));".formatted(DB_NAME, DB_TABLE_NAME);
            dbStatement.addBatch(sqlQuery);
//            dbStatement.addBatch("create table if not exists '%s'.'%s'(" +
//                    "'id' int not null auto_increment, " +
//                    "'name' varchar(45) not null, " +
//                    "'lastName' varchar(45) not null, " +
//                    "'age' int(3) not null, " +
//                    "primary key ('id'));".formatted(DB_NAME, DB_TABLE_NAME));

            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dropTable() {
        try{
//            dbStatement.addBatch("drop table if exists `%s`.`%s`;".formatted(DB_NAME, DB_TABLE_NAME));
            dbStatement.addBatch("drop table if exists `%s`.`%s`;".formatted(DB_NAME, DB_TABLE_NAME));
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addRecord(String name_, String lastName_, int age_) {
        try{
            String sqlQuery = "insert into %s.%s (name, lastName, age) values ('%s', '%s', %d)".formatted(DB_NAME, DB_TABLE_NAME, name_, lastName_, age_);
            dbStatement.addBatch(sqlQuery);
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public ResultSet getById(long id) {
        ResultSet rs = null;
        try{
            String sqlQuery = "select * from %s.%s where id = %d;".formatted(DB_NAME, DB_TABLE_NAME, id);
             rs = dbStatement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rs;
    }

    public void removeRecordById(long id) {
        try{
            String sqlQuery = "delete from %s.%s where id = %d;".formatted(DB_NAME, DB_TABLE_NAME, id);
            dbStatement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void deleteAll() {
        try{
            String sqlQuery = "delete from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME);
            dbStatement.execute(sqlQuery);
        } catch (SQLException e) {
        }
    }

    public ResultSet getAll() {
        ResultSet rs = null;
        try{
            String sqlQuery = "select * from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME);
            rs = dbStatement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rs;
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
