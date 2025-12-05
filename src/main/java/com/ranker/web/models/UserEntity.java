package com.ranker.web.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// TODO: add ability for user to have multiple lists

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")  // Explicitly named to avoid keyword "user"
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // FIXME: change this to sequence instead of identity?
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
//    @Email(message = "Please provide a valid email")  // FIXME: ADD REQUIREMENTS
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be empty")
//    @Size(min = 6, message = "Password must be at least 6 characters long")  // FIXME: ADD REQUIREMENTS
    private String password;

}
