package com.ranker.web.controllers;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.services.FavoritesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class FavoritesListController {

    private FavoritesListService favoritesListService;

    @Autowired
    public FavoritesListController(FavoritesListService favoritesListService) {
        this.favoritesListService = favoritesListService;
    }


    // HOME PAGE
    @GetMapping("/")
    public String listLists(Model model) {

        List<FavoritesListDTO> lists = favoritesListService.findAllLists();

        model.addAttribute("lists", lists);

        return "lists-list";
    }


    // DISPLAY FORM NEW LIST
    @GetMapping("/new")
    public String createListForm(Model model) {

        FavoritesList newlist = new FavoritesList();  // Create new list
        model.addAttribute("list", newlist);  // Add list to the model
        return "lists-create";
    }

    // CREATE NEW LIST WITH POST METHOD
    @PostMapping("/new")
    public String saveList(@ModelAttribute("list") FavoritesListDTO listDTO) {  //

        // Save new list via the service instance and then go back to home page
        favoritesListService.saveList(listDTO);
        return "redirect:/";

    }
}
