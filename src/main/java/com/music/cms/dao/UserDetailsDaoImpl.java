package com.music.cms.dao;

import com.music.cms.model.UserAttempts;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao{

    private static final String SQL_USERS_UPDATE_LOCKED = "UPDATE User SET accountNonLocked = :accnonLocked WHERE email = :email ";
    private static final String SQL_USERS_COUNT = "SELECT count(*) FROM User WHERE email = :email";

    private static final String SQL_USER_ATTEMPTS_GET = " FROM UserAttempts WHERE email = :email";
    private static final String SQL_USER_ATTEMPTS_INSERT = "INSERT INTO UserAttempts (EMAIL, ATTEMPTS, LASTMODIFIED) VALUES( :email , :attempts , :lastmodified)";
    private static final String SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS = "UPDATE UserAttempts SET attempts = attempts + 1, lastmodified = :lastmodified WHERE email = :email";
    private static final String SQL_USER_ATTEMPTS_RESET_ATTEMPTS = "UPDATE UserAttempts SET attempts = 0, lastmodified = null WHERE email = :email";


    private static final int MAX_ATTEMPTS = 6;

    @Autowired
    private SessionFactory sessionFactory;



    @Override
    public void updateFailAttempts(String email) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            UserAttempts user = getUserAttempts(email);
            if (user == null) {
                if (isUserExists(email)) {
                    // if no record, insert a new
                    org.hibernate.query.Query query = session.createNativeQuery(SQL_USER_ATTEMPTS_INSERT);
                    query.setParameter("email",String.format(email));
                    query.setParameter("attempts",new Integer("1"));
                    query.setParameter("lastmodified",new Date());
                    query.executeUpdate();

                    //getJdbcTemplate().update(SQL_USER_ATTEMPTS_INSERT, new Object[] { email, 1, new Date() });
                }
            } else {

                if (isUserExists(email)) {
                    // update attempts count, +1
                    org.hibernate.query.Query query = session.createNativeQuery(SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS);
                    query.setParameter("lastmodified",new Date());
                    query.setParameter("email",String.format(email));
                    query.executeUpdate();
                }

                if (user.getAttempts() + 1 >= MAX_ATTEMPTS) {
                    // locked user
                    org.hibernate.query.Query query = session.createNativeQuery(SQL_USERS_UPDATE_LOCKED);
                    query.setParameter("accnonLocked",false);
                    query.setParameter("email",String.format(email));
                    query.executeUpdate();
                    // throw exception
                    throw new LockedException("User Account is locked!");
                }

            }
            tx.commit();

        }catch (RuntimeException rne){
            try{
                tx.rollback();
            }catch(RuntimeException e){

            }
            throw rne;
        }finally{

            if(session!=null){
                session.close();
            }
        }
    }

    @Override
    public UserAttempts getUserAttempts(String email) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_GET);
            query.setParameter("email",String.format(email));
            tx.commit();
            return (UserAttempts)query.uniqueResult();

        } catch (EmptyResultDataAccessException e) {
            try{
                tx.rollback();
            }catch(RuntimeException rne){

            }
            throw e;
        }finally{

            if(session!=null){
                session.close();
            }
        }

    }

    @Override
    public void resetFailAttempts(String email) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_RESET_ATTEMPTS);
            query.setParameter("email",String.format(email));
            tx.commit();
        } catch (RuntimeException e) {
            try{
                tx.rollback();
            }catch(RuntimeException rne){

            }
            throw e;
        }finally{

            if(session!=null){
                session.close();
            }
        }


    }

    private boolean isUserExists(String email) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;


        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            org.hibernate.query.Query query = session.createQuery(SQL_USERS_COUNT);
            query.setParameter("email", String.format(email));
            tx.commit();
            Integer count = ((Number) query.uniqueResult()).intValue();
            if (count > 0) {
                result = true;
            }
        } catch (RuntimeException e) {
            try {
                tx.rollback();
            } catch (RuntimeException rne) {

            }
            throw e;
        } finally {

            if (session != null) {
                session.close();
            }


        }

        return result;


    }


}
