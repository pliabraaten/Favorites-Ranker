package com.ranker.web.services;

import com.ranker.web.models.PasswordResetToken;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.PasswordResetTokenRepository;
import com.ranker.web.services.impl.PasswordResetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
    }

    // ==================== createPasswordResetToken ====================

    @Test
    void shouldCreateTokenAndSendEmailForExistingUser() {
        // Given
        when(userService.findByEmail("test@example.com")).thenReturn(testUser);

        // When
        passwordResetService.createPasswordResetToken("Test@Example.COM");

        // Then
        verify(tokenRepository).deleteByUser(testUser);
        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(emailService).sendPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    void shouldSilentlyReturnForNonExistentEmail() {
        // Given — security: don't reveal if email exists
        when(userService.findByEmail("unknown@example.com")).thenReturn(null);

        // When
        passwordResetService.createPasswordResetToken("unknown@example.com");

        // Then — no token created, no email sent
        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    void shouldDeleteExistingTokensBeforeCreatingNew() {
        // Given
        when(userService.findByEmail("test@example.com")).thenReturn(testUser);

        // When
        passwordResetService.createPasswordResetToken("test@example.com");

        // Then — deleteByUser should be called before save
        var inOrder = inOrder(tokenRepository);
        inOrder.verify(tokenRepository).deleteByUser(testUser);
        inOrder.verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    // ==================== validateToken ====================

    @Test
    void shouldReturnTrueForValidNonExpiredToken() {
        // Given
        PasswordResetToken token = new PasswordResetToken("valid-token", testUser);
        // token has 30 min expiry from creation, so it's not expired
        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        // When
        boolean result = passwordResetService.validateToken("valid-token");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForMissingToken() {
        // Given
        when(tokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());

        // When
        boolean result = passwordResetService.validateToken("nonexistent");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        // Given
        PasswordResetToken token = new PasswordResetToken("expired-token", testUser);
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1)); // already expired
        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        // When
        boolean result = passwordResetService.validateToken("expired-token");

        // Then
        assertThat(result).isFalse();
    }

    // ==================== resetPassword ====================

    @Test
    void shouldResetPasswordAndDeleteToken() {
        // Given
        PasswordResetToken token = new PasswordResetToken("valid-token", testUser);
        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        // When
        passwordResetService.resetPassword("valid-token", "newpassword");

        // Then
        verify(userService).updatePassword(testUser, "newpassword");
        verify(tokenRepository).delete(token);
    }

    @Test
    void shouldThrowForInvalidTokenOnReset() {
        // Given
        when(tokenRepository.findByToken("invalid")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> passwordResetService.resetPassword("invalid", "newpass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid token");
    }

    @Test
    void shouldThrowForExpiredTokenOnReset() {
        // Given
        PasswordResetToken token = new PasswordResetToken("expired-token", testUser);
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        // When & Then
        assertThatThrownBy(() -> passwordResetService.resetPassword("expired-token", "newpass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token has expired");
    }
}
