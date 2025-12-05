package com.ranker.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


// TODO: add input validation
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationDTO {

    private Long id;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Email should not be empty")
//    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password should not be empty")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
