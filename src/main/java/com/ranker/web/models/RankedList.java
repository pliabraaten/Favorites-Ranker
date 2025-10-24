package com.ranker.web.models;


import jakarta.persistence.*;


@Entity
//@Table(name = "RankedList")
public class RankedList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankedListId;

    @ManyToOne  // A user can have many lists
    @JoinColumn(name = "user_id")  // Explicitly name the column
    private User user;

    private String itemName;  // TODO: add ability for user to have multiple lists
    private int position;

    private boolean isRanked;


}
