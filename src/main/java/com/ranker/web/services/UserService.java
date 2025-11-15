package com.ranker.web.services;


import com.ranker.web.dto.RegistrationDTO;

public interface UserService {

    void saveUser(RegistrationDTO registrationDTO);
}
