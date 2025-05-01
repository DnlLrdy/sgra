package com.project.sgra.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMINISTRADOR")) {
            response.sendRedirect("/sgra/admin/rutas");
        } else if (roles.contains("ROLE_CONDUCTOR")) {
            response.sendRedirect("/sgra/conductor");
        } else if (roles.contains("ROLE_USUARIO")) {
            response.sendRedirect("/sgra/usuario");
        } else {
            response.sendRedirect("/sgra/login");
        }
    }

}
