package com.PL.ranker_app;

import com.PL.ranker_app.domain.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
