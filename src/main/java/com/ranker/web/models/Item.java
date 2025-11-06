package com.ranker.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int position;

    @ManyToOne  // RankedList contains multiple items
    @JoinColumn(name = "FavoritesList_id", nullable = false)  // Explicitly name the column; item has to be tied to list
    private FavoritesList favoritesList;

}
