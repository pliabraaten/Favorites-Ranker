package com.PL.ranker_app;

import java.util.Scanner;

import static com.PL.ranker_app.ItemList.parsedList;




public class BinaryInsertionSort {


    // Rank list by prompting user with pairwise comparisons and use Binary Insertion Sort
    public static String[] rank(String[] parsedList) {

        String[] rankedList = parsedList;
        int itemCount = parsedList.length;

        Scanner scr = new Scanner(System.in);

        String winner;
        String loser;

        int L = 0;  // Track left index
        int R = 0;  // For calculating the middle
        int M = 0;
        String selectedItem;


        // FIXME: concerns with the Arrow's impossibility theorem: A > B, B > C, but C > A is possible in preferences
        // BINARY INSERTION SORT
        // https://en.wikipedia.org/wiki/Insertion_sort#Variants
        // Time Complexity: The algorithm as a whole still has a running worst-case running time of O(n2) because of the series of swaps required for each insertion.
        // https://www.geeksforgeeks.org/dsa/binary-insertion-sort/

        // Take element
        for (int i = 1; i < itemCount; i++) {  // For every element in the rankedList; skipping first item (already sorted)
                // FIXME: update such that i starts at 0 and then skips the first element

            // Sorted portion of the array
            int j = 0;
            L = 0;
            R = i - 1;
            selectedItem = rankedList[i];

            // Prompt the user with pairwise comparisons via a binary sort comparison method to determine where each element ranks in the list
            int insertPosition = binarySearch(i, L, R, rankedList, selectedItem, scr);

            // Move the element to the ranked value position in the array
            insertionSort(j, insertPosition, i, rankedList, selectedItem);

        }

        return rankedList;
    }


    // Binary Search: Find location to be inserted
    static int binarySearch(int i, int L, int R, String[] rankedList, String selectedItem, Scanner scr) {
        while (L <= R) {  // Repeat until no more middles

            int M = (R - L) / 2;  // Find middle element in the sorted (left) side of the array

            // Prompt user to compared nextItem (i) to middle item
            String winner = pairwisePrompt(i, M, scr, rankedList);

            // If selected item is ranked higher than middle
            if (winner.equals(selectedItem)) {  // If selected element wins

                R = M - 1;  // L stays, R <- old middle
            } else if (winner.equals(rankedList[M])) {  // If nextItem loses, segment and find next middle

                L = M + 1;  // Move middle
            }
        }
        return L;
    }


    static String pairwisePrompt(int i, int M, Scanner scr, String[] rankedList) {

        // Prompt user to compared nextItem (i) to middle item
        System.out.println(rankedList[i] + " vs " + rankedList[M]);
        String winner = scr.nextLine();

        return winner;
    }


    // Insert new item into position (R = L = insert index)
    static void insertionSort(int j, int insertPosition, int i, String[] rankedList, String selectedItem) {
        while (j > insertPosition) {

            j = i - 1;  // Index of selectedItem

            rankedList[j + 1] = rankedList[j];  // Set
            j--;
        }
        rankedList[j + 1] = selectedItem;
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



