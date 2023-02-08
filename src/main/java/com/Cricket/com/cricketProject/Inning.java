package com.Cricket.com.cricketProject;

public class Inning {
    public Inning(Pair<Team, Team> teams, int overs){
        this.battingTeam=teams.getFirst();
        this.bowlingTeam=teams.getSecond();
        this.overs=overs;

    }
    public Inning(Pair<Team, Team> teams, int overs, int target){
        this(teams,overs);
        this.target=target;
    }

    Team battingTeam;
    Team bowlingTeam;
    static Player batsman1,batsman2,bowler,lastBowler=new Player("dummy");
    Integer overs,target=null,remainingWickets=10;
    ScoreCard scoreCard;


    Player selectBowler(Player lastBowler){
        int randomIndex = (int)Math.ceil(Math.random()*11)-1;
        //Until you find a new bowler keep recursing
        if(bowlingTeam.playersArray.get(randomIndex)==lastBowler){
            return selectBowler(lastBowler);
        }
        return bowlingTeam.playersArray.get(randomIndex);
    }

    public void setScoreCard(ScoreCard scoreCard){
        this.scoreCard=scoreCard;
    }
    public Player selectBatsman(){
        Player nextBatsman;
        for(int i=0;i<11;i++){
            nextBatsman = battingTeam.playersArray.get(i);
            if(!scoreCard.playerMappingBattingTeam.get(nextBatsman).isHasPlayed()){
                scoreCard.playerMappingBattingTeam.get(nextBatsman).setHasPlayed();
                return nextBatsman;
            }
        }
        return null;
    }
    public void decreaseRemainingWicket(){
        remainingWickets--;
    }

    public static void  switchStrick(){
        Player c=batsman2;
        batsman2=batsman1;
        batsman1=c;
    }

    public void play(){
        batsman1=selectBatsman();
        batsman2=selectBatsman();
        bowler=selectBowler(lastBowler);
        for(int i=0;i<overs;i++){
            for(int j=0;j<6;j++){

                Ball currentBall=new Ball();
//                System.out.println(batsman1.name+"  "+batsman2.name+"  "+bowler.name+"  "+currentBall.getOutcome()+"  "+remainingWickets);
                scoreCard.addBall(currentBall, batsman1, bowler);
                if(scoreCard.playerMappingBattingTeam.get(batsman1).isOut){
                    decreaseRemainingWicket();
                    if(remainingWickets==0){
                        return;
                    }
                    batsman1=selectBatsman();
                }
                else{
                    if(currentBall.getOutcome()%2!=0){
                        switchStrick();
                    }
                    if(target!=null&&target<scoreCard.totalRuns){
                        return;
                    }
                }
            }
            lastBowler=bowler;
            bowler=selectBowler(lastBowler);
            switchStrick();
        }

    }


}
