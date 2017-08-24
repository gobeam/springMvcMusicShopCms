package com.music.cms.dao;

import com.music.cms.model.Role;
import com.music.cms.model.User;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by alis on 8/6/17.
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RoleDao roleRepository;


    @Override
    public User findById(int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.load(User.class, new Integer(id));
        logger.info("User loaded successfully, User details="+user);
        session.close();
        return user;
    }

    @Override
    @Transactional(readOnly=true)
    public User findByEmail(String email) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));
        Query<User> q=session.createQuery(query);
        List<User> user=q.getResultList();
        if (!user.isEmpty())
        {
            Hibernate.initialize(user.get(0).getRoles());
        }

        session.close();

        if (user.isEmpty()) return null;
        else if (user.size() == 1) return user.get(0);
        throw new NonUniqueResultException(user.size());



    }

    @Override
    public void save(User user) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            System.out.println("addedsuccess");
            session.persist(user);

            //this is for test purpose only
            Role userRole = roleRepository.findById(1);
            System.out.println(userRole);
            System.out.println(userRole.getRole());

            if(userRole != null)
            {
                System.out.println(userRole);
                if(user != null)
                {
                    user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
                }

            }
            System.out.println((user));

            tx.commit();
            logger.info("User saved successfully, Person Details="+user);

        }catch(RuntimeException e){
            try{
                tx.rollback();
            }catch(RuntimeException rbe){

            }
            throw e;
        }finally{

            if(session!=null){
                session.close();
            }
        }

    }

    @Override
    public void update(User user) {
        Session session = null;
        Transaction tx = null;
        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            session.update(user);
            tx.commit();
            logger.info("User updated successfully, Person Details="+user);
        }catch (RuntimeException e)
        {
            try{
                tx.rollback();

            }catch (RuntimeException rne)
            {

            }
            throw e;
        }finally {
            if(session != null)
            {
                session.close();
            }
        }
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