package com.ranker.web.controllers;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.services.FavoritesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class FavoritesListController {

    private FavoritesListService favoritesListService;

    @Autowired
    public FavoritesListController(FavoritesListService favoritesListService) {
        this.favoritesListService = favoritesListService;
    }


    @GetMapping("/")
    public String listLists(Model model) {
        List<FavoritesListDTO> lists = favoritesListService.findAllLists();

        model.addAttribute("lists", lists);

        return "lists-list";
    }
}
