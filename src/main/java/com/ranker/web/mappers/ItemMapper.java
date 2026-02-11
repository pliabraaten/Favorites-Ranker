package com.ranker.web.mappers;

import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.Item;


public class ItemMapper {


    // MAPPER -> convert DB item entities into item DTOs
    public static ItemDTO mapToItemDTO(Item item) {

        ItemDTO itemDTO = ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .position(item.getPosition())
                .listId(item.getFavoritesList() != null ?
                        item.getFavoritesList().getId() : null)
                .build();

        return itemDTO;
    }

    // MAPPER -> converted DTOs into entities for the DB
    public static Item mapToItemEntity(ItemDTO itemDTO) {

        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .position(itemDTO.getPosition())
                .build();
    }
}
