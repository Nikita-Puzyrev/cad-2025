package ru.bsuedu.cad.lab.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.OrderDetail;

import java.util.List;

@Repository
public class OrderDetailRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public OrderDetailRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(OrderDetail detail) {
        sessionFactory.getCurrentSession().persist(detail);
    }

    public OrderDetail findById(Integer id) {
        return sessionFactory.getCurrentSession().get(OrderDetail.class, id);
    }

    public List<OrderDetail> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from OrderDetail", OrderDetail.class)
                .list();
    }
}