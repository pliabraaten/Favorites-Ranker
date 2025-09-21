package com.PL.ranker_app.service;


import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    // Parse comma separated input list
    public static ArrayList<String> parse(String inputtedList) {

        // Obtain number of items in user list
        int length = inputtedList.length();

        int itemCount = 1;  // Starts at 1 since last items isn't followed by a comma

        // Count total commas to know total number of items in list
        for (int i=0; i<length; i++) {

            if (inputtedList.charAt(i) == ',') {

                itemCount++;
            }
        }

        // PARSE into smaller strings
        String[] itemList = inputtedList.split("\\s*,\\s*");  // Regex "\\s*,\\s*" looks for any whitespace around comma

        // Convert String[] into ArrayList
        ArrayList<String> itemArrayList = new ArrayList<String>(Arrays.asList(itemList));


//        itemList = Arrays.stream(itemList)  // Converts array into Stream for processing
//                .filter(s -> (s != null && !s.isEmpty()))  // Filters Stream to only include elements that are not null or empty
//                .toArray(String[]::new);  // Returns filtered stream as new array

        return itemArrayList;
    }
}