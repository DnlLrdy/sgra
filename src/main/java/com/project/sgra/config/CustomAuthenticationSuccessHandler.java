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

    private static final String ROLE_USUARIO = "ROLE_USUARIO";
    private static final String ROLE_CONDUCTOR = "ROLE_CONDUCTOR";
    private static final String ROLE_ADMIN = "ROLE_ADMINISTRADOR";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains(ROLE_USUARIO)) {
            response.sendRedirect("/usuario/buscar");
        } else if (roles.contains(ROLE_CONDUCTOR)) {
            response.sendRedirect("/conductor");
        } else if (roles.contains(ROLE_ADMIN)) {
            response.sendRedirect("/admin/rutas");
        } else {
            response.sendRedirect("/login");
        }
    }

}
