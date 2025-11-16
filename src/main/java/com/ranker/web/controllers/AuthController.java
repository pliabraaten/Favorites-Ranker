package com.ranker.web.controllers;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {

        RegistrationDTO user = new RegistrationDTO();  // TODO: understand this more
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDTO user,
                           BindingResult result, Model model) {

        UserEntity existingUserEmail = userService.findByEmail(user.getEmail()); // Try to find existing user first

        // If no email address, then check the field, then check if empty
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {

            result.rejectValue("email", "There is already a user with this email/username");
        }

        // Check if username is already used
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {

            result.rejectValue("email", "There is already a user with this email/username");
        }

        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";  // Reload page if error
        }

        // Save if new user
        userService.saveUser(user);
        return "redirect:/lists?success";
    }
}
