package com.PL.ranker_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
public class RankerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankerAppApplication.class, args);
	}
	{
		// START CLI MENU
		menu();


	}

	// COMMAND LINE MENU
	void menu() {

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

				// Create ItemList based on user input
				ItemList itemList = new ItemList(listString);

				itemList.printList();

				// Compare
				itemList.compareItems();

				//
				itemList.printMatrix();

			}
		}
		while (!action.equals("0"));

		// If 0 action, exit program
		System.exit(0);

	}
}
