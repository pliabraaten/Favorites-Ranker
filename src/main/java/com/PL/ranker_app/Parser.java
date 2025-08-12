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



// MANUAL PARSING ATTEMPT -> NEEDED TO ADD CONSIDERATION FOR LAST ITEM WITHOUT COMMA
//
//    public static String[] manualParse(String inputtedList) {
//
//        System.out.println("item #: " + itemCount);
//
//        int itemStart = 0;  // Tracks index of items in list
//        int itemEnd = 0;
//
//        // Parse through string breaking out items between commas
//        while (itemList.size() < itemCount) {
//            // pizza, corn
//            // 012345678910
//            itemEnd = inputtedList.indexOf(",");
//
//            String item = inputtedList.substring(itemStart, itemEnd);
//
//            // add strip whitespace
//
//            itemList.add(item);  //grab that position, add prior position to that position as new string
//
//            System.out.println("item: " + item);
//
//            inputtedList = inputtedList.substring(itemEnd + 2, length);  // Update list to be from comma on
//
//            itemStart = itemEnd;  // update prior position and look for next comma
//        }