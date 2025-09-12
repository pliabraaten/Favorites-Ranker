package com.PL.ranker_app;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {


    // COMMAND LINE MENU
    void startMenu() {

        Scanner scanner = new Scanner(System.in);
        String listString = null;
        ItemList itemList = null;
        String action = null;

        // Keeper user in menu until they exit by entering 0
        do {

            if (itemList == null) {  // If no list has been entered yet
                System.out.println("Welcome to the Ranker Project\n"
                        + "Type 1 to enter list of items to be ranked\n"
                        + "Type 0 to exit program");
            }
            else {
                System.out.println("Welcome to the Ranker Project\n"
                        + "Type 2 to view the ranked list of items\n"
                        + "Type 3 to add items to the list and then rank\n"
                        + "Type 4 to remove an item from the ranked list\n"
                        + "Type 0 to exit program");
            }

            // Retrieve action input from user
            action = scanner.nextLine();

            // If 1 save input as string
            if (action.equals("1") && itemList == null) {

                // Prompt user to enter comma separated list
                System.out.println("Enter list (ex: item1, item2, ...");
                listString = scanner.nextLine();

//				System.out.println("inputted list: " + listString);

                // Create ItemList based on user input (parsed in constructor)
                itemList = new ItemList(listString);

//                itemList.printList();

                // Run pairwise comparisons
                itemList.compareItems(0, null);

                // Print items ranked in order of preference
//                itemList.printRankedList();

            }


            // If 2 print ranked list
            if (action.equals("2") && itemList != null) {  // Make sure the user already created initial list

                itemList.printRankedList();
            }


            // If 3, take additional items, parse, then insert them into ranked list
            if (action.equals("3") && itemList != null) {  // Make sure the user already created initial list

                // Prompt user to enter MORE comma separated list
                System.out.println("Enter additional items to be added to the list (ex: item1, item2, ...");
                String newInput = scanner.nextLine();

                // Run method to add newInput to the ParsedList
                itemList.addItems(newInput);

            }


            // FIXME: validation in case user enters item that is not in the ranked list
            // If 3, print ranked list and then prompt user to remove an item
            if (action.equals("4") && itemList != null) {  // Make sure the user already created initial list

                // Print ranked list
                itemList.printRankedList();

                // Prompt user to select an item to remove
                System.out.println("Enter item to be removed to the list");
                String newInput = scanner.nextLine();

                // Run method to add newInput to the ParsedList
                itemList.removeItem(newInput);

            }

        }
        while (!action.equals("0"));  // Stay in menu until user enters 0

        // If 0 action, exit program
        System.exit(0);
    }
}