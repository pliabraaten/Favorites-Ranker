package com.ranker.web.dto;

import com.ranker.web.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;


// Sends data to controller/view without exposing the database entity
@Data
@Builder
public class FavoritesListDTO {

    private Long id;

    @NotEmpty(message = "List name should not be empty")  // Input validation
    private String listName;

    private int sortedCount;

    private UserEntity user;

    private List<ItemDTO> items;
}
