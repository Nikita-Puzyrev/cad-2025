package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;

public class OrderRepository {

    private final SessionFactory sessionFactory;

    public OrderRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Order order) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.merge(order);

        tx.commit();
        session.close();
    }

    public Order findById(Integer id) {
        Session session = sessionFactory.openSession();
        Order order = session.get(Order.class, id);
        session.close();
        return order;
    }

    public List<Order> findAll() {
        Session session = sessionFactory.openSession();
        List<Order> orders = session
                .createQuery("from Order", Order.class)
                .list();
        session.close();
        return orders;
    }
}