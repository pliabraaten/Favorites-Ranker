package com.ranker.web.controllers;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.Item;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
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
    public String saveItem(@PathVariable("listId") Long listId, @ModelAttribute("item")ItemDTO itemDTO, Model model) {

        itemService.saveItem(listId, itemDTO);

        return "redirect:/lists/" + listId;  // Send user back to details html for this list
    }


    // OPEN ITEM EDIT FORM FOR SPECIFIC Item
    @GetMapping("/items/{itemId}/edit")
    public String editItemForm(@PathVariable("itemId") long itemId, Model model) {  // PathVariable annotation takes template variable and uses it for the method parameter

        ItemDTO itemDTO = itemService.findItemById(itemId);  // Pull item via services and set it to DTO

        model.addAttribute("item", itemDTO);  // To edit, first pull the entity->DTO

        return "items-edit";
    }

    // SAVE EDITED ITEM
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId,
                             @ModelAttribute("item") ItemDTO itemDTO,
                             Model model, BindingResult result) {

        if(result.hasErrors()) {  // Return to page if there is an error editing the item
            model.addAttribute("item", itemDTO);
            return "items-edit";  // Re-render NOT a redirect reload with existing values
        }

        // Get list from existing item
        ItemDTO existingItem = itemService.findItemById(itemId);

        // Keep its list association
        itemDTO.setFavoritesList(existingItem.getFavoritesList());
        itemDTO.setId(itemId);

        itemService.updateItem(itemDTO);

        return "redirect:/lists/" + existingItem.getFavoritesList().getId();
    }


    // Change the ranking of items via UP/DOWN buttons
    @PostMapping("/items/{itemId}/reposition")
    public String repositionItem(@PathVariable("itemId") Long itemId,
                                 @RequestParam String direction) {

        itemService.reposition(itemId, direction);

        return "redirect:/items/" + itemId + "/edit";  // Reload page
    }


    // FIXME: ALLOW USER TO EDIT/DELETE ALL LIST ITEMS ON ONE PAGE RATHER THAN CLICKING INTO THE ITEM EDIT PAGE EACH TIME
    // DELETE ITEM FROM LIST
    @PostMapping("/items/{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId) {

        // Get list of the item about to be deleted to redirect user back to the list after deletion
        Long listId = itemService.findItemById(itemId).getFavoritesList().getId();

        itemService.deleteItem(itemId);

        return "redirect:/lists/" + listId;
    }


}
