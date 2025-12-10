package com.ranker.web.services.impl;


import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.ItemRepository;
import com.ranker.web.services.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ranker.web.mappers.ItemMapper.mapToItemDTO;
import static com.ranker.web.mappers.ItemMapper.mapToItemEntity;


@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private FavoritesListRepository listRepository;


    // Constructor
    public ItemServiceImpl(ItemRepository itemRepository, FavoritesListRepository listRepository) {
        this.itemRepository = itemRepository;
        this.listRepository = listRepository;
    }


    // Create new item and tie it to a list
    @Override
    public void saveItem(Long listId, ItemDTO itemDTO) {  // ItemDTO is created by user -> mapped to DB entity for saving

        FavoritesList foundList = listRepository.findById(listId).get();  // Find list id

        Item item = mapToItemEntity(itemDTO);  // Map DTO to entity
        item.setFavoritesList(foundList);  // Set list object to the list found by ID

        itemRepository.save(item);
    }


    // Find a specific item in a list
    @Override
    public ItemDTO findItemById(long itemId) {

        // Pull item ID from DB via Repository
        Item itemEntity = itemRepository.findById(itemId).get();

        // Map entity to DTO and return
        return mapToItemDTO(itemEntity);
    }

    @Override
    public void updateItem(ItemDTO itemDTO) {

        Item itemEntity = mapToItemEntity(itemDTO);

        itemRepository.save(itemEntity);
    }

    @Override
    public void deleteItem(Long itemId) {

        itemRepository.deleteById(itemId);
    }


    @Override
    @Transactional  // Treat whole method as one DB transaction
    public void reposition(Long itemId, String direction) {

        if (direction.equals("Up")) {
            moveItemUp(itemId);
        } else if (direction.equals("Down")) {
            moveItemDown(itemId);
        }

    }

    // Swap item's position with the higher ranked item
    public void moveItemUp(Long itemId) {

        // Get item and its position
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Current item not found"));

        int itemPosition = item.getPosition();

        // Get list of this item
        FavoritesList list = item.getFavoritesList();

        // Find prior higher ranked item within the same list
        Item priorItem = itemRepository.findByFavoritesListFavoritesListIdAndPosition(list.getFavoritesListId(), itemPosition - 1)
                .orElseThrow(() -> new EntityNotFoundException("Prior item not found"));

        // Swap positions
        item.setPosition(itemPosition - 1);
        priorItem.setPosition(priorItem.getPosition() + 1);
    }


    // Swap item's position with the lower ranked item
    public void moveItemDown(Long itemId) {

        // Get item and its position
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Current item not found"));

        int itemPosition = item.getPosition();

        // Get list of this item
        FavoritesList list = item.getFavoritesList();

        // Find next lower ranked item within the same list
        Item nextItem = itemRepository.findByFavoritesListFavoritesListIdAndPosition(list.getFavoritesListId(), itemPosition + 1)
                .orElseThrow(() -> new EntityNotFoundException("Next item not found"));

        // Swap positions
        item.setPosition(itemPosition + 1);
        nextItem.setPosition(nextItem.getPosition() - 1);
    }
}