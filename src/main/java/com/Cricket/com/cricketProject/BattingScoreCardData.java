package com.Cricket.com.cricketProject;

import java.util.ArrayList;
import java.util.List;

public class BattingScoreCardData{
    int totalRun;
    List<Ball> balls = new ArrayList<>();
    boolean hasPlayed=false;

    public int getTotalRun() {
        return totalRun;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public boolean isOut() {
        return isOut;
    }

    boolean isOut=false;
    public void setOut(){
        isOut=true;
    }

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(){
        hasPlayed=true;
    }
    public void updateRuns(Ball ball){
        totalRun+=ball.getOutcome();
    }
    public void addBall(Ball ball){
        balls.add(ball);
    }
}
