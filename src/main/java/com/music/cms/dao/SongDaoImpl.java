package com.music.cms.dao;

import com.music.cms.model.Category;
import com.music.cms.model.Song;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class SongDaoImpl implements SongDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Song> listAll() {
        Session session = null;
        Transaction tx = null;
        try{
            session =sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Song> criteriaQuery = builder.createQuery(Song.class);
            criteriaQuery.from(Song.class);
            List<Song> songs = session.createQuery(criteriaQuery).getResultList();
            tx.commit();
            return songs;



        }catch (RuntimeException rne)
        {
            try{
                tx.rollback();

            }catch (RuntimeException e)
            {

            }
            throw rne;

        }finally {
            if(session != null)
            {
                session.close();
            }

        }
    }

    @Override
    public Song findById(Integer id) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            org.hibernate.query.Query query = session.createQuery("from Song where id = :id");
            query.setParameter("id", id);
            tx.commit();
            return (Song)query.uniqueResult();

        }catch (RuntimeException rne)
        {
            try {
                tx.rollback();

            }catch (RuntimeException e)
            {

            }
            throw rne;

        }finally {
            if(session != null)
            {
                session.close();
            }
        }
    }

    @Override
    public void store(Song song) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);

            session.persist(song);

        }catch (RuntimeException rne)
        {
            try {
                tx.rollback();

            }catch (RuntimeException e)
            {

            }
            throw rne;

        }finally {
            if(session != null)
            {
                session.close();
            }
        }

    }

    @Override
    public void update(Song song) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            if(song != null)
            {
                session.update(song);
            }

            tx.commit();

        }catch (RuntimeException rne){
            try {
                tx.rollback();

            }catch (RuntimeException e)
            {

            }
            throw rne;

        }finally {
            if(session != null)
            {
                session.close();
            }
        }

    }

    @Override
    public void destroy(Integer id) {
        Session session = null;
        Transaction tx = null;

        try{
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Song song = findById(id);
            if(song != null)
            {
                session.delete(song);
            }
            tx.commit();


        }catch (RuntimeException rne)
        {
            try {
                tx.rollback();

            }catch (RuntimeException e)
            {

            }
            throw rne;

        }finally {
            if(session != null)
            {
                session.close();
            }
        }

    }
}
