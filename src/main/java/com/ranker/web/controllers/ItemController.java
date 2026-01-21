package com.ranker.web.controllers;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class ItemController {

    private ItemService itemService;
    private FavoritesListService favoritesListService;


    public ItemController(ItemService itemService, FavoritesListService favoritesListService) {

        this.itemService = itemService;
        this.favoritesListService = favoritesListService;
    }


    // SHOW NEW ITEM CREATION FORM
    @GetMapping("/items/{listId}/new")
    public String createItemForm(@PathVariable("listId") Long listId, Model model) {

        Item item = new Item();

        FavoritesListDTO list = favoritesListService.findListById(listId);

        model.addAttribute("list", list);
        model.addAttribute("item", item);

        return "items-create";
    }


    // CREATE NEW ITEM
    @PostMapping("/items/{listId}")
    public String saveItem(@PathVariable("listId") Long listId,
                           @Valid @ModelAttribute("item")ItemDTO itemDTO,
                           BindingResult result,
                           Model model) {

//        if (result.hasErrors()) {
//            // Stay on the same page and show validation errors
//            model.addAttribute("list", favoritesListService.findListById(listId));
//            return "items-create";
//        }

        itemService.saveItem(listId, itemDTO);

        return "redirect:/items/" + listId + "/new";  // Refresh page for the user to add more items
    }


    // Change the ranking of items via UP/DOWN buttons
    @PostMapping("/items/{itemId}/reposition")
    public String repositionItem(@PathVariable Long itemId,
                                 @RequestParam String direction) {

        Long listId = itemService.getListIdByItemId(itemId);

        itemService.repositionItem(itemId, direction);

        System.out.println("Controller hit");

        return "redirect:/lists/" + listId;  // Refresh page for the new order of items
    }


//    // FIXME: ALLOW USER TO EDIT/DELETE ALL LIST ITEMS ON ONE PAGE RATHER THAN CLICKING INTO THE ITEM EDIT PAGE EACH TIME
//    // DELETE ITEM FROM LIST
//    @PostMapping("/items/{itemId}/delete")
//    public String deleteItem(@PathVariable("itemId") Long itemId) {
//
//        // Get list of the item about to be deleted to redirect user back to the list after deletion
//        Long listId = itemService.findItemById(itemId).getListId();
//
//        itemService.deleteItem(itemId);
//
//        return "redirect:/lists/" + listId;
//    }


}
