package com.ranker.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;


// Sends data to controller/view without exposing the database entity
@Data
@Builder
public class FavoritesListDTO {

    private Long favoritesListId;

    @NotEmpty(message = "List name should not be empty")  // Input validation
    private String listName;

}
