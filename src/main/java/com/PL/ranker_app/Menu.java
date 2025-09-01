package com.PL.ranker_app;

import java.util.Scanner;

public class Menu {


    // COMMAND LINE MENU
    void startMenu() {

        Scanner scanner = new Scanner(System.in);
        String listString = null;

        String action = null;

        // Keeper user in menu until they exit by entering 0
        do {
            System.out.println("Welcome to the Ranker Project\n"
                    + "Type 1 to enter list of items to be ranked\n"
                    + "Type 0 to exit program"
            );

            // Retrieve action input from user
            action = scanner.nextLine();

            // If 1 save input as string
            if (action.equals("1")) {

                // Prompt user to enter comma separated list
                System.out.println("Enter list (ex: item1, item2, ...");
                listString = scanner.nextLine();

//				System.out.println("inputted list: " + listString);

                // Create ItemList based on user input (parsed in constructor)
                ItemList itemList = new ItemList(listString);

//                itemList.printList();

                // Run pairwise comparisons
                itemList.compareItems();

                // Print items ranked in order of preference
                itemList.printRankedList();

            }
        }
        while (!action.equals("0"));  // Stay in menu until user enters 0

        // If 0 action, exit program
        System.exit(0);
    }
}