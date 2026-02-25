package com.ranker.web.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUtilTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUsernameWhenAuthenticated() {
        // Given
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "testuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When
        String username = SecurityUtil.getSessionUser();

        // Then
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void shouldReturnNullWhenAnonymous() {
        // Given
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
                "key", "anonymousUser",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When
        String username = SecurityUtil.getSessionUser();

        // Then
        assertThat(username).isNull();
    }
}
