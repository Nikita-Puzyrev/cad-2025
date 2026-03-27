package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;

@Repository
public class OrderRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public OrderRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Order save(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(order);
        return order;
    }

    public Order findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Order.class, id);
    }

    public List<Order> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Order", Order.class)
                .list();
    }

    public void deleteById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, id);
        if (order != null) {
            session.remove(order);
        }
    }
}