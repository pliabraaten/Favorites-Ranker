package com.ranker.web.mappers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;

public class FavoritesListMapper {



    // MAPPER -> convert DB list entities into list DTOs
    public static FavoritesListDTO mapToFavoritesListDTO(FavoritesList list) {

        FavoritesListDTO listDTO = FavoritesListDTO.builder()
                .favoritesListId(list.getFavoritesListId())
                .listName(list.getListName())
//                .username(list.getUser().getUsername())  // FIXME
                .build();

        return listDTO;
    }


    // MAPPER -> converted DTOs into entities for the DB
    public static FavoritesList mapToListEntity(FavoritesListDTO listDTO) {

        FavoritesList listEntity = FavoritesList.builder()
                .favoritesListId(listDTO.getFavoritesListId())
                .listName(listDTO.getListName())
//                .username(listDTO.getUser().getUsername())  // FIXME
                .build();

        return listEntity;
    }
}
