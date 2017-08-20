package com.music.cms.dao;

import com.music.cms.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;

/**
 * Created by alis on 8/6/17.
 */
@Repository("roleDao")
@Transactional
public class RoleDaoImpl implements RoleDao{

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Role findByRole(String role) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            Role roles = (Role) session.load(Role.class, new String(role));

            tx.commit();
            return roles;


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
    public Role findById(Integer id) {

        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

           // Role roles = (Role) session.load(Role.class, new Integer(id));
            org.hibernate.query.Query query = session.createQuery("from Role where id = :id ");
            query.setParameter("id", id);


            tx.commit();
//            return roles;
            return (Role)query.uniqueResult();


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
