package com.music.cms.dao;

import com.music.cms.model.UserAttempts;

public interface UserDetailsDao {
    void updateFailAttempts(String username);

    void resetFailAttempts(String username);

    UserAttempts getUserAttempts(String username);
}
