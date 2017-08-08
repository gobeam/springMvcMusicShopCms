package com.music.cms.dao;

import com.music.cms.model.PersistentLogin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository("tokenRepositoryDao")
@Transactional
public class HibernateTokenRepositoryImpl
		implements PersistentTokenRepository {


	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		PersistentLogin persistentLogin = new PersistentLogin();
		persistentLogin.setUsername(token.getUsername());
		persistentLogin.setSeries(token.getSeries());
		persistentLogin.setToken(token.getTokenValue());
		persistentLogin.setLast_used(token.getDate());
		session.persist(persistentLogin);
		tx.commit();
		session.close();

	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			Session session = sessionFactory.openSession();
			PersistentLogin persistentLogin = (PersistentLogin) session.load(PersistentLogin.class, new String(seriesId));

			return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
					persistentLogin.getToken(), persistentLogin.getLast_used());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void removeUserTokens(String username) {
		Session session = sessionFactory.openSession();
		PersistentLogin persistentLogin = (PersistentLogin) session.load(PersistentLogin.class, new String(username));
		Transaction tx = session.beginTransaction();
		if(null != persistentLogin){
			session.delete(persistentLogin);
		}
		tx.commit();
		session.close();

	}

	@Override
	public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		PersistentLogin persistentLogin = (PersistentLogin) session.load(PersistentLogin.class, new String(seriesId));
		if(null != persistentLogin) {
			persistentLogin.setToken(tokenValue);
			persistentLogin.setLast_used(lastUsed);
			session.update(persistentLogin);
		}
		tx.commit();
		session.close();
	}

}
