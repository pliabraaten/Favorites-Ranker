package com.ranker.web.dto;


import lombok.Data;


@Data
public class ItemDTO {

    private Long itemId;
    private com.ranker.web.models.FavoritesList FavoritesList;
    private String itemName;
    private int position;
}
