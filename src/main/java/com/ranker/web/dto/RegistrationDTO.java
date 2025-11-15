package com.ranker.web.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


// TODO: add input validation
@Data
public class RegistrationDTO {

    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

}
