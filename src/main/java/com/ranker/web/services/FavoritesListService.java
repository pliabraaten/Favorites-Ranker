package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface FavoritesListService {

    // Defines which actions can happen
    List<FavoritesListDTO> findUserLists();

    // Controller works with DTO for encapsulation/security
    Long saveList(FavoritesListDTO listDTO);

    FavoritesListDTO findListById(long listId);

    void updateListName(Long listId, String newName);

    void delete(long listId);

    List<FavoritesListDTO> searchLists(String query);

//    Optional<FavoritesList> getListById(Long );
}


