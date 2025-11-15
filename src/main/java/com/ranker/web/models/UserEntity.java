package com.ranker.web.models;


import jakarta.persistence.*;
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
    private String username;
    private String email;
    private String password;

}
