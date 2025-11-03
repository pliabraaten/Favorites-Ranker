package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FavoritesListService {

    // Defines which actions can happen
    List<FavoritesListDTO> findAllLists();

    // Controller works with DTO for encapsulation/security
    FavoritesListDTO saveList(FavoritesListDTO listDTO);

    FavoritesListDTO findListById(long listId);

    void updateList(FavoritesListDTO listDTO);


}
