package com.ranker.web.controllers;

import com.ranker.web.security.CustomUserDetailsService;
import com.ranker.web.security.SecurityConfig;
import com.ranker.web.services.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PasswordResetController.class)
@Import(SecurityConfig.class)
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasswordResetService passwordResetService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ==================== GET /forgot-password ====================

    @Test
    void shouldShowForgotPasswordForm() throws Exception {
        // /forgot-password is permitAll
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"));
    }

    // ==================== POST /forgot-password ====================

    @Test
    void shouldProcessForgotPasswordAndRedirectToLogin() throws Exception {
        // /forgot-password POST is handled by the controller, permitAll via
        // /forgot-password
        mockMvc.perform(post("/forgot-password")
                .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("message"));

        verify(passwordResetService).createPasswordResetToken("test@example.com");
    }

    // ==================== GET /reset-password ====================

    @Test
    void shouldShowResetPasswordFormForValidToken() throws Exception {
        // /reset-password is permitAll
        when(passwordResetService.validateToken("valid-token")).thenReturn(true);

        mockMvc.perform(get("/reset-password")
                .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attribute("token", "valid-token"));
    }

    @Test
    void shouldShowErrorPageForInvalidToken() throws Exception {
        when(passwordResetService.validateToken("invalid-token")).thenReturn(false);

        mockMvc.perform(get("/reset-password")
                .param("token", "invalid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-error"))
                .andExpect(model().attributeExists("error"));
    }

    // ==================== POST /reset-password ====================

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {
        mockMvc.perform(post("/reset-password")
                .param("token", "valid-token")
                .param("password", "newpassword")
                .param("confirmPassword", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("message"));

        verify(passwordResetService).resetPassword("valid-token", "newpassword");
    }

    @Test
    void shouldRejectMismatchedPasswords() throws Exception {
        mockMvc.perform(post("/reset-password")
                .param("token", "valid-token")
                .param("password", "password1")
                .param("confirmPassword", "password2"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attribute("error", "Passwords don't match"));

        verify(passwordResetService, never()).resetPassword(anyString(), anyString());
    }

    @Test
    void shouldRejectTooShortPassword() throws Exception {
        mockMvc.perform(post("/reset-password")
                .param("token", "valid-token")
                .param("password", "short")
                .param("confirmPassword", "short"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attribute("error", "Password must be at least 6 characters"));

        verify(passwordResetService, never()).resetPassword(anyString(), anyString());
    }

    @Test
    void shouldHandleResetPasswordServiceException() throws Exception {
        doThrow(new IllegalArgumentException("Token has expired"))
                .when(passwordResetService).resetPassword(anyString(), anyString());

        mockMvc.perform(post("/reset-password")
                .param("token", "expired-token")
                .param("password", "newpassword")
                .param("confirmPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attribute("error", "Token has expired"));
    }
}
