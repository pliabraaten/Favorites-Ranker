package com.ranker.web.controllers;

import com.ranker.web.models.UserEntity;
import com.ranker.web.security.CustomUserDetailsService;
import com.ranker.web.security.SecurityConfig;
import com.ranker.web.services.AuthService;
import com.ranker.web.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ==================== GET /register ====================

    @Test
    void shouldShowRegistrationForm() throws Exception {
        // /register is permitAll in SecurityConfig
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    // ==================== POST /register/save ====================

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        // Given — /register/** is permitAll
        when(userService.findByEmail(anyString())).thenReturn(null);
        when(userService.findByUsername(anyString())).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/register/save")
                .param("username", "newuser")
                .param("email", "new@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/welcome?success"));

        verify(userService).saveUser(any());
        verify(authService).authenticateUserAndSetSession(eq("newuser"), eq("password123"), any());
    }

    @Test
    void shouldRejectRegistrationWithDuplicateEmail() throws Exception {
        // Given
        UserEntity existing = new UserEntity();
        existing.setEmail("existing@example.com");
        when(userService.findByEmail(anyString())).thenReturn(existing);

        // When & Then
        mockMvc.perform(post("/register/save")
                .param("username", "newuser")
                .param("email", "existing@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?fail"));

        verify(userService, never()).saveUser(any());
    }

    @Test
    void shouldRejectRegistrationWithDuplicateUsername() throws Exception {
        // Given
        when(userService.findByEmail(anyString())).thenReturn(null);

        UserEntity existing = new UserEntity();
        existing.setUsername("existinguser");
        when(userService.findByUsername(anyString())).thenReturn(existing);

        // When & Then
        mockMvc.perform(post("/register/save")
                .param("username", "existinguser")
                .param("email", "new@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?fail"));

        verify(userService, never()).saveUser(any());
    }

    // ==================== GET /login ====================

    @Test
    void shouldShowLoginPage() throws Exception {
        // /login is handled by Spring Security form login — returns 200 with generated
        // page
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    // ==================== GET /welcome ====================

    @Test
    @WithMockUser
    void shouldShowWelcomePage() throws Exception {
        mockMvc.perform(get("/welcome"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    @WithMockUser
    void shouldShowWelcomeWithSuccessMessage() throws Exception {
        mockMvc.perform(get("/welcome").param("success", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attributeExists("successMessage"));
    }
}
