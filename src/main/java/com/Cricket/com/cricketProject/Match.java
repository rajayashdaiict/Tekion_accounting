package com.Cricket.com.cricketProject;

import java.util.Scanner;
public class Match {

    static int overs;
    Team winner;

    public void startMsg(){
        System.out.println("Welcome the match has started");
    }
    public Pair<Team, Team> toss(Pair<Team, Team> teams){

        //select a random team as a tosswinner
        Team[] teamsForToss = {teams.getFirst(),teams.getSecond()};
        int randomIndexFromList = (int)Math.ceil(teamsForToss.length*Math.random())-1;
        Team tossWinner = teamsForToss[randomIndexFromList];

//        System.out.println(tossWinner.name+" has won the toss");
//        System.out.println("Team "+tossWinner.name+", enter 1 to bat or enter 2 to bowl");
//        Scanner sc = new Scanner(System.in);
//        int userInput = sc.nextInt();
        int userInput = 1;

//        while(!validateUserInput(userInput)){
//            System.out.println("Enter a valid input....");
//            userInput = sc.nextInt();
//        }

        //first team in pair will be batting in the inning
        if(userInput==1)
           return new Pair<Team, Team>(teams.getFirst(),teams.getSecond());
        else
            return new Pair<Team, Team>(teams.getSecond(),teams.getFirst());
    }
    private boolean validateUserInput(int userInput){
        if(userInput==1||userInput==2)
            return true;
        else
            return false;
    }

    public Pair<Team, Team> swapTeams(Pair<Team, Team> teams){
        return new Pair<Team, Team>(teams.getSecond(),teams.getFirst());
    }


    public Team decideWinner(ScoreCard inning1ScoreCard , ScoreCard inning2ScoreCard){

        if(inning1ScoreCard.totalRuns>inning2ScoreCard.totalRuns){
            return inning1ScoreCard.inning.battingTeam;
        }
        else if(inning1ScoreCard.totalRuns<inning2ScoreCard.totalRuns){
            return inning2ScoreCard.inning.battingTeam;
        }
        return new Team("dummy");
    }
}
