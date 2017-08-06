package com.music.cms.dao;

import com.music.cms.model.Role;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by alis on 8/6/17.
 */
@Repository("roleDao")
public class RoleDaoImpl extends AbstractDao<Integer, Role> implements RoleDao{
    @Override
    public Role findByRole(String role) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("role", role));
        Role roles = (Role)criteria.uniqueResult();
        return roles;
    }

    @Override
    public Role findById(Integer id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("id", id));
        Role roles = (Role)criteria.uniqueResult();
        return roles;
    }
}
