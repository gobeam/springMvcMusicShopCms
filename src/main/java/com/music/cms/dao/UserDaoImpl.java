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
        if (user != null)
        {
            Hibernate.initialize(user.getRoles());
        }
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
            session.persist(user);

            //this is for test purpose only
            if(user.getRole_id() != null)
            {
                Role userRole = roleRepository.findById(user.getRole_id());

                if(userRole != null)
                {
                    if(user != null)
                    {
                        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
                    }

                }
            }


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
            Role userRol = roleRepository.findById(user.getRole_id());
            user.setRoles(new HashSet<Role>(Arrays.asList(userRol)));
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
        Transaction tx = session.beginTransaction();
        User user = (User) session.load(User.class, new Integer(id));
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

    @Override
    public void updatePartial(User user) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            User userData = (User) session.load(User.class,user.getId());
            if(userData != null)
            {
                userData.setFirst_name(user.getFirst_name());
                userData.setLast_name(user.getLast_name());
                userData.setAddress(user.getAddress());
                userData.setCity(user.getCity());
                userData.setCountry(user.getCountry());
                userData.setEmail(user.getEmail());
                userData.setPhone(user.getPhone());
                userData.setStatus(user.getStatus());
                userData.setZip(user.getZip());
                if(user.getPassword() != null)
                {
                    userData.setPassword(user.getPassword());
                }

                Role userRol = roleRepository.findById(user.getRole_id());
                System.out.println("forwarimn");
                System.out.println(userData.getRoles()  + "first one");
                System.out.println(userRol + "Seconf one");
                if(userData.getRoles().toArray()[0] != userRol.getRole().toCharArray())
                {
                    for(Role role : userData.getRoles())
                    {
                        userData.getRoles().remove(role);
                    }
                    userData.setRoles(new HashSet<Role>(Arrays.asList(userRol)));
                }

                session.update(userData);

            }

            tx.commit();

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
    public User findUserByEmailForUpdate(User user) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            org.hibernate.query.Query query = session.createQuery("from User where id != :id and email = :email ");
            query.setParameter("id", user.getId());
            query.setParameter("email", user.getEmail());
            tx.commit();
            return (User)query.uniqueResult();


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
}