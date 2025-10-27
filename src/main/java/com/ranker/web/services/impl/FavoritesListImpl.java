package com.ranker.web.services.impl;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.services.FavoritesListService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


// Implements actions defined in the Service
public class FavoritesListImpl implements FavoritesListService {

    private FavoritesListRepository favoritesListRepository;

    @Autowired
    public FavoritesListImpl(FavoritesListRepository favoritesListRepository) {
        this.favoritesListRepository = favoritesListRepository;
    }


    @Override
    public List<FavoritesListDTO> findAllLists() {

        List<FavoritesList> lists = favoritesListRepository.findAll();  // Put all the lists into a List<>

        // Convert each list into a DTO
        return lists.stream()
                .map(this::mapToFavoritesListDTO)
                .collect(Collectors.toList());
    }


    // MAPPER -> convert lists into list DTOs
    private FavoritesListDTO mapToFavoritesListDTO(FavoritesList list) {

        return FavoritesListDTO.builder()
                .favoritesListId(list.getFavoritesListId())
                .listName(list.getListName())
                .username(list.getUser().getUsername())
                .isRanked(list.isRanked())
                .build();
    }

}
