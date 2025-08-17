package com.PL.ranker_app;

import java.util.Scanner;

import static com.PL.ranker_app.ItemList.parsedList;




public class BinaryInsertionSort {

    public static String[] rankedList;
    public static int itemCount = parsedList.length;


    public BinaryInsertionSort(String[] parsedList) {

        rankedList = parsedList;
    }


    // Rank list by prompting user with pairwise comparisons and use Binary Insertion Sort
    public static String[] rank() {

        Scanner scr = new Scanner(System.in);

        String winner;
        String loser;

        int L = 0;  // Track left index
        int R = 0;  // For calculating the middle
        int M = 0;


        // FIXME: concerns with the Arrow's impossibility theorem: A > B, B > C, but C > A is possible in preferences
        // BINARY INSERTION SORT
            // https://en.wikipedia.org/wiki/Insertion_sort#Variants
            // O (log N)

        // Take element
        for (int i=0; i < itemCount; i++) {  // For every element in the rankedList

            if (i == 0) {  // If first, then it is considered sorted

                continue;
            }
            else {

                // Find middle element in the sorted (left) side of the array
                    // Sorted portion is L to i-1
                L = 0;
                R = i - 1;
                M = findMiddle(R, L, M);

                // Prompt user to compared nextItem (i) to middle item
                winner = compareItems(i, M, scr);

                // If nextItem wins, segment and find next middle
                    // L stays, R <- old middle
                if (winner.equals(rankedList[i])) {

                    R = M;  // Move middle
                    M = findMiddle(R, L, M);  // Find next middle

                    // Prompt user to compared nextItem (i) to middle item
                    winner = compareItems(i, M, scr);

                } else if (winner.equals(rankedList[M])) {  // If nextItem loses, segment and find next middle

                    // R stays, L <- old middle
                    L = M;
                    M = findMiddle(R, L, M);

                    winner = compareItems(i, M, scr);

                }



                // Repeat until no more middles FIXME: what conditional?

                // Insert new item into position FIXME: how?

            }

        }


        return rankedList;
    }


    static int findMiddle(int L, int R, int M) {

        // L is farthest left, R is farthest right in sorted portion
        M = (R - L) / 2;

        System.out.println("L: " + L);
        System.out.println("R: " + R);
        System.out.println("M: " + M);

        return M;
    }


    static String compareItems(int i, int M, Scanner scr) {

        // Prompt user to compared nextItem (i) to middle item
        System.out.println(rankedList[i] + "vs " + rankedList[M]);
        String winner = scr.nextLine();

        return winner;
    }



}


        // OLD ATTEMPT AT SORTING THEN INSERTING INTO DIFFERENT ARRAY
//        int L = 0;  // Left index
//        int R = itemCount - 1;  // Right index
//
//        // For first comparison, just compare first two items
//        for (int i=0; i<itemCount; i++) {
//
//            // Handle first comparison
//            if (i==0) {
//
//                do {
//                    System.out.println(parsedList[i] + " vs " + parsedList[i + 1]);
//
//                    winner = scr.nextLine();
//
//                    if (Objects.equals(winner, parsedList[i])) {
//                        loser = parsedList[i+1];
//                    }
//                    else {
//                        loser = parsedList[i];
//                    }
//                }
//                while (!winner.equals(parsedList[i]) && !winner.equals(parsedList[i+1]));
//
//                // Put ranked items into ArrayList in order
//                rankedList.add(winner);  // Add since arraylist is empty and set only works if it is non-empty
//                rankedList.add(loser);
//
//                i++;  // Skip 2nd item since it was sorted in the first comparison
//            }
//            else {  // All other comparisons
//
//
//                int middle = (L - R) / 2;  // Middle index
//
//                // Take next item from parsed list
//                String currentItem = parsedList[i];
//
//                // Compare it to middle item in ranked list
//                System.out.println(currentItem + " vs " + rankedList.get(middle));
//
//                winner = scr.nextLine();
//
//
//                insert();
//
//
//
//            }
//        }
//
////        ItemList.rankedList;
//
//
//        // From parsed list, compare that item to elements in ArrayList via binary insertion
//
//    return rankedList;
//    }
//
//
//    static void insert() {
//
//        // take next element from parsed list
//        // current = next element
//
//

        // find middle of ranked list

        // compared current to middle with user feedback

        // if user says current > than middle, find next middle to the left

            // repeat

        // if user says current is < than middle, find next middle to the right

            // repeat

        // once there is nothing to the right/left of the last "middle"

        // add current to the arrayList



