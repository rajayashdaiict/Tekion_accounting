package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.BallObject;
import com.Jpalearning.jpalearning.Entity.MatchES;
import com.Jpalearning.jpalearning.Entity.MatchMongo;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.Ball;
import com.Jpalearning.jpalearning.repository.MatchEsRepo;
import com.Jpalearning.jpalearning.repository.MatchMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoServices {
    @Autowired
    MatchEsRepo matchEsRepo;
    @Autowired
    MatchMongoRepo matchMongoRepo;

    MatchMongo matchMongo = new MatchMongo();
    MatchES matchES = new MatchES();
    public void addBall(Ball ball, Player batsman , Player bowler ,int inningNum){
        BallObject ballObject = new BallObject(ball.getOutcome(), batsman.getId(), bowler.getId());
        if(inningNum==1){
            ballObject.setBallNumber(matchMongo.getInning1Details().size() + 1);
            matchMongo.getInning1Details().add(ballObject);
            matchES.getInning1Details().add(ballObject);
        }
        if(inningNum==2){
            ballObject.setBallNumber(matchMongo.getInning2Details().size() + 1);
            matchMongo.getInning2Details().add(ballObject);
            matchES.getInning2Details().add(ballObject);
        }
    }
    public void addMatch(int matchId, int team1Id , int team2Id){
        matchMongo.setMatchId(matchId);
        matchMongo.setTeam1Id(team1Id);
        matchMongo.setTeam2Id(team2Id);
        matchES.setMatchId(matchId);
        matchES.setMatchId(team1Id);
        matchES.setTeam2Id(team2Id);
    }
    public void setWinner(int winnerTeamId){
        matchMongo.setWinnerTeamId(winnerTeamId);
        matchMongoRepo.save(matchMongo);
        matchES.setWinnerTeamId(winnerTeamId);
        matchEsRepo.save(matchES);
    }
    public Optional<MatchMongo> getMatch(int matchId){
        Optional<MatchMongo> byMatchId = matchMongoRepo.findByMatchId(matchId);
        return byMatchId;
    }

    public Optional<MatchES> getMatchES(int matchId){
        Optional<MatchES> matchEs = matchEsRepo.findById(matchId);
        return matchEs;
    }


}
