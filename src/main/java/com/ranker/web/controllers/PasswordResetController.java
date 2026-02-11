package com.ranker.web.controllers;

import com.ranker.web.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class PasswordResetController {

    private PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }


    // Show "Forgot Password" form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }


    // Handle forgot password form submission
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        RedirectAttributes redirectAttributes) {
        passwordResetService.createPasswordResetToken(email);

        // Always show success message (security - don't reveal if email exists)
        redirectAttributes.addFlashAttribute("message",
                "If that email exists in our system, a password reset link has been sent.");

        return "redirect:/login";
    }


    // Show "Reset Password" form (from email link)
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        if (!passwordResetService.validateToken(token)) {
            model.addAttribute("error", "Invalid or expired reset link.");
            return "reset-password-error";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }


    // Handle password reset form submission
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String password,
                                       @RequestParam String confirmPassword,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords don't match");
            model.addAttribute("token", token);
            return "reset-password";
        }

        // Validate password length
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            model.addAttribute("token", token);
            return "reset-password";
        }

        try {
            passwordResetService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("message",
                    "Password reset successfully! Please login with your new password.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password";
        }
    }
}
