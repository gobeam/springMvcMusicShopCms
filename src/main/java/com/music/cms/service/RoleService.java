package com.music.cms.service;

import com.music.cms.model.Role;

import java.util.List;

public interface RoleService {

    Role findById(Integer id);

    List<Role> getallRole();
}
