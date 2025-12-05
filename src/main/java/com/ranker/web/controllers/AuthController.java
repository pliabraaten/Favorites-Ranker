package com.ranker.web.controllers;


import com.ranker.web.dto.RegistrationDTO;
import com.ranker.web.models.UserEntity;
import com.ranker.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    private UserService userService;


    // Allows auto-login directly after registering
    @Autowired
    private AuthenticationManager authenticationManager;


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
    public String register(@Valid @ModelAttribute("user") RegistrationDTO registrationDTO,
                           BindingResult result,
                           Model model) {

        UserEntity existingUserEmail = userService.findByEmail(registrationDTO.getEmail()); // Try to find existing user first

        // If email is already used
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {

            return "redirect:/register?fail";
        }

        // Check if username is already used
        UserEntity existingUserUsername = userService.findByUsername(registrationDTO.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {

            return "redirect:/register?fail";
        }

        if(result.hasErrors()) {
            model.addAttribute("user", registrationDTO);
            return "register";  // Reload page if error
        }

        // Save new user
        userService.saveUser(registrationDTO);


        // AUTO-LOGIN: new user directly after registering
        // Get username and password from the registering user
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    registrationDTO.getUsername(),
                    registrationDTO.getPassword()
                );

        // Give Spring Security username/password to build authenticated object
        Authentication authentication = authenticationManager.authenticate(authToken);
        // Puts authentication into session
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Redirect registered user as authenticated
        return "redirect:/lists?success";
    }


    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }
}
