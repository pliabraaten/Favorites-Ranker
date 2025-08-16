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
		Menu menu = new Menu();
		menu.startMenu();


	}


}
