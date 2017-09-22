package com.music.cms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection <? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            try {
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/home");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isAdmin) {
            try {
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin/home");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
