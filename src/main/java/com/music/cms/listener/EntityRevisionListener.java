package com.music.cms.listener;

import com.music.cms.model.RevisionsEntity;
import com.music.cms.model.User;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by mndeveci on 21.11.2016.
 */
public class EntityRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        System.out.println("New revision is created: " + o);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null)
        {
            User user = (User)authentication.getPrincipal();
            String email = user.getEmail();

            RevisionsEntity exampleRevEntity = (RevisionsEntity) o;
            exampleRevEntity.setUsername(email);
        }

    }
}
