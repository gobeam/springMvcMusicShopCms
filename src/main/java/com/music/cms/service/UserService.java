package com.music.cms.service;

import com.music.cms.model.User;

/**
 * Created by alis on 8/3/17.
 */
public interface UserService {

    public User findUserByEmail(String email);

    public void saveUser(User user);

    public User findById(Integer id);

}
