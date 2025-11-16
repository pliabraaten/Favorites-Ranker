package com.ranker.web.controllers;


import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.services.FavoritesListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO: REORGANIZE THESE MAPPINGS
@Controller
public class FavoritesListController {

    private FavoritesListService favoritesListService;

    @Autowired
    public FavoritesListController(FavoritesListService favoritesListService) {
        this.favoritesListService = favoritesListService;
    }


    //fixme: ADD SEPARATE HOMEPAGE, AND PUT LISTS-LIST AS /LISTS ENDPOINT
    // HOME PAGE
    @GetMapping("/lists")
    public String listLists(Model model) {

        List<FavoritesListDTO> lists = favoritesListService.findAllLists();

        model.addAttribute("lists", lists);

        return "lists-list";
    }


    // DISPLAY FORM NEW LIST
    @GetMapping("/lists/new")
    public String createListForm(Model model) {

        FavoritesList newlist = new FavoritesList();  // Create new list
        model.addAttribute("list", newlist);  // Add list to the model
        return "lists-create";
    }

    // CREATE NEW LIST WITH POST METHOD
    @PostMapping("/lists/new")
    public String saveList(@Valid @ModelAttribute("list") FavoritesListDTO listDTO, BindingResult result, Model model) {  // TODO: ADD EXPLANATION ON BINDING RESULT

        if(result.hasErrors()) {  // Return to page if there is an error creating the list
            model.addAttribute("list", listDTO);  // Re-render the form with previously inputted values
            return "lists-create";  // No redirect here in order to re-render the form
        }

        // Save new list via the service instance and then go back to home page
        favoritesListService.saveList(listDTO);

        return "redirect:/lists";
    }

    // OPEN LIST EDIT FORM FOR SPECIFIC LIST
    @GetMapping("/lists/{favoritesListId}/edit")
    public String editListForm(@PathVariable("favoritesListId") long favoritesListId, Model model) {  // PathVariable annotation takes template variable and uses it for the method parameter

        FavoritesListDTO listDTO = favoritesListService.findListById(favoritesListId);  // Pull list via services and set it to DTO

        model.addAttribute("list", listDTO);  // To edit, first pull the entity->DTO

        return "lists-edit";
    }

    // SAVE CHANGES TO LIST
    @PostMapping("/lists/{favoritesListId}/edit")  // URL has to match @PathVariable below
    public String updateList(@PathVariable("favoritesListId") Long listId,
                             @Valid @ModelAttribute("list") FavoritesListDTO listDTO,  // TODO: ADD EXPLANATION FOR @MODEL ATTRIBUTE
                             BindingResult result, Model model) {  // If validation on the FavoritesListDTO is not met

        if(result.hasErrors()) {  // Return to page if there is an error editing the list
            model.addAttribute("list", listDTO);
            return "lists-edit";  // Re-render NOT a redirect reload with existing values
        }

        listDTO.setFavoritesListId(listId);  //

        favoritesListService.updateList(listDTO);

        return "redirect:/lists";
    }


    // LOAD DETAILS FOR SELECTED LIST
    @GetMapping("/lists/{favoritesListId}")
    public String listDetail(@PathVariable("favoritesListId") long listId, Model model) {

        FavoritesListDTO listDTO = favoritesListService.findListById(listId);  // Pull list from DB

        model.addAttribute("list", listDTO);  // Pass data to webpage by binding it to model

        return "lists-detail";
    }


    // DELETE EXISTING LIST
    @GetMapping("/lists/{favoritesListId}/delete")
    public String deleteList(@PathVariable("favoritesListId") long listId) {

        favoritesListService.delete(listId);

        return "redirect:/lists";
    }


    // TODO: ADD EXPLANATIONS HERE
    @GetMapping("/lists/search")
    public String searchList(@RequestParam(value = "query") String query, Model model) {

        List<FavoritesListDTO> lists = favoritesListService.searchLists(query);

        model.addAttribute("lists", lists);

        return "lists-list";
    }


}
