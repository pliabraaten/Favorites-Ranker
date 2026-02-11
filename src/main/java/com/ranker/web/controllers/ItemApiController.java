package com.ranker.web.controllers;

import com.ranker.web.dto.UpdateItemNameRequestDTO;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController  // Only returning ResponseEntity not views
@RequestMapping("/api/items")
public class ItemApiController {

    private FavoritesListService favoritesListService;
    private ItemService itemService;

    @Autowired
    public ItemApiController(FavoritesListService favoritesListService, ItemService itemService) {
        this.favoritesListService = favoritesListService;
        this.itemService = itemService;
    }


    // SAVE CHANGES TO LIST NAME
    @PatchMapping("/{itemId}")  // Patch -> partial update
    public ResponseEntity<Void> updateItemName(
            @PathVariable Long itemId,
            @RequestBody UpdateItemNameRequestDTO requestDTO) {

        if (requestDTO.getItemName() == null || requestDTO.getItemName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        itemService.updateItemName(itemId, requestDTO.getItemName().trim());

        return ResponseEntity.ok().build();
    }

}
