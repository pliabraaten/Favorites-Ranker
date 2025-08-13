package com.PL.ranker_app;

import java.util.Scanner;

public class ItemList {

    private final String inputList;
    private final String[] parsedList;
    private final int length;

    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.length = parsedList.length;

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


    // FIXME: later update the algorithm to minimize pairing iterations
    void compareItems() {

        Scanner scr = new Scanner(System.in);
        String[] result = new String[length];

        // Iterate though all pairs
        for (int i=0; i<length; i++) {
            for (int j=0; j<length; j++) {

                if (parsedList[i] != parsedList[j]) {
                    System.out.println(parsedList[i] + ": " + parsedList[j]);

                    result[i] = scr.nextLine();  // Get user input
                }

            }
        }


    }



}
