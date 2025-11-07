package com.ranker.web.controllers;


import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.services.ItemService;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ItemController {

    private ItemService itemService;


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    // SHOW NEW ITEM CREATION FORM
    @GetMapping("/items/{listId}/new")
    public String createItemForm(@PathVariable("listId") Long listId, Model model) {

        Item item = new Item();

        model.addAttribute("listId", listId);
        model.addAttribute("item", item);

        return "items-create";
    }

    // CREATE NEW ITEM
    @PostMapping("/items/{listId}")
    public String saveItem(@PathVariable("listId") Long listId, @ModelAttribute("item")ItemDTO itemDTO, Model model) {

        itemService.saveItem(listId, itemDTO);

        return "redirect:/" + listId;  // Send user back to details html for this list
    }


}
