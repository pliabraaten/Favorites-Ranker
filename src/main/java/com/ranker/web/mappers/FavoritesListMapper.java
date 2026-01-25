package com.ranker.web.mappers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;

import java.util.stream.Collectors;

import static com.ranker.web.mappers.ItemMapper.mapToItemDTO;

public class FavoritesListMapper {



    // MAPPER -> convert DB list entities into list DTOs
    public static FavoritesListDTO mapToFavoritesListDTO(FavoritesList list) {

        FavoritesListDTO listDTO = FavoritesListDTO.builder()
                .id(list.getId())
                .listName(list.getListName())
                .items(list.getItems().stream().map((item) -> mapToItemDTO(item)).collect(Collectors.toList()))
                .user(list.getUser())
                .sortedCount(list.getSortedCount())
                .build();

        return listDTO;
    }


    // MAPPER -> converted DTOs into entities for the DB
    public static FavoritesList mapToListEntity(FavoritesListDTO listDTO) {

        FavoritesList listEntity = FavoritesList.builder()
                .id(listDTO.getId())
                .listName(listDTO.getListName())
                .sortedCount(listDTO.getSortedCount())
//                .user(listDTO.getUser())
                .build();

        return listEntity;
    }
}
