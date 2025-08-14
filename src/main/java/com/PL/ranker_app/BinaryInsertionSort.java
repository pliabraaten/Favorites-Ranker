package com.PL.ranker_app;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class BinaryInsertionSort {

    static ArrayList<String> rankedList = new ArrayList<String>();


    public static ArrayList<String> compareItems(String[] parsedList) {

        int itemCount = parsedList.length;
        Scanner scr = new Scanner(System.in);


        String winner;
        String loser;

        //Array A can be accessed by A.get(index) and A.set(index, value)


        // For first comparison, just compare first two items
        for (int i=0; i<itemCount; i++) {

            if (i==0) {

                do {
                    System.out.println(parsedList[i] + "vs " + parsedList[i + 1]);

                    winner = scr.nextLine();
                    if (Objects.equals(winner, parsedList[i])) {
                        loser = parsedList[i+1];
                    }
                    else {
                        loser = parsedList[i];
                    }
                }
                while (!winner.equals(parsedList[i]) && !winner.equals(parsedList[i+1]));

                // Put ranked items into ArrayList in order
                rankedList.add(winner);  // Add since arraylist is empty and set only works if it is non-empty
                rankedList.add(loser);


            }

        }

//        ItemList.rankedList;


        // From parsed list, compare that item to elements in ArrayList via binary insertion

    return rankedList;
    }

}
