package com.ranker.web.mappers;

import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.Item;

public class ItemMapper {


    // MAPPER -> converted DTOs into entities for the DB
    public static Item mapToItem(ItemDTO itemDTO) {

        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .position(itemDTO.getPosition())
                .favoritesList(itemDTO.getFavoritesList())
                .build();
    }
}
