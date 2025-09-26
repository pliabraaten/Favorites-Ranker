package com.PL.ranker_app.controller;

import com.PL.ranker_app.domain.ItemList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;


@Controller
public class MainScreenController {

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("features", Arrays.asList(
                "pizza",
                "corn",
                "tortilla"
        ));

        return "main";
    }


//    // INPUT TEXT STRING OF ITEMS TO BE RANKED
//    @PostMapping("/createInputList")
//    public String createInputList(@ModelAttribute("itemList") ItemList itemList, Model model){
//
//        // CREATE ITEMLIST OBJECT
//        model.addAttribute("itemList", itemList);
//
//        // PARSE INTO ARRAY
//
//        // PRINT ARRAY
//
//        return "main";}


    // ADD NEW ITEM TO THE LIST
//    @PostMapping("/addItems")  //FIXME: CAN I HAVE MULTIPLE POSTMAPPINGS ON THE MAIN?
//    //FIXME: THIS IS LOGIC STILL ON THE MAIN SCREEN; IS THERE WHERE I DISTINGUISH BY THE ACTION ELEMENT?
//    public String addItems(){

        // PARSE

        // ADD TO EXISTING LIST

        // PRINT UPDATED ARRAY
//    }


    // DELETE AN ITEM
//    @PostMapping()

        // ADD BUTTONS TO ALL THE LIST ITEMS?
        // USER CLICKS DELETE BUTTON NEXT TO ITEMS TO BE REMOVED?


    // BIG BUTTON: RANK
//    @PostMapping()

        // REDIRECT USER TO NEW PAGE FOR PAIRWISE COMPARISIONS


}
