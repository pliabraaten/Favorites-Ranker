package com.ranker.web.services;


import com.ranker.web.dto.ItemDTO;
import com.ranker.web.dto.ItemPositionDTO;
import com.ranker.web.models.Item;

import java.util.List;


public interface ItemService {

    void saveItem(Long itemId, ItemDTO itemDTO);

    ItemDTO findItemById(long itemId);

    void updateItem(ItemDTO itemDTO);

    void deleteItem(Long itemId);

    void reposition(Long itemId, String direction);

    List<ItemDTO> getItemsByListId(long listId);  // Sorted in Repo by position value

    void updatePositions(long listId, List<ItemPositionDTO> items);

}
