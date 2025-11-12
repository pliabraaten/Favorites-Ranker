package com.ranker.web.services;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;


public interface ItemService {

    void saveItem(Long itemId, ItemDTO itemDTO);

    ItemDTO findItemById(long itemId);

    void updateItem(ItemDTO itemDTO);
}
