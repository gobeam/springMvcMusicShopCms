package com.music.cms.dao;

import com.music.cms.model.UserAttempts;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.LockedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDetailsDaoImpl implements UserDetailsDao{

    private static final String SQL_USERS_UPDATE_LOCKED = "UPDATE USERS SET accountNonLocked = :accnonLocked WHERE username = :username ";
    private static final String SQL_USERS_COUNT = "SELECT count(*) FROM USERS WHERE username = :username";

    private static final String SQL_USER_ATTEMPTS_GET = "SELECT * FROM USER_ATTEMPTS WHERE username = :username";
    private static final String SQL_USER_ATTEMPTS_INSERT = "INSERT INTO USER_ATTEMPTS (USERNAME, ATTEMPTS, LASTMODIFIED) VALUES( :username , :attempts , :lastmodified)";
    private static final String SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS = "UPDATE USER_ATTEMPTS SET attempts = attempts + 1, lastmodified = :lastmodified WHERE username = :username";
    private static final String SQL_USER_ATTEMPTS_RESET_ATTEMPTS = "UPDATE USER_ATTEMPTS SET attempts = 0, lastmodified = null WHERE username = :username";


    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    private SessionFactory sessionFactory;



    @Override
    public void updateFailAttempts(String username) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            UserAttempts user = getUserAttempts(username);
            if (user == null) {
                if (isUserExists(username)) {
                    // if no record, insert a new
                    org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_INSERT);
                    query.setParameter("username",String.format(username));
                    query.setParameter("attempts",new Integer("1"));
                    query.setParameter("lastmodified",new Date());

                    //getJdbcTemplate().update(SQL_USER_ATTEMPTS_INSERT, new Object[] { username, 1, new Date() });
                }
            } else {

                if (isUserExists(username)) {
                    // update attempts count, +1
                    org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS);
                    query.setParameter("lastmodified",new Date());
                    query.setParameter("username",String.format(username));
                }

                if (user.getAttempts() + 1 >= MAX_ATTEMPTS) {
                    // locked user
                    org.hibernate.query.Query query = session.createQuery(SQL_USERS_UPDATE_LOCKED);
                    query.setParameter("accnonLocked",false);
                    query.setParameter("username",String.format(username));
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
    public UserAttempts getUserAttempts(String username) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_GET);
            query.setParameter("username",String.format(username));
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
            return null;
        }

    }

    @Override
    public void resetFailAttempts(String username) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            org.hibernate.query.Query query = session.createQuery(SQL_USER_ATTEMPTS_RESET_ATTEMPTS);
            query.setParameter("username",String.format(username));
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

    private boolean isUserExists(String username) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;


        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            org.hibernate.query.Query query = session.createQuery(SQL_USERS_COUNT);
            query.setParameter("username", String.format(username));
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
