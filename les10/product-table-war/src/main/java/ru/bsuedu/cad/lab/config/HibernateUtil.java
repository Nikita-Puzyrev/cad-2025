package ru.bsuedu.cad.lab.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ru.bsuedu.cad.lab.entity.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration cfg = new Configuration();

            cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
            cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:lab;DB_CLOSE_DELAY=-1");
            cfg.setProperty("hibernate.connection.username", "sa");
            cfg.setProperty("hibernate.connection.password", "");
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");
            cfg.setProperty("hibernate.show_sql", "true");
            cfg.setProperty("hibernate.current_session_context_class", "thread");

            cfg.addAnnotatedClass(Customer.class);
            cfg.addAnnotatedClass(Category.class);
            cfg.addAnnotatedClass(Product.class);
            cfg.addAnnotatedClass(Order.class);
            cfg.addAnnotatedClass(OrderDetail.class);

            return cfg.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("SessionFactory build failed", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}