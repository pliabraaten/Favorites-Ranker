package com.ranker.web.security;

import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByUsernameAndAssignRoleUser() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("encoded_password");
        when(userRepository.findFirstByUsername("testuser")).thenReturn(user);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("encoded_password");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_USER");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionForUnknownUser() {
        // Given
        when(userRepository.findFirstByUsername("unknown")).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Invalid username or password");
    }
}
