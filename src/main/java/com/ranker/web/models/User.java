package com.ranker.web.models;


import jakarta.persistence.*;


// TODO: add ability for user to have multiple lists

@Entity
@Table(name = "APP_USER")  // Explicitly named to avoid keyword "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
}
