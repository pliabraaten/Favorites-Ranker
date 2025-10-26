package com.ranker.web.models;

import jakarta.persistence.*;


@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne  // RankedList contains multiple items
    @JoinColumn(name = "FavoritesList_id")  // Explicitly name the column
    private FavoritesList FavoritesList;

    private String itemName;
    private int position;

}
