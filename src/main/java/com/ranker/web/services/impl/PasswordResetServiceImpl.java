package com.ranker.web.services.impl;

import com.ranker.web.models.PasswordResetToken;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.PasswordResetTokenRepository;
import com.ranker.web.services.EmailService;
import com.ranker.web.services.PasswordResetService;
import com.ranker.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private PasswordResetTokenRepository tokenRepository;
    private UserService userService;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public PasswordResetServiceImpl(PasswordResetTokenRepository tokenRepository,
                                    UserService userService,
                                    EmailService emailService,
                                    PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void createPasswordResetToken(String email) {
        UserEntity user = userService.findByEmail(email.toLowerCase());

        if (user == null) {
            // Security: Don't reveal if email exists
            return;
        }

        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        // Send email with reset link
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        return resetToken.isPresent() && !resetToken.get().isExpired();
    }


    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token has expired");
        }

        // Update user password
        UserEntity user = resetToken.getUser();
        userService.updatePassword(user, newPassword);

        // Delete token (one-time use)
        tokenRepository.delete(resetToken);
    }
}