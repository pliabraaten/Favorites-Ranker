package com.PL.ranker_app;

public class ItemList {

    private final String inputList;
    public static String[] parsedList = new String[0];
    private final int numberOfItems;
    public String[] rankedList = new String[0];


    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.numberOfItems = parsedList.length;
    }


    void printInputList() {

        System.out.print("Input list: ");

        for (int i = 0; i < numberOfItems; i++) {

            // If the last element, don't add a comma
            if (i == numberOfItems - 1) {

                System.out.print(parsedList[i]);
            }
            // For every element sans last, add a comma after
            else {
                System.out.print(parsedList[i] + ", ");
            }
        }
        System.out.print("\n");
    }


    void printRankedList() {

        System.out.println("Ranked List: ");

        for (int i = 0; i < numberOfItems; i++) {

            System.out.println(i + 1 + ": " + rankedList[i]);  // Print ranked position and item per line
        }
    }


    // Run the pairwise comparison prompts using binary search and reorder list according to rank
    void compareItems() {

        rankedList = BinaryInsertionSort.rank(parsedList);

    }
}