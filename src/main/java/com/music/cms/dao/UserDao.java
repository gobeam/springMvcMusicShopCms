package com.music.cms.dao;

import com.music.cms.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alis on 8/3/17.
 */
@Repository("userDao")
public interface UserDao extends JpaRepository<User,Long>{
    User findByEmail(String email);
}
