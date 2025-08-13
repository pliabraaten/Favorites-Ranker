package com.PL.ranker_app;

import java.util.Objects;
import java.util.Scanner;

public class ItemList {

    private final String inputList;
    private final String[] parsedList;
    private final int length;
    private final String[][] resultMatrix;
    private final String[] rankedList;

    Scanner scr = new Scanner(System.in);

    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.parsedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.length = parsedList.length;
        this.resultMatrix = new String[length][length];
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
    void printMatrix() {

        System.out.println("Result Matrix: ");

        // Iterate though all pairs to create Matrix
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {

                System.out.println(resultMatrix[i][j]);
            }
        }
    }


    // FIXME: optimize this -> better way to avoid combinations
    void compareItems() {

        int elementTracker = 0;  // This increments to avoid combinations in iteration (ex: avoid 1,0 after 0,1)

        // Iterate though all pairs
        for (int i=0; i<length; i++) {
            for (int j=elementTracker; j<length; j++) {

                if (Objects.equals(parsedList[i], parsedList[j])) {  // Comparison of same item is not considered

                    resultMatrix[i][j] = "X";
                }
                else {

                    System.out.println(parsedList[i] + ": " + parsedList[j]);

                    resultMatrix[i][j] = scr.nextLine();   // Add user input to matrix
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