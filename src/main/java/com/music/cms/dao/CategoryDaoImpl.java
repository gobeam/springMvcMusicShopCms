package com.music.cms.dao;

import com.music.cms.model.Category;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Category findById(int id) {
        Session session = null;
        Transaction tx = null;
        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

//            Category category = (Category) session.load(Category.class,new Integer(id));
//            Hibernate.initialize(category);

            org.hibernate.query.Query query= session.createQuery("from Category where id = :id ");
            query.setParameter("id", id);
            tx.commit();
            return (Category)query.uniqueResult();
        }catch (RuntimeException e)
        {
            try{
                tx.rollback();

            }catch (RuntimeException rne)
            {
            }
            throw e;
        }finally {
            if(session!=null){
                session.close();
            }
        }
    }

    @Override
    public void save(Category category) {

    }

    @Override
    public void update(Category category) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<Category> findAllCategory() {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);
            criteriaQuery.from(Category.class);
            List<Category> categories = session.createQuery(criteriaQuery).getResultList();

            tx.rollback();

            return categories;

        }catch (RuntimeException e)
        {
            try{
                tx.rollback();
            }catch (RuntimeException rne)
            {

            }
            throw e;

        }finally {
            if(session!=null){
                session.close();
            }
        }
    }
}
