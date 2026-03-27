package ru.bsuedu.cad.lab.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Category;

import java.util.List;

@Repository
public class CategoryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Category category) {
        sessionFactory.getCurrentSession().persist(category);
    }

    public Category findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Category.class, id);
    }

    public List<Category> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Category", Category.class)
                .list();
    }
}