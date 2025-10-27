package com.ranker.web.dto;


import com.ranker.web.models.User;
import lombok.Builder;
import lombok.Data;


// Sends data to controller/view without exposing the database entity
@Data
@Builder
public class FavoritesListDTO {

    private Long favoritesListId;
    private String username;
    private String listName;
    private boolean isRanked;

}
