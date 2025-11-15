package com.ranker.web.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FavoritesList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoritesListId;

    @ManyToOne  // A user can have many lists
    @JoinColumn(name = "user_id")  // Explicitly name the column
    private UserEntity user;

    private String listName;
    private boolean isRanked = false;

    @OneToMany(mappedBy = "favoritesList", cascade = CascadeType.REMOVE)
    private List<Item> items = new ArrayList<>();

}
