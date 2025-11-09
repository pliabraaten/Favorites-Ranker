package com.ranker.web.dto;

import com.ranker.web.models.Item;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


// Sends data to controller/view without exposing the database entity
@Data
@Builder
public class FavoritesListDTO {

    private Long favoritesListId;

    @NotEmpty(message = "List name should not be empty")  // Input validation
    private String listName;

    private List<ItemDTO> items;
}
