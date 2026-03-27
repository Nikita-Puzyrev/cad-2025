package ru.bsuedu.cad.lab.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.bsuedu.cad.lab.entity.Category;

import java.util.List;

public class CategoryRepository {

    private final SessionFactory sessionFactory;

    public CategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Category category) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(category);

        tx.commit();
        session.close();
    }

    public Category findById(Integer id) {
        Session session = sessionFactory.openSession();
        Category category = session.get(Category.class, id);
        session.close();
        return category;
    }

    public List<Category> findAll() {
        Session session = sessionFactory.openSession();
        List<Category> categories = session
                .createQuery("from Category", Category.class)
                .list();
        session.close();
        return categories;
    }
}