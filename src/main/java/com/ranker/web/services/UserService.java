package com.ranker.web.services;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {

    void saveUser(RegistrationDTO registrationDTO);

    UserEntity findByEmail(@NotEmpty String email);

    UserEntity findByUsername(@NotEmpty String username);

    // FIXME: DO I NEED TO FIND ALL USERS??
//    List<RegistrationDTO> findAllUsers();
}
