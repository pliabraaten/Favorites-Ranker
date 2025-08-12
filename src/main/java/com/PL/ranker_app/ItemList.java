package com.PL.ranker_app;

public class ItemList {

    private final String inputList;
    private final String[] rankedList;
    private final int length;

    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.rankedList = Parser.parse(inputList);  // Parse inputted string into elements in this String[] object
        this.length = rankedList.length;

    }

    // Print all elements
    void printList() {

        System.out.print("Ranked list: ");

        for (int i=0; i<length; i++) {

            if (i == length - 1) {

                System.out.print(rankedList[i]);
            }
            else {
                System.out.print(rankedList[i] + ", ");
            }
        }
        System.out.print("\n");
    }

    // FIXME: later update the algorithm to minimize pairing iterations

    void compareItems() {

        // Iterate though all pairs
        for (int i=0; i<length; i++) {
            for (int j=0; j<length; j++) {
                System.out.println(rankedList[i] + ": " + rankedList[j]);


            }
        }


    }



}
