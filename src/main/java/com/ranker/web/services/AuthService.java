package com.ranker.web.services;


import jakarta.servlet.http.HttpServletRequest;


public interface AuthService {

    void authenticateUserAndSetSession(String username, String rawPassword, HttpServletRequest request);

}
