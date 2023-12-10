package jm.task.core.jdbc.util;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import jm.task.core.jdbc.model.User;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
//    private static String MYSQL_URL = "jdbc:mysql://localhost:3306/hibernate_db?useSSL=false";
    private static String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    private static String MYSQL_USER = "root";
    private static String MYSQL_PASS = "memgnptc";
    private static String DB_NAME = "UsersDB";
    private static String DB_USER_NAME = "user";
    private static String DB_USER_PASS = "1234";
    final String DB_TABLE_NAME = "users_table";

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = getConfiguration();
                configuration.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.err.println("----------------");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, MYSQL_URL);
        settings.put(Environment.USER, MYSQL_USER);
        settings.put(Environment.PASS, MYSQL_PASS);

        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
        configuration.setProperties(settings);
        return configuration;
    }
}
