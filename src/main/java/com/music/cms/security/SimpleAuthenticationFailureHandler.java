package com.music.cms.security;

import com.music.cms.dao.UserDetailsDao;
import com.music.cms.model.UserAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    UserDetailsDao userDetailsDao;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException,LockedException {
        try {
            userDetailsDao.updateFailAttempts(httpServletRequest.getParameter("email"));

            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/login?error='incorrect username'");

        } catch (LockedException exc) {

            String error = "";
            UserAttempts userAttempts =
                    userDetailsDao.getUserAttempts(httpServletRequest.getParameter("email"));

            if (userAttempts != null) {
                Date lastAttempts = userAttempts.getLastModified();
                error = "User account is locked! <br><br>Email : "
                        + httpServletRequest.getParameter("email") + "<br>Last Attempts : " + lastAttempts;
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/login?error="+error);
                //throw new LockedException("error");
            }

        }
    }
}
