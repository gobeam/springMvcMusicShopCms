package com.music.cms.service;

import com.music.cms.model.User;

import java.util.List;

/**
 * Created by alis on 8/3/17.
 */
public interface UserService {

    User findUserByEmail(String email);

    void saveUser(User user);

    void update(User user);

    void updatePartial(User user);

    User findUserByEmailForUpdate(User user);

    User findById(Integer id);

    List<User> getAllUsers();


}
