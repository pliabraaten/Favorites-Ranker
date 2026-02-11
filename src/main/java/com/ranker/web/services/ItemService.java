package com.ranker.web.services;


import com.ranker.web.dto.ItemDTO;
import com.ranker.web.dto.ItemPositionDTO;

import java.util.List;


public interface ItemService {

    int saveItem(Long itemId, String itemNamesInput);

    ItemDTO findItemById(long itemId);

    void deleteItem(Long itemId);

    void repositionItem(Long itemId, String direction);

    List<ItemDTO> getItemsByListId(long listId);  // Sorted in Repo by position value

    void updatePositions(long listId, List<ItemPositionDTO> items);

    void updateItemName(Long itemId, String trim);

    Long getListIdByItemId(Long itemId);
}
