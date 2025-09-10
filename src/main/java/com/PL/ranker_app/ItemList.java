package com.PL.ranker_app;

import java.util.ArrayList;

public class ItemList {

    private final String inputList;
    private ArrayList<String> parsedList = new ArrayList<>();
    private int numberOfItems;
    private ArrayList<String> rankedList = new ArrayList<>();


    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.numberOfItems = parsedList.size();
    }


    void printInputList() {

        numberOfItems = parsedList.size();

        System.out.println("Input list: ");

        for (int i = 0; i < parsedList.size(); i++) {
            System.out.println(parsedList.get(i));
        }
    }


    void printRankedList() {

        System.out.println("Ranked List: ");

        for (int i = 0; i < rankedList.size(); i++) {

            System.out.println(i + 1 + ": " + rankedList.get(i));  // Print ranked position and item per line
        }
    }


    // Run the pairwise comparison prompts using binary search and reorder list according to rank
    void compareItems(int numberSorted) {

        rankedList = BinaryInsertionSort.rank(parsedList, numberSorted);  // Update rankedList

    }



    // Add additional items to the list to be ranked
    void addItems(String newInput) {

        // Parse added inputs into an ArrayList
        ArrayList<String> addedItems = Parser.parse(newInput);
        int numberAdded = addedItems.size();

        // Add additional ArrayList elements to the ParsedList
        for (int i=0; i<numberAdded; i++) {

            parsedList.add(addedItems.get(i));
        }

        // FIXME: RANKED LIST IS UPDATING TO MATCH PARSED LIST IMMEDIATELY
        // RANKED LIST NEEDS TO = PARSED LIST ONLY WHEN RANK() IS RUN AND ONLY UNTIL FIRST COMPARISON IS DONE
        // ARE RANKED AND PARSED LINKED SOMEWHERE OR POINTING TO THE SAME PLACE?


        numberOfItems += numberAdded;

        printInputList();
        printRankedList();

        compareItems(rankedList.size());  // Input number of elements already sorted to skip re-ranking them

    }
}