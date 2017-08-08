package com.music.cms.dao;

import com.music.cms.model.User;

import java.util.List;

/**
 * Created by alis on 8/6/17.
 */
public interface UserDao {

    User findById(int id);

    User findByEmail(String Email);

    void save(User user);

    void update(User user);

    void deleteById(int id);

    List<User> findAllUsers();

}
