package com.ranker.web.models;

import jakarta.persistence.*;


@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne  // RankedList contains multiple items
    @JoinColumn(name = "rankedList_id")  // Explicitly name the column
    private RankedList rankedList;

    private String itemName;
    private int position;

}
