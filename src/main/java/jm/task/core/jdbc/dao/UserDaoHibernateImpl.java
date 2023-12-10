package jm.task.core.jdbc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoHibernateImpl implements UserDao {

    private final String DB_NAME = "UsersDB";
    private final String DB_USER_NAME = "user";
    private final String DB_USER_PASS = "1234";
    final String DB_TABLE_NAME = "User";

    private void SQLQueryTransaction(String[] querys) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (String q : querys) {
                session.createSQLQuery(q).executeUpdate();
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public UserDaoHibernateImpl() {
        String[] sqlQuery = {"CREATE DATABASE if not exists %s;".formatted(DB_NAME),
                "CREATE USER IF NOT EXISTS '%s' IDENTIFIED BY '%s';".formatted(DB_USER_NAME, DB_USER_PASS),
                "GRANT ALL on %s.* to '%s';".formatted(DB_NAME, DB_USER_NAME)};
        SQLQueryTransaction(sqlQuery);
    }


    @Override
    public void createUsersTable() {

        String[] sqlQuery = {"""
                CREATE TABLE IF NOT exists %s.%s (
                `id` INT NOT NULL AUTO_INCREMENT,
                `name` VARCHAR(45) NOT NULL,
                `lastName` VARCHAR(45) NOT NULL,
                `age` INT(3) NOT NULL,
                PRIMARY KEY (`id`));""".formatted(DB_NAME, DB_TABLE_NAME)};

        SQLQueryTransaction(sqlQuery);
    }

    @Override
    public void dropUsersTable() {
        SQLQueryTransaction(new String[]{"drop table if exists `%s`.`%s`;".formatted(DB_NAME, DB_TABLE_NAME)});
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        SQLQueryTransaction(new String[]{"insert into %s.%s (name, lastName, age) values ('%s', '%s', %d)".formatted(DB_NAME, DB_TABLE_NAME, name, lastName, age)});
    }

    @Override
    public void removeUserById(long id) {
        SQLQueryTransaction(new String[]{"delete from %s.%s where id = %d;".formatted(DB_NAME, DB_TABLE_NAME, id)});
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createSQLQuery("select * from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME)).addEntity(User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        SQLQueryTransaction(new String[]{"delete from %s.%s;".formatted(DB_NAME, DB_TABLE_NAME)});
    }
}
