package com.PL.ranker_app;

import java.util.Scanner;

public class ItemList {

    private final String inputList;
    public final String[] parsedList;
    private final int numberOfItems;
    private final String[] pairwiseWinners;

    private int numberOfPairs = 0;

    Scanner scr = new Scanner(System.in);

    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.numberOfItems = parsedList.length;

        this.numberOfPairs = (numberOfItems * (numberOfItems - 1)) / 2;  // Counting pairs formula: n(n-1) / 2

        this.pairwiseWinners = new String[numberOfPairs];
    }


    // Print all elements
    void printList() {

        System.out.print("Ranked list: ");

        for (int i = 0; i< numberOfItems; i++) {

            if (i == numberOfItems - 1) {

                System.out.print(parsedList[i]);
            }
            else {
                System.out.print(parsedList[i] + ", ");
            }
        }
        System.out.print("\n");
    }


    // Print ranked list
    void printRankedList() {

        System.out.println("Ranked List: ");

        for (int i = 0; i< numberOfItems; i++) {

            System.out.println(i+1 + ": " + BinaryInsertionSort.rankedList.get(i));  // Print ranked position and item
        }
    }


    void compareItems() {

        BinaryInsertionSort.compareItems(parsedList);

    }

//    // FIXME: optimize this -> Big O(N^2)
//    void compareItems() {
//
//        int elementTracker = 0;  // This increments to avoid combinations in iteration (ex: avoid 1,0 after 0,1)
//        int pairCounter = 0;  // Tracks which element is put into pairwiseWinners list
//
//        // Iterate though all pairs
//        for (int i=0; i<length; i++) {
//            for (int j=elementTracker; j<length; j++) {
//
//                if (!Objects.equals(parsedList[i], parsedList[j])) {  // Comparison of same item is not considered
//
//                    System.out.println(parsedList[i] + ": " + parsedList[j]);
//
//                    pairwiseWinners[pairCounter] = scr.nextLine();   // Add user input to matrix
//
//                    pairCounter++;
//                }
//            }
//
//            elementTracker++;
//        }
//    }




}
