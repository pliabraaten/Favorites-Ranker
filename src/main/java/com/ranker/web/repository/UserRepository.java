package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ranker.web.models.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // Custom Queries
    Optional<FavoritesList> findByUsername(String username);

}
