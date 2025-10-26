package com.ranker.web.dto;


import com.ranker.web.models.User;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class FavoritesListDTO {

    private Long rankedListId;
    private User user;
    private String listName;
    private boolean isRanked;

}
