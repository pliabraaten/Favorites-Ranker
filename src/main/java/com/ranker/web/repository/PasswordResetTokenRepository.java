package com.ranker.web.repository;

import com.ranker.web.models.PasswordResetToken;
import com.ranker.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(UserEntity user);
}
