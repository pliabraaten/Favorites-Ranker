package com.ranker.web.security;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


// Retrieve currently logged in user
public class SecurityUtil {
    public static String getSessionUser() {

        // Pulls user information from the cookie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is logged in
        if(!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUser = authentication.getName();
            return currentUser;
        }
        return null;
    }
}
