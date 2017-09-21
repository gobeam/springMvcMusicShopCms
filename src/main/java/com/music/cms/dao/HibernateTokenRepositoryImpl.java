package com.music.cms.dao;

import com.music.cms.model.PersistentLogin;
import com.music.cms.model.Role;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Repository("tokenRepositoryDao")
@Transactional
public class HibernateTokenRepositoryImpl
		implements PersistentTokenRepository {


	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		Session session = null;
		Transaction tx = null;
		try{
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			tx.setTimeout(5);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PersistentLogin> query = builder.createQuery(PersistentLogin.class);
			Root<PersistentLogin> root = query.from(PersistentLogin.class);
			query.select(root).where(builder.equal(root.get("email"), token.getUsername()));
			Query<PersistentLogin> q=session.createQuery(query);
			List<PersistentLogin> persistentLoginCheck = q.getResultList();
			System.out.println("persistent login entry yo");
			System.out.println(persistentLoginCheck);
			if (persistentLoginCheck.isEmpty())
			{
				System.out.println("yo you reach here");
				PersistentLogin persistentLogin = new PersistentLogin();
				persistentLogin.setUsername(token.getUsername());
				persistentLogin.setSeries(token.getSeries());
				persistentLogin.setToken(token.getTokenValue());
				persistentLogin.setLast_used(token.getDate());
				session.persist(persistentLogin);
			}



			tx.commit();

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
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		System.out.println("getTokenForSeries");
		Session session = null;
		Transaction tx = null;

		try{
			session = sessionFactory.openSession();

			tx = session.beginTransaction();

			tx.setTimeout(5);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PersistentLogin> query = builder.createQuery(PersistentLogin.class);
			Root<PersistentLogin> root = query.from(PersistentLogin.class);
			query.select(root).where(builder.equal(root.get("series"), seriesId));
			Query<PersistentLogin> q=session.createQuery(query);
			List<PersistentLogin> persistentLogin = q.getResultList();
			tx.commit();
			if (!persistentLogin.isEmpty())
			{
				System.out.println("noempty");
				return new PersistentRememberMeToken(persistentLogin.get(0).getUsername(), persistentLogin.get(0).getSeries(),
						persistentLogin.get(0).getToken(), persistentLogin.get(0).getLast_used());
			}else{
				return null;
			}


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
	public void removeUserTokens(String username) {
		System.out.println("removeUserTokens");
		Session session = null;
		Transaction tx = null;

		try{
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			tx.setTimeout(5);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PersistentLogin> query = builder.createQuery(PersistentLogin.class);
			Root<PersistentLogin> root = query.from(PersistentLogin.class);
			query.select(root).where(builder.equal(root.get("email"), username));
			Query<PersistentLogin> q=session.createQuery(query);
			List<PersistentLogin> persistentLogin = q.getResultList();
			if (!persistentLogin.isEmpty())
			{
				session.delete(persistentLogin.get(0));
			}

			tx.commit();
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
	public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
		System.out.println("updateToken");
		Session session = null;
		Transaction tx = null;

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			tx.setTimeout(5);

			//PersistentLogin persistentLogin = (PersistentLogin) session.load(PersistentLogin.class, new String(seriesId));
			Query query = session.createQuery(" from PersistentLogin where series = :series");
			query.setParameter("series",seriesId);
			PersistentLogin persistentLoginToken = (PersistentLogin)query.uniqueResult();

			if (null != persistentLoginToken) {
				persistentLoginToken.setToken(tokenValue);
				persistentLoginToken.setLast_used(lastUsed);
				session.update(persistentLoginToken);
			}
			tx.commit();

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
