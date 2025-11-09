package com.ranker.web.mappers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;

import java.util.stream.Collectors;

public class ItemMapper {


    // MAPPER -> convert DB item entities into item DTOs
    public static ItemDTO mapToItemDTO(Item item) {

        ItemDTO itemDTO = ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .position(item.getPosition())
                .favoritesList(item.getFavoritesList())
                .build();

        return itemDTO;
    }

    // MAPPER -> converted DTOs into entities for the DB
    public static Item mapToItemEntity(ItemDTO itemDTO) {

        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .position(itemDTO.getPosition())
                .favoritesList(itemDTO.getFavoritesList())
                .build();
    }
}
