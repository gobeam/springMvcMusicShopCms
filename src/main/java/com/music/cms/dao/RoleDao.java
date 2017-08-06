package com.music.cms.dao;

import com.music.cms.model.Role;

/**
 * Created by alis on 8/4/17.
 */
public interface RoleDao {
    Role findByRole(String role);

    Role findById(Integer id);
}
