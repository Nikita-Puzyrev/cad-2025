package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;

public class ProductRepository {

    private final SessionFactory sessionFactory;

    public ProductRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Product product) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(product);

        tx.commit();
        session.close();
    }

    public Product findById(Integer id) {
        Session session = sessionFactory.openSession();
        Product product = session.get(Product.class, id);
        session.close();
        return product;
    }

    public List<Product> findAll() {
        Session session = sessionFactory.openSession();
        List<Product> products = session
                .createQuery("from Product", Product.class)
                .list();
        session.close();
        return products;
    }
}