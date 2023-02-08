package com.Cricket.com.cricketProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CricketProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketProjectApplication.class, args);
		System.out.println("Hello welcome to the game of cricket");
		Match match=new Match();
		GamePlay.gamePlay(match);
	}

}
