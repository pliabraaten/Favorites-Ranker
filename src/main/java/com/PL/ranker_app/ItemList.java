package com.PL.ranker_app;

import java.util.ArrayList;

public class ItemList {

    private final String inputList;
    private ArrayList<String> parsedList = new ArrayList<>();
    private final int numberOfItems;
    private ArrayList<String> rankedList = new ArrayList<>();


    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.numberOfItems = parsedList.size();
    }


    void printInputList() {

        System.out.print("Input list: ");

        for (int i = 0; i < numberOfItems; i++) {

            // If the last element, don't add a comma
            if (i == numberOfItems - 1) {

                System.out.print(parsedList.get(i));
            }
            // For every element sans last, add a comma after
            else {
                System.out.print(parsedList.get(i) + ", ");
            }
        }
        System.out.print("\n");
    }


    void printRankedList() {

        System.out.println("Ranked List: ");

        for (int i = 0; i < numberOfItems; i++) {

            System.out.println(i + 1 + ": " + rankedList.get(i));  // Print ranked position and item per line
        }
    }


    // Run the pairwise comparison prompts using binary search and reorder list according to rank
    void compareItems(int numberSorted) {

        rankedList = BinaryInsertionSort.rank(parsedList, numberSorted);

    }


    // Add additional items to the list to be ranked
    void addItems(String newInput) {


    }
}