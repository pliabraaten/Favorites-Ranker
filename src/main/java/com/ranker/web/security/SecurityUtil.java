package com.ranker.web.security;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


// TODO: UNDERSTAND THIS -> IS THIS THE BEST WAY FOR SESSION USER MANAGEMENT? ESP SINCE USERS ONLY SEE THEIR OWN LISTS?

// IS THIS NEEDED NOW THAT THE USER USERNAME IS PASSED IN WHEN SAVING A NEW LIST?

//
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
