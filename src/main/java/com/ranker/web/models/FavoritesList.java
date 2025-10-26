package com.ranker.web.models;


import jakarta.persistence.*;


@Entity
//@Table(name = "RankedList")
public class FavoritesList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankedListId;

    @ManyToOne  // A user can have many lists
    @JoinColumn(name = "user_id")  // Explicitly name the column
    private User user;

    private String listName;
    private boolean isRanked;

}
