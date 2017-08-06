package com.music.cms.dao;

import com.music.cms.model.User;

import java.util.List;

/**
 * Created by alis on 8/6/17.
 */
public interface UserDao {

    User findById(Integer id);

    User findByEmail(String email);

    void save(User user);

    void deleteById(Long id);

    List<User> findAllUsers();
}
