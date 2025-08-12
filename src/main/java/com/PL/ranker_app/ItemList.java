package com.PL.ranker_app;

public class ItemList {

    private final String inputList;
    private final String[] rankedList;

    // Constructor
    public ItemList(String inputtedList) {

        this.inputList = inputtedList;
        this.rankedList = Parser.parse(inputList);

    }


    // Print method
    void printList() {

        System.out.print("Ranked list: ");

        for (int i=0; i<rankedList.length; i++) {

            if (i == rankedList.length - 1) {

                System.out.print(rankedList[i]);
            }
            else {
                System.out.print(rankedList[i] + ", ");
            }
        }
        System.out.print("\n");
    }
}
