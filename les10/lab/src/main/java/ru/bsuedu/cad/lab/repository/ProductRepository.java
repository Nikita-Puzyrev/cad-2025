package ru.bsuedu.cad.lab.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;

@Repository
public class ProductRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ProductRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Product product) {
        sessionFactory.getCurrentSession().persist(product);
    }

    public Product findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Product.class, id);
    }

    public List<Product> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Product", Product.class)
                .list();
    }
}