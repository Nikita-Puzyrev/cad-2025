package ru.bsuedu.cad.lab.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Customer;

import java.util.List;

@Repository
public class CustomerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Customer customer) {
        sessionFactory.getCurrentSession().persist(customer);
    }

    public Customer findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Customer.class, id);
    }

    public List<Customer> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Customer", Customer.class)
                .list();
    }
}