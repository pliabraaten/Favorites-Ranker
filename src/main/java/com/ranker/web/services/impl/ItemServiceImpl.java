package com.ranker.web.services.impl;


import com.ranker.web.dto.ItemDTO;
import com.ranker.web.dto.ItemPositionDTO;
import com.ranker.web.mappers.ItemMapper;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.ItemRepository;
import com.ranker.web.services.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        FavoritesList foundList = listRepository.findById(listId)  // Find list id
            .orElseThrow(() -> new EntityNotFoundException("List not found"));

        Item item = mapToItemEntity(itemDTO);  // Map DTO to entity
        item.setFavoritesList(foundList);  // Set list object to the list found by ID

        // Count existing items +1 and set position in the list
        int nextPosition = itemRepository.countByFavoritesListId(listId) + 1;  // COUNT # of items WHERE FavoritesListId = listId
        item.setPosition(nextPosition);

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


    // GET ALL ITEMS IN A LIST
    @Override
    public List<ItemDTO> getItemsByListId(long listId) {

        List<Item> items = itemRepository.findByFavoritesListIdOrderByPositionAsc(listId);

        // Convert entire list to DTOs
        return items.stream()
                .map(ItemMapper::mapToItemDTO)  // Use your existing mapper
                .collect(Collectors.toList());
    }


    // UPDATE ITEM POSITIONS AFTER PAIRWISE COMPARISON RANKING: ItemPositionDTOs are passed in from the JSON
    @Override
    @Transactional
    public void updatePositions(long listId, List<ItemPositionDTO> items) {

        for (ItemPositionDTO dto : items) {  // For each item/position in the DTO list

            // Find each item entity in the db by id
            Item item = itemRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));

            // Security check to make sure the item belongs to this list
            if (!item.getFavoritesList().getId().equals(listId)) {
                throw new SecurityException("Item " + dto.getId() + " does not belong to list " + listId);
            }

            item.setPosition(dto.getPosition());  // Update item's position to with the DTO position
            itemRepository.save(item);
        }
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
    @Transactional
    public void moveItemUp(Long itemId) {

        // Get item and its position
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Current item not found"));

        int itemPosition = item.getPosition();

        // Get list of this item
        FavoritesList list = item.getFavoritesList();

        // Find prior higher ranked item within the same list
        Optional<Item> priorItemOptional = itemRepository.findByFavoritesListIdAndPosition(list.getId(), itemPosition - 1);

        // Swap positions if prior item is found
        if (priorItemOptional.isPresent()) {  // If the optional wrapper returns an item (nextItem is not the last position)
            Item priorItem = priorItemOptional.get();  // Set it as a variable to strip the optional wrapper for normal handling

            // Swap
            item.setPosition(itemPosition + 1);
            priorItem.setPosition(itemPosition);
        }
    }


    // Swap item's position with the lower ranked item
    @Transactional
    public void moveItemDown(Long itemId) {

        // Get item and its position
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Current item not found"));

        int itemPosition = item.getPosition();

        // Get list of this item
        FavoritesList list = item.getFavoritesList();

        // Find next lower ranked item within the same list
        Optional<Item> nextItemOptional = itemRepository.findByFavoritesListIdAndPosition(list.getId(), itemPosition + 1);

        // Swap positions if not last
        if (nextItemOptional.isPresent()) {  // If the optional wrapper returns an item (nextItem is not the last position)
            Item nextItem = nextItemOptional.get();  // Set it as a variable to strip the optional wrapper for normal handling

            // Swap
            item.setPosition(itemPosition + 1);
            nextItem.setPosition(itemPosition);
        }

    }
}