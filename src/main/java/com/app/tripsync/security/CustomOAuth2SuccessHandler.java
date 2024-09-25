package com.app.tripsync.security;

import com.app.tripsync.model.Admin;
import com.app.tripsync.model.User;
import com.app.tripsync.service.AdminService;
import com.app.tripsync.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttribute("email");

        // Assign roles based on email or other criteria
        if (email.endsWith("@admin.com")) {
            // Register or update Admin in the database
            adminService.registerAdmin(new Admin(email));
        } else {
            // Register or update User in the database
            userService.registerUser(new User(email));
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
