package com.ranker.web.dto;


import com.ranker.web.models.User;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class FavoritesListDTO {

    private Long favoritesListId;
    private User user;
    private String listName;
    private boolean isRanked;

}
