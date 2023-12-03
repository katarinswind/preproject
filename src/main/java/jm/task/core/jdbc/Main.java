package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("dddd", "ffff", (byte)12);
        userService.saveUser("fdkg", "ggggg", (byte)12);
        userService.saveUser("rewerew", ";dlasld", (byte)12);
        userService.saveUser("asasas", "eqwe", (byte)12);

        userService.removeUserById(1);
        List<User> usersList = userService.getAllUsers();
        for (User user: usersList) {
            System.out.printf("name: %s, lastName: %s, age: %d\n", user.getName(), user.getLastName(), user.getAge());
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
