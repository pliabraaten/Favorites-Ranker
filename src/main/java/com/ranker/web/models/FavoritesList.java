package com.ranker.web.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
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

    // Items that have been pairwise ranked
    private int sortedCount = 0;  // Used to track new added items that haven't been ranked yet

    @OneToMany(mappedBy = "favoritesList", cascade = CascadeType.REMOVE)
    @OrderBy("position ASC")  // Sorts by the position value in the DB
    private List<Item> items = new ArrayList<>();

}
