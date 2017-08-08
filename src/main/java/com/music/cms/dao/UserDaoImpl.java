package com.music.cms.dao;

import com.music.cms.model.User;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Locale;

/**
 * Created by alis on 8/6/17.
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public User findById(int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.load(User.class, new Integer(id));
        logger.info("User loaded successfully, User details="+user);
        session.close();
        return user;
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.openSession();
        User user = (User) session.load(User.class, new String(email));
        logger.info("User loaded successfully, User details="+user);
        session.close();
        return user;
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(user);
        tx.commit();
        session.close();
        logger.info("User saved successfully, Person Details="+user);
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(user);
        tx.commit();
        session.close();
        logger.info("User updated successfully, Person Details="+user);
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.load(User.class, new Integer(id));
        Transaction tx = session.beginTransaction();
        if(null != user){
            session.delete(user);
        }
        tx.commit();
        session.close();
        logger.info("User deleted successfully, user details="+user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        // Open a session
        Session session = sessionFactory.openSession();


        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<User> criteria = builder.createQuery(User.class);

        // Specify criteria root
        criteria.from(User.class);

        // Execute query
        List<User> users = session.createQuery(criteria).getResultList();

        // Close session
        session.close();

        return users;
    }
}