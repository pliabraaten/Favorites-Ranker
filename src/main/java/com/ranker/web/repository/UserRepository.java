package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ranker.web.models.UserEntity;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Custom Queries
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    UserEntity findFirstByUsername(String username);


}
