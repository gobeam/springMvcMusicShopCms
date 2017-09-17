package com.music.cms.service;

import com.music.cms.dao.RoleDao;
import com.music.cms.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleRepository;

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> getallRole() {
        return roleRepository.findAll();
    }
}
