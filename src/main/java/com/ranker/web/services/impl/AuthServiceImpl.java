package com.ranker.web.services.impl;

import com.ranker.web.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    // AUTO-LOGIN: new user directly after registering
    @Override
    public void authenticateUserAndSetSession(String username, String rawPassword, HttpServletRequest request) {

        // Create auth request with username and password (pre-encoding) from the passed in registrationDTO
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, rawPassword);

        // Give Spring Security username/password to build authenticated token object
        Authentication authenticatedUser = authenticationManager.authenticate(authToken);

        // Put the authentication into the session
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        // Ties user's authentication to the HTTP session
        request.getSession(true).setAttribute(  // Gets current HTTP session; (true) => creates one if no session exists
                // Stores security context in the session via key/value pair
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,  // Key: looks for security info
                SecurityContextHolder.getContext()  // Value: security context holding authenticated user
        );
    }
}
