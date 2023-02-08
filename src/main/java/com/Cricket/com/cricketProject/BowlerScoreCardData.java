package com.Cricket.com.cricketProject;

public class BowlerScoreCardData {
    int overBowled=0;
    int wicketsTaken=0;

    public int getOverBowled() {
        return overBowled;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public int getRunsGiven() {
        return runsGiven;
    }

    int runsGiven=0;

    public void incrementOverBowled(){
        overBowled++;
    }
    public void incrementWicketsTaken(){
        wicketsTaken++;
    }
    public void incrementRunsGiven(Ball ball){
        runsGiven+=ball.getOutcome();
    }
}
