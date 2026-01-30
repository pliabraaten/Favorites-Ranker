package com.ranker.web.services.impl;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.security.SecurityUtil;
import com.ranker.web.services.FavoritesListService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private UserRepository userRepository;


    @Autowired
    public FavoritesListImpl(FavoritesListRepository favoritesListRepository, UserRepository userRepository) {
        this.favoritesListRepository = favoritesListRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<FavoritesListDTO> findUserLists() {

        // Get logged-in user from session
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        List<FavoritesList> lists = favoritesListRepository.findByUserId(user.getId());  // Put all the lists into a List<>

        // Fetch lists from DB and convert them into a DTO
        return lists.stream()
                .map(favoritesList -> mapToFavoritesListDTO(favoritesList))
                .collect(Collectors.toList());
    }


    @Override
    public Long saveList(FavoritesListDTO listDTO) {

        // Get logged-in user from session
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        // Converts the DTO to a DB entity
        FavoritesList listEntity = mapToListEntity(listDTO);  // Mapper method below: DTO -> Entity
        listEntity.setSortedCount(listDTO.getSortedCount() != null ? listDTO.getSortedCount() : 0);  // Set sortedCount to 0 if null
        listEntity.setRanked(false); // Internal flag, default to false when saved

        // Tie user to the list
        listEntity.setUser(user);

        // Save the entity
        FavoritesList savedEntity = favoritesListRepository.save(listEntity);    // JPA automatically provides .save()

        // Convert entity back to DTO and return
        return savedEntity.getId();   // error when using mapToFavoritesListDTO(savedEntity) -> because no items are saved at this time
    }


    @Override
    public FavoritesListDTO findListById(long listId) {

        // Pull list ID from DB via Repository
        FavoritesList listEntity = favoritesListRepository.findById(listId).get();

        // Map entity to DTO and return
        return mapToFavoritesListDTO(listEntity);
    }


    @Override
    public void updateListName(Long listId, String newName) {

        FavoritesList list = favoritesListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        list.setListName(newName);
        favoritesListRepository.save(list);  // FIXME: is this redundant?
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


    @Override
    @Transactional
    public void updateSortedCount(Long listId, int sortedCount) {
        FavoritesList list = favoritesListRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        list.setSortedCount(sortedCount);
        favoritesListRepository.save(list);  // FIXME: is this redundant?
    }


    @Transactional
    @Override
    public void decrementSortedCount(Long listId) {
        FavoritesList list = favoritesListRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        int currentCount = list.getSortedCount();

        if (currentCount > 0) {
            list.setSortedCount(currentCount - 1);
        }
    }


    @Override
    @Transactional
    public void setRankedFlag(Long listId, boolean isRanked) {

        FavoritesList list = favoritesListRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        list.setRanked(isRanked);
        favoritesListRepository.save(list);  // FIXME: is this redundant?
    }
}