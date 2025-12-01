package com.ranker.web.services.impl;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void saveUser(RegistrationDTO registrationDTO) {

        // Create user with info from registration DTO
        UserEntity user = new UserEntity();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());

        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));  // Encrypts user password

        userRepository.save(user);  // Save to DB
    }


    @Override
    public UserEntity findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {

        return userRepository.findByUsername(username);
    }
}
