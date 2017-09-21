package com.music.cms.dao;

import com.music.cms.model.Role;

import java.util.List;

/**
 * Created by alis on 8/4/17.
 */
public interface RoleDao {
    Role findByRole(String role);

    Role findById(Integer id);

    List<Role> findAll();

    void store(Role role);

    void update(Role role);

    void destroy(Integer id);
}
