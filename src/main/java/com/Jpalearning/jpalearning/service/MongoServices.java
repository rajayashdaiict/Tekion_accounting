package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.BallMongo;
import com.Jpalearning.jpalearning.Entity.MatchMongo;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.Ball;
import com.Jpalearning.jpalearning.repository.MatchMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoServices {
    @Autowired
    MatchMongoRepo matchMongoRepo;
    MatchMongo matchMongo = new MatchMongo();
    public void addBall(Ball ball, Player batsman , Player bowler ,int inningNum){
        BallMongo ballMongo = new BallMongo(ball.getOutcome(), batsman.getId(), bowler.getId());
        if(inningNum==1){
            ballMongo.setBallNumber(matchMongo.getInning1Details().size()+1);
            matchMongo.getInning1Details().add(ballMongo);
        }
        if(inningNum==2){
            ballMongo.setBallNumber(matchMongo.getInning1Details().size()+1);
            matchMongo.getInning2Details().add(ballMongo);
        }
    }
    public void addMatch(int matchId, int team1Id , int team2Id){
        matchMongo.setMatchId(matchId);
        matchMongo.setTeam1Id(team1Id);
        matchMongo.setTeam2Id(team2Id);
    }
    public void setWinner(int winnerTeamId){
        matchMongo.setWinnerTeamId(winnerTeamId);
        matchMongoRepo.save(matchMongo);
    }

}
