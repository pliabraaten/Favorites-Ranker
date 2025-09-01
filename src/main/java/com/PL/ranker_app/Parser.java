package com.PL.ranker_app;


public class Parser {

    // Parse comma separated input list
    public static String[] parse(String inputtedList) {

        // Obtain number of items in user list
        int length = inputtedList.length();

        int itemCount = 1;  // Starts at 1 since last items isn't followed by a comma

        // Count total commas to know total number of items in list
        for (int i=0; i<length; i++) {

            if (inputtedList.charAt(i) == ',') {

                itemCount++;
            }
        }

        // PARSE
        String[] itemList = inputtedList.split("\\s*,\\s*");  // Regex "\\s*,\\s*" looks for any whitespace around comma

        return itemList;
    }
}