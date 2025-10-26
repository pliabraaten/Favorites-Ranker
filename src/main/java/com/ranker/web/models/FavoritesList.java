package com.ranker.web.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private User user;

    private String listName;
    private boolean isRanked;

}
