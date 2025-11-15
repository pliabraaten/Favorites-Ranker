package com.ranker.web.services.impl;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;


public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUser(RegistrationDTO registrationDTO) {

        // Create user with info from registration DTO
        UserEntity user = new UserEntity();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
    }
}
