package com.ranker.web.services.impl;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.ItemRepository;
import com.ranker.web.services.ItemService;
import org.springframework.stereotype.Service;

import static com.ranker.web.mappers.FavoritesListMapper.mapToFavoritesListDTO;
import static com.ranker.web.mappers.FavoritesListMapper.mapToListEntity;
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


}
