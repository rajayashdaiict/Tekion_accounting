package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.GameplayDto;
import com.Jpalearning.jpalearning.dto.InningTeamsDto;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Scanner;

@Service
public class Helper {
    public InningTeamsDto toss(GameplayDto gameplayDto) {
        //select a random team as a tosswinner
        Random rand = new Random();
        int randomInt = rand.nextInt(2) + 1;

        Team tossWinner, tossLoser;
        if (randomInt == 1) {
            tossWinner = gameplayDto.getTeam1();
            tossLoser = gameplayDto.getTeam2();
        } else {
            tossWinner = gameplayDto.getTeam2();
            tossLoser = gameplayDto.getTeam1();
        }
        System.out.println(tossWinner.getName() + " has won the toss");
        System.out.println("Team " + tossWinner.getName() + ", enter 1 to bat or enter 2 to bowl");
//        Scanner sc = new Scanner(System.in);
//        int userInput = sc.nextInt();
        int userInput=1;

        InningTeamsDto inningTeamsDto = new InningTeamsDto();
        //first team in pair will be batting in the inning
        if (userInput == 1) {
            inningTeamsDto.setBattingTeam(tossWinner);
            inningTeamsDto.setBowlingTeam(tossLoser);
        } else {
            inningTeamsDto.setBattingTeam(tossLoser);
            inningTeamsDto.setBowlingTeam(tossWinner);
        }
        return inningTeamsDto;
    }

    public InningTeamsDto swapTeams(InningTeamsDto inningTeamsDto){
        InningTeamsDto inningTeamsDto1 = new InningTeamsDto();
        inningTeamsDto1.setBowlingTeam(inningTeamsDto.getBattingTeam());
        inningTeamsDto1.setBattingTeam(inningTeamsDto.getBowlingTeam());
        return inningTeamsDto1;
    }

}
