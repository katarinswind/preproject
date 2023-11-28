package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Util util;
    private final String SQL_USER_NAME = "root";
    private final String SQL_USER_PASS = "memgnptc";
    private final String DB_NAME = "UsersDB";
    private final String DB_USER_NAME = "user";
    private final String DB_USER_PASS = "1234";

    public UserDaoJDBCImpl() {
        util = new Util(SQL_USER_NAME, SQL_USER_PASS, DB_NAME, DB_USER_NAME, DB_USER_PASS);
    }

    public void createUsersTable() {
        util.createTable();
    }

    public void dropUsersTable() {
        util.dropTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        util.addRecord(name, lastName, age);
    }

    public void removeUserById(long id) {
        util.removeRecordById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        ResultSet rs = util.getAll();
        try {
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
        util.deleteAll();
    }
}
