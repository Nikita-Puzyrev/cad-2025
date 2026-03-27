package ru.bsuedu.cad.lab.repository;

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

    public void save(Order order) {
        sessionFactory.getCurrentSession().persist(order);
    }

    public Order findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Order.class, id);
    }

    public List<Order> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Order", Order.class)
                .list();
    }
}