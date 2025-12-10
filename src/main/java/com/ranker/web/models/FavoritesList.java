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
    private Long id;

    @ManyToOne  // A user can have many lists
    @JoinColumn(name = "user_id", nullable = false)  // Explicitly name the column
    private UserEntity user;

    private String listName;
    private boolean isRanked = false;

    @OneToMany(mappedBy = "favoritesList", cascade = CascadeType.REMOVE)
    @OrderBy("position ASC")  // Sorts by the position value in the DB
    private List<Item> items = new ArrayList<>();

}
