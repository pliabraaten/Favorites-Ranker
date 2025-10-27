package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface FavoritesListService {

    // Defines which actions can happen
    List<FavoritesListDTO> findAllLists();

}
