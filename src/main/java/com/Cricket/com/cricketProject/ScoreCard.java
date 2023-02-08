package com.Cricket.com.cricketProject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ScoreCard {
    Inning inning;
    public int totalRuns=0,totalWickets=0;
    public ScoreCard(Inning inning){
        this.inning=inning;

        for(int i=0;i<11;i++){
            playerMappingBattingTeam.put(inning.battingTeam.playersArray.get(i),new BattingScoreCardData());
        }

        for(int i=0;i<11;i++){
            playerMappingBowlingTeam.put(inning.bowlingTeam.playersArray.get(i),new BowlerScoreCardData());
        }
    }
    public TreeMap<Player, BattingScoreCardData> playerMappingBattingTeam = new TreeMap<>();
    public TreeMap<Player, BowlerScoreCardData> playerMappingBowlingTeam = new TreeMap<>();

    public void addBall(Ball ball,Player batsman,Player bowler){
        BattingScoreCardData batsmanData = playerMappingBattingTeam.get(batsman);
        BowlerScoreCardData bowlerData = playerMappingBowlingTeam.get((bowler));
        if(ball.getOutcome()==7){
            batsmanData.setOut();
            bowlerData.incrementWicketsTaken();
            totalWickets++;
        }
        else{
            batsmanData.updateRuns(ball);
            bowlerData.incrementRunsGiven(ball);
            totalRuns+=ball.getOutcome();
        }
        batsmanData.addBall(ball);
        playerMappingBowlingTeam.put(bowler,bowlerData);
        playerMappingBattingTeam.put(batsman,batsmanData);

    }

}
