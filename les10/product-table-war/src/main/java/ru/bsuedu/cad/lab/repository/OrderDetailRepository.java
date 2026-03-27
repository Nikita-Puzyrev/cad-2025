package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.bsuedu.cad.lab.entity.OrderDetail;

import java.util.List;

public class OrderDetailRepository {

    private final SessionFactory sessionFactory;

    public OrderDetailRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(OrderDetail detail) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(detail);

        tx.commit();
        session.close();
    }

    public OrderDetail findById(Integer id) {
        Session session = sessionFactory.openSession();
        OrderDetail detail = session.get(OrderDetail.class, id);
        session.close();
        return detail;
    }

    public List<OrderDetail> findAll() {
        Session session = sessionFactory.openSession();
        List<OrderDetail> details = session
                .createQuery("from OrderDetail", OrderDetail.class)
                .list();
        session.close();
        return details;
    }
}