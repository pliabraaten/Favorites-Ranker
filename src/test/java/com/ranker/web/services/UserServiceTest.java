package com.ranker.web.services;

import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // ==================== saveUser ====================

    @Test
    void shouldSaveNewUser() {
        // Given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setUsername("  newuser  ");
        dto.setEmail("Test@Example.COM");
        dto.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        // When
        userService.saveUser(dto);

        // Then
        verify(userRepository).save(argThat(user -> {
            assertThat(user.getUsername()).isEqualTo("newuser"); // trimmed
            assertThat(user.getEmail()).isEqualTo("test@example.com"); // lowercased
            assertThat(user.getPassword()).isEqualTo("encoded_password"); // encoded
            return true;
        }));
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        // Given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setUsername("existinguser");
        dto.setEmail("new@example.com");
        dto.setPassword("password123");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existinguser");
        when(userRepository.findByUsername("existinguser")).thenReturn(existingUser);

        // When & Then
        assertThatThrownBy(() -> userService.saveUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        // Given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setUsername("newuser");
        dto.setEmail("Existing@Example.COM");
        dto.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(null);

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("existing@example.com");
        when(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser);

        // When & Then
        assertThatThrownBy(() -> userService.saveUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already exists");

        verify(userRepository, never()).save(any());
    }

    // ==================== updatePassword ====================

    @Test
    void shouldUpdatePassword() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setPassword("old_encoded");
        when(passwordEncoder.encode("newpassword")).thenReturn("new_encoded");

        // When
        userService.updatePassword(user, "newpassword");

        // Then
        assertThat(user.getPassword()).isEqualTo("new_encoded");
        verify(userRepository).save(user);
    }

    // ==================== findByEmail ====================

    @Test
    void shouldFindByEmail() {
        // Given
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // When
        UserEntity result = userService.findByEmail("test@example.com");

        // Then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnNullWhenEmailNotFound() {
        // Given
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

        // When
        UserEntity result = userService.findByEmail("missing@example.com");

        // Then
        assertThat(result).isNull();
    }

    // ==================== findByUsername ====================

    @Test
    void shouldFindByUsername() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // When
        UserEntity result = userService.findByUsername("testuser");

        // Then
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldReturnNullWhenUsernameNotFound() {
        // Given
        when(userRepository.findByUsername("missing")).thenReturn(null);

        // When
        UserEntity result = userService.findByUsername("missing");

        // Then
        assertThat(result).isNull();
    }
}
