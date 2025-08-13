package com.PL.ranker_app;

import java.util.Objects;
import java.util.Scanner;

public class ItemList {

    private final String inputList;
    private final String[] parsedList;
    private final int length;
    private final String[] pairwiseWinners;

    private int numberOfPairs = 0;
    private String[] rankedList;

    Scanner scr = new Scanner(System.in);


    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.length = parsedList.length;

        this.numberOfPairs = (length * (length - 1)) / 2;  // Counting pairs formula: n(n-1) / 2

        this.pairwiseWinners = new String[numberOfPairs];
        this.rankedList = new String[length];
    }


    // Print all elements
    void printList() {

        System.out.print("Ranked list: ");

        for (int i=0; i<length; i++) {

            if (i == length - 1) {

                System.out.print(parsedList[i]);
            }
            else {
                System.out.print(parsedList[i] + ", ");
            }
        }
        System.out.print("\n");
    }


    // Print results Matrix
    void printWinners() {

        System.out.println("Winner List: ");

        // Iterate though all pairs to create Matrix
        for (int i=0; i < numberOfPairs; i++) {

            System.out.println(pairwiseWinners[i]);
        }
    }


    // FIXME: optimize this -> better way to avoid combinations
    void compareItems() {

        int elementTracker = 0;  // This increments to avoid combinations in iteration (ex: avoid 1,0 after 0,1)
        int pairCounter = 0;  // Tracks which element is put into pairwiseWinners list

        // Iterate though all pairs
        for (int i=0; i<length; i++) {
            for (int j=elementTracker; j<length; j++) {

                if (!Objects.equals(parsedList[i], parsedList[j])) {  // Comparison of same item is not considered

                    System.out.println(parsedList[i] + ": " + parsedList[j]);

                    pairwiseWinners[pairCounter] = scr.nextLine();   // Add user input to matrix

                    pairCounter++;
                }
            }

            elementTracker++;
        }
    }


    // Rank items based on results
    void rankItems() {

        // Loop through results
        for (int i=0; i<length; i++) {
            for (int j=0; j < length; j++) {




            }
        }
    }


}
