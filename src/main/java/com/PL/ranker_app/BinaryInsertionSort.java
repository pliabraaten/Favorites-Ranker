package com.PL.ranker_app;

import java.util.ArrayList;
import java.util.Scanner;

public class BinaryInsertionSort {


    // Rank list by prompting user with pairwise comparisons and use Binary Insertion Sort
    public static ArrayList<String> rank(ArrayList<String> parsedList, int numberSorted) {

        ArrayList<String> rankedList = parsedList;
        int itemCount = parsedList.size();

        Scanner scr = new Scanner(System.in);

        int L = 0;  // Track left index
        int R = 0;  // For calculating the middle
        String selectedItem;  // Value of element being ranked

        // FIXME: concerns with the Arrow's impossibility theorem: A > B, B > C, but C > A is possible in preferences

        // BINARY INSERTION SORT

        // https://en.wikipedia.org/wiki/Insertion_sort#Variants
        // Time Complexity: The algorithm as a whole still has a running worst-case running time of O(n2) because of the series of swaps required for each insertion.
        // Binary insertion sort employs a binary search to determine the correct location to insert new elements, and therefore performs ⌈log2 n⌉ comparisons in the worst case.
            // When each element in the array is searched for and inserted this is O(n log n).[7]
            // The algorithm as a whole still has a running time of O(n2) on average because of the series of swaps required for each insertion.[7]

        // https://www.geeksforgeeks.org/dsa/binary-insertion-sort/



        // Take element
        for (int i = numberSorted + 1; i < itemCount; i++) {  // For every element in the rankedList; skipping first item (already sorted)
                // FIXME: update such that i starts at 0 and then skips the first element

            // Sorted portion of the array
            L = 0;  // Left
            R = i - 1;  // Right
            selectedItem = rankedList.get(i);  // Value of element being ranked
            int j = i - 1;  // i is the index of selectedItem, j is prior element

            // Find rank position with pairwise comparisons via a binary sort comparison method
            int insertPosition = binarySearch(i, L, R, rankedList, selectedItem, scr);

            // Move the element to the ranked value position in the array
            insertionSort(j, insertPosition, i, rankedList, selectedItem);
        }

        return rankedList;
    }


    // Binary Search: Find location to be inserted
    static int binarySearch(int i, int L, int R, ArrayList<String> rankedList, String selectedItem, Scanner scr) {
        while (L <= R  && R >= 0) {  // Repeat until no more middle value

            int M = L + (R - L) / 2;  // Find middle element in the sorted (left) side of the array

            // Prompt user to compare nextItem (i) to middle item (M)
            String winner = pairwisePrompt(i, M, scr, rankedList);

            // If selected item is ranked higher than middle
            if (winner.equals(selectedItem)) {

                R = M - 1;  // L stays, R <- old middle; continue on left side of sorted
            }
            // If selected items is ranked lower than middle
            else if (winner.equals(rankedList.get(M))) {

                L = M + 1;  // Move left to middle and continue search on right side of sorted
            }
        }
        return L;
    }


    static String pairwisePrompt(int i, int M, Scanner scr, ArrayList<String> rankedList) {

        String winner;

        do {
            // Prompt user to compared nextItem (i) to middle item
            System.out.println(rankedList.get(i) + " vs " + rankedList.get(M));
            winner = scr.nextLine();
        } while (!winner.equals(rankedList.get(i)) && !winner.equals(rankedList.get(M)));  // Keep prompting until response is valid

        return winner;
    }


    // Insert new item into position (R = L = insert index) by swapping elements
    static void insertionSort(int j, int insertPosition, int i, ArrayList<String> rankedList, String selectedItem) {
        while (j >= insertPosition) {

            rankedList.set(j + 1, rankedList.get(j));  // Move element one place to the right (j starts as element immediately to the left of selectedItem)
                // Put prior element at the index of element to its right

            j--;  // Move to the next element to the left

        }
        // Keep moving elements to the right 1 until at insertPosition, then put value of selectedItem there
        rankedList.set(j + 1, selectedItem);
    }

}