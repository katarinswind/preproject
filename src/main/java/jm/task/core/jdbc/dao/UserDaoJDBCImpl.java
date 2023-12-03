package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.model.User;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Util util;
    private Statement dbStatement;

    private final String DB_NAME = "UsersDB";
    private final String DB_USER_NAME = "user";
    private final String DB_USER_PASS = "1234";
    final String DB_TABLE_NAME = "users_table";

    public UserDaoJDBCImpl() {
        util = new Util();
        dbStatement = util.getStatement();

        try{

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

    public void createUsersTable() {
        try{
            String sqlQuery = "CREATE TABLE IF NOT exists %s.%s (\n`id` INT NOT NULL AUTO_INCREMENT,\n`name` VARCHAR(45) NOT NULL,\n`lastName` VARCHAR(45) NOT NULL,\n`age` INT(3) NOT NULL,\nPRIMARY KEY (`id`));".formatted(DB_NAME, DB_TABLE_NAME);
            dbStatement.addBatch(sqlQuery);
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dropUsersTable() {
        try{
            dbStatement.addBatch("drop table if exists `%s`.`%s`;".formatted(DB_NAME, DB_TABLE_NAME));
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try{
            String sqlQuery = "insert into %s.%s (name, lastName, age) values ('%s', '%s', %d)".formatted(DB_NAME, DB_TABLE_NAME, name, lastName, age);
            dbStatement.addBatch(sqlQuery);
            dbStatement.executeBatch();
            dbStatement.clearBatch();
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeUserById(long id) {
        try{
            String sqlQuery = "delete from %s.%s where id = %d;".formatted(DB_NAME, DB_TABLE_NAME, id);
            dbStatement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        ResultSet rs = null;
        try{
            String sqlQuery = "select * from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME);
            rs = dbStatement.executeQuery(sqlQuery);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = (byte)rs.getInt("age");

                users.add(new User(name, lastName, age));
            }
        } catch (SQLException e) {

        }
        return users;
    }

    public void cleanUsersTable() {
        try{
            String sqlQuery = "delete from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME);
            dbStatement.execute(sqlQuery);
        } catch (SQLException e) {
        }
    }
}
