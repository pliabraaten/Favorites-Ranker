package com.ranker.web.controllers;

import com.ranker.web.dto.RankRequestDTO;
import com.ranker.web.dto.UpdateListNameRequestDTO;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController  // Only returning ResponseEntity not views
@RequestMapping("/api/lists")
public class FavoritesListApiController {

    private FavoritesListService favoritesListService;
    private ItemService itemService;

    @Autowired
    public FavoritesListApiController(FavoritesListService favoritesListService, ItemService itemService) {
        this.favoritesListService = favoritesListService;
        this.itemService = itemService;
    }


    // SAVE RANKED ORDER FROM USER INPUT
    @PostMapping("/{listId}/save-rankings")
    public ResponseEntity<Void> saveRankings(
            @PathVariable long listId,
            @RequestBody RankRequestDTO requestDTO) {

        itemService.updatePositions(listId, requestDTO.getItems());

        return ResponseEntity.ok().build();
    }


    // SAVE CHANGES TO LIST NAME
    @PatchMapping("/{listId}")  // Patch -> partial update
    public ResponseEntity<Void> updateListName(
            @PathVariable Long listId,
            @RequestBody UpdateListNameRequestDTO requestDTO) {

        if (requestDTO.getListName() == null || requestDTO.getListName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        favoritesListService.updateListName(listId, requestDTO.getListName().trim());

        return ResponseEntity.ok().build();
    }
}
