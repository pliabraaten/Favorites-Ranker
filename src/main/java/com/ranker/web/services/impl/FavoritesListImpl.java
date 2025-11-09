package com.ranker.web.services.impl;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.services.FavoritesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ranker.web.mappers.FavoritesListMapper.mapToFavoritesListDTO;
import static com.ranker.web.mappers.FavoritesListMapper.mapToListEntity;


// Implements actions defined in the Service
@Service
public class FavoritesListImpl implements FavoritesListService {

    private FavoritesListRepository favoritesListRepository;

    @Autowired
    public FavoritesListImpl(FavoritesListRepository favoritesListRepository) {
        this.favoritesListRepository = favoritesListRepository;
    }


    @Override
    public List<FavoritesListDTO> findAllLists() {

        List<FavoritesList> lists = favoritesListRepository.findAll();  // Put all the lists into a List<>

        // Fetch lists from DB and convert them into a DTO
        return lists.stream()
                .map(favoritesList -> mapToFavoritesListDTO(favoritesList))
                .collect(Collectors.toList());
    }

    @Override
    public FavoritesListDTO saveList(FavoritesListDTO listDTO) {

        // Converts the DTO to a DB entity
        FavoritesList listEntity = mapToListEntity(listDTO);  // Mapper method below: DTO -> Entity

        listEntity.setRanked(false); // Internal flag, default to false when saved

        // Save the entity
        FavoritesList savedEntity = favoritesListRepository.save(listEntity);    // JPA automatically provides .save()

        // Convert entity back to DTO and return
        return mapToFavoritesListDTO(savedEntity);
    }

    @Override
    public FavoritesListDTO findListById(long listId) {

        // Pull list ID from DB via Repository
        FavoritesList listEntity = favoritesListRepository.findById(listId).get();

        // Map entity to DTO and return
        return mapToFavoritesListDTO(listEntity);
    }

    @Override
    public void updateList(FavoritesListDTO listDTO) {

        FavoritesList listEntity = mapToListEntity(listDTO);

        favoritesListRepository.save(listEntity);
    }

    @Override
    public void delete(long listId) {

        favoritesListRepository.deleteById(listId);
    }

    @Override
    public List<FavoritesListDTO> searchLists(String query) {

        List<FavoritesList> lists = favoritesListRepository.searchLists(query);

        return lists.stream().map(favoritesList -> mapToFavoritesListDTO(favoritesList)).collect(Collectors.toList());
    }


}
