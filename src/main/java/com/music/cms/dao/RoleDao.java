package com.music.cms.dao;

import com.music.cms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alis on 8/4/17.
 */
public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
