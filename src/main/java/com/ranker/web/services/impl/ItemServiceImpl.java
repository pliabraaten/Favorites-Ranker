package com.ranker.web.services.impl;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.ItemRepository;
import com.ranker.web.services.ItemService;
import org.springframework.stereotype.Service;


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

        Item item = mapToItem(itemDTO);  // Map DTO to entity
        item.setFavoritesList(foundList);  // Set list object to the list found by ID

        itemRepository.save(item);
    }


    // MAPPER -> converted DTOs into entities for the DB
    private Item mapToItem(ItemDTO itemDTO) {

        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .position(itemDTO.getPosition())
                .favoritesList(itemDTO.getFavoritesList())
                .build();
    }
}
