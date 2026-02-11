package com.ranker.web.services.impl;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public void saveUser(RegistrationDTO registrationDTO) {

        // Normalize email/username
        String email = registrationDTO.getEmail().toLowerCase();
        String username = registrationDTO.getUsername().trim();

        // ------------- ONLY TRIGGERS IF CONTROLLER MISSES CHECK ------------------
        // ---- users not created through UI, Race Conditions, future controller changes -----
        // -- "Validation must be enforced on every layer that receives untrusted input" --
        if(userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if(userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        // ------------- ---------------------------------------- ------------------

        // Create user with info from registration DTO
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));  // Encrypts user password

        userRepository.save(user);  // @Transactional only updates exiting entities but doesn't create new
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
