package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import jakarta.validation.constraints.Min;

import java.util.List;


public interface FavoritesListService {

    // Defines which actions can happen
    List<FavoritesListDTO> findUserLists();

    // Controller works with DTO for encapsulation/security
    Long saveList(FavoritesListDTO listDTO);

    FavoritesListDTO findListById(long listId);

    void updateListName(Long listId, String newName);

    void delete(long listId);

    List<FavoritesListDTO> searchLists(String query);

    void updateSortedCount(Long listId, @Min(0) int sortedCount);

    void decrementSortedCount(Long listId);

    void setRankedFlag(Long listId, boolean b);

}


