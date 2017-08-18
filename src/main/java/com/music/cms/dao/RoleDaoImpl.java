package com.music.cms.dao;

import com.music.cms.model.Role;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        Session session = sessionFactory.openSession();
        Role roles = (Role) session.load(Role.class, new String(role));
        session.close();
        return roles;
    }

    @Override
    public Role findById(Integer id) {
        Session session = sessionFactory.openSession();
        Role roles = (Role) session.load(Role.class, new Integer(id));
        session.close();
        return roles;
    }
}
