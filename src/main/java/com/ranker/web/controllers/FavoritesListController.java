package com.ranker.web.controllers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import com.ranker.web.security.SecurityUtil;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import com.ranker.web.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


// TODO: REORGANIZE THESE MAPPINGS
@Controller
public class FavoritesListController {

    private FavoritesListService favoritesListService;
    private UserService userService; // ***
    private ItemService itemService;


    @Autowired
    public FavoritesListController(FavoritesListService favoritesListService, UserService userService, ItemService itemService) {
        this.favoritesListService = favoritesListService;
        this.itemService = itemService;
        this.userService = userService;  // ***
    }


    // HOME PAGE
    @GetMapping("/")
    public String home(Model model) {

        return "welcome";
    }


    @GetMapping("/lists")
    public String listLists(Model model) {

        UserEntity user = new UserEntity();  // *** Create user for controlling access

        String username = SecurityUtil.getSessionUser();  // *** Get logged-in user
        if (username != null) {  // *** If user is logged in
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);  // ***

        List<FavoritesListDTO> lists = favoritesListService.findUserLists();

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
    public String saveList(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @ModelAttribute("list") FavoritesListDTO listDTO,
                           BindingResult result,
                           Model model) {

        if(result.hasErrors()) {  // Return to page if there is an error creating the list
            model.addAttribute("list", listDTO);  // Re-render the form with previously inputted values
            return "lists-create";  // No redirect here in order to re-render the form
        }

        // Save new list via the service instance and then go back to home page
        Long listId = favoritesListService.saveList(listDTO);

        return "redirect:/lists/" + listId;
    }


    // LOAD DETAILS FOR SELECTED LIST
    @GetMapping("/lists/{id}")
    public String listDetail(@PathVariable("id") long listId, Model model) {

        FavoritesListDTO listDTO = favoritesListService.findListById(listId);  // Pull list from DB

        model.addAttribute("list", listDTO);  // Pass data to webpage by binding it to model

        return "lists-details";
    }


    // DELETE EXISTING LIST
    @GetMapping("/lists/{id}/delete")
    public String deleteList(@PathVariable("id") long listId) {

        favoritesListService.delete(listId);

        return "redirect:/lists";
    }


    // SEARCH LISTS
    @GetMapping("/lists/search")
    public String searchList(@RequestParam(value = "query") String query, Model model) {

        List<FavoritesListDTO> lists = favoritesListService.searchLists(query);

        model.addAttribute("lists", lists);

        return "lists-list";
    }


    // GET LIST INFO AND ALL ITEMS SORTED BY POSITION VALUE TO START RANKING
    @GetMapping("/lists/{listId}/rank")
    public String showRankingPage(@PathVariable long listId, Model model) {

        // Get list and all items
        FavoritesListDTO listDTO = favoritesListService.findListById(listId);  // Pull list via services and set it to DTO
        List<ItemDTO> itemsDTO = itemService.getItemsByListId(listId);  // Sorted by position at Repository

        // Check if list is empty
        if (listDTO.getItems().isEmpty()) {
            return "redirect:/lists/" + listId + "?noItems=true";
        }

        // Check if all items are already ranked
        if (listDTO.getSortedCount() >= listDTO.getItems().size()) {
            // Redirect back to list details with a message
            return "redirect:/lists/" + listId + "?alreadyRanked=true";
        }

        // Pass items to the view via model.addAttribute
        model.addAttribute("list", listDTO);
        model.addAttribute("items", itemsDTO);

        return "pairwise-comparison";
    }


    // IF USER WANTS TO RERANK ITEMS IN LIST -> RESET SORTED ITEM COUNT AND RANK
    @GetMapping("/lists/{listId}/rank/reset")
    public String resetRanking(
            @PathVariable Long listId,
            @RequestParam(required = false) String returnTo) {

        // Reset the sorted count to 0
        favoritesListService.updateSortedCount(listId, 0);

        // Flip Ranked flag to false
        favoritesListService.setRankedFlag(listId, false);

        // If cancelled rankings, reset and go back to the list-details
        if ("list".equals(returnTo)) {
            return "redirect:/lists/" + listId;  // Go to list details
        }

        // Redirect to ranking page to rerank
        return "redirect:/lists/" + listId + "/rank";
    }


    // EXPORT LIST AS CSV
    @GetMapping("/lists/{listId}/export")
    public void exportListToCsv(@PathVariable Long listId, HttpServletResponse response) throws IOException {
        FavoritesListDTO list = favoritesListService.findListById(listId);

        // Set response headers for file download
        response.setContentType("text/csv");
        String filename = list.getListName().replaceAll("[^a-zA-Z0-9]", "_") + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // Write CSV
        try (PrintWriter writer = response.getWriter()) {
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("Ranked Position", "Item Name"));

            for (ItemDTO item : list.getItems()) {
                csvPrinter.printRecord(item.getPosition(), item.getName());
            }

            csvPrinter.flush();
        }
    }


    // EXPORT ALL LISTS AS CSV
    @GetMapping("/lists/export-all")
    public void exportAllListsToCsv(@AuthenticationPrincipal UserDetails userDetails,
                                    HttpServletResponse response) throws IOException {
        String username = userDetails.getUsername();
        List<FavoritesListDTO> allLists = favoritesListService.findUserLists();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"all_lists.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("List Name", "Position", "Item Name"));

            for (FavoritesListDTO list : allLists) {
                for (ItemDTO item : list.getItems()) {
                    csvPrinter.printRecord(list.getListName(), item.getPosition(), item.getName());
                }
            }

            csvPrinter.flush();
        }
    }


    @GetMapping("/privacy")
    public String privacyPage() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsPage() {
        return "terms";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}