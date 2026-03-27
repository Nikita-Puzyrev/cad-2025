package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.bsuedu.cad.lab.entity.Customer;

import java.util.List;

public class CustomerRepository {

    private final SessionFactory sessionFactory;

    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(customer);

        tx.commit();
        session.close();
    }

    public Customer findById(Integer id) {
        Session session = sessionFactory.openSession();
        Customer customer = session.get(Customer.class, id);
        session.close();
        return customer;
    }

    public List<Customer> findAll() {
        Session session = sessionFactory.openSession();
        List<Customer> customers = session
                .createQuery("from Customer", Customer.class)
                .list();
        session.close();
        return customers;
    }
}