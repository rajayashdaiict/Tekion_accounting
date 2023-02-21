package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.*;
import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CricketServices {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    GameplayService gameplayService;



    public boolean addPlayerIntoTeam(AddPlayerIntoTeamDto addPlayerIntoTeamDto) {

        Optional<Player> player = playerRepository.findById(addPlayerIntoTeamDto.getPlayerId());
        if (player.isEmpty()) {
            return false;
        }

        Optional<Team> team = teamRepository.findById(addPlayerIntoTeamDto.getTeamId());
        if (team.isEmpty()) {
            return false;
        }

        team.get().setPlayers(List.of(player.get()));
//        System.out.println(team.get().getPlayers());
        player.get().setTeam(team.get());
        playerRepository.save(player.get());
        return true;
    }

    public MatchResultDto matchPlay(MatchPlayDto matchPlayDto) {
        //getting teams from their team ids
        Optional<Team> team1 = teamRepository.findById(matchPlayDto.getTeam1Id());
        Optional<Team> team2 = teamRepository.findById(matchPlayDto.getTeam2Id());
        MatchResultDto matchResultDto = new MatchResultDto();
        if(team1.isEmpty()||team2.isEmpty()){
            matchResultDto.setErrorMsg("Teams does not exist");
            return matchResultDto;
        }
        Match match = Match.builder().overs(matchPlayDto.getOvers()).team1(team1.get()).team2(team2.get()).build();
        matchRepository.save(match);

        System.out.println("----checkpoint--- gameplayservice start");

        GameplayDto gameplayDto = new GameplayDto(team1.get(),team2.get(), match.getOvers(),match);
        Team winnerTeam = gameplayService.gameplay(gameplayDto);

        System.out.println("----checkpoint--- gameplayservice done");

        match.setWinner(winnerTeam);

        matchRepository.save(match);
        System.out.println("temp checkpoint");
        matchResultDto.setWinnerTeam(winnerTeam);

        return matchResultDto;
    }

    public Optional<ScoreCardOutputDto> scorecard(int matchId){

        Match match;
        if(matchRepository.findById(matchId).isEmpty()){
            return Optional.empty();
        }
        else
            match=matchRepository.findById(matchId).get();
        ScoreCardOutputDto scoreCardOutputDto = new ScoreCardOutputDto();
        scoreCardOutputDto.setMatchId(matchId);
        scoreCardOutputDto.setTeam1Id(match.getTeam1().getId());
        scoreCardOutputDto.setTeam2Id(match.getTeam2().getId());

        List<Inning> innings = match.getInning();
        for(Inning inning : innings){
            System.out.println(inning.getInningId());
            List<BattingScoreCard> battingScoreCards = inning.getBattingScoreCards();
            for(BattingScoreCard battingScoreCard : battingScoreCards){
                BattingScoreCardOutputDto battingScoreCardOutputDto =
                        new BattingScoreCardOutputDto(battingScoreCard.getBatsman().getName(),
                                inning.getBattingTeam().getName(),battingScoreCard.getRuns(),
                                battingScoreCard.getBalls(), battingScoreCard.isOut(),
                                inning.getInningId().getInningNum());
                scoreCardOutputDto.getBatsmanDetails().add(battingScoreCardOutputDto);
            }

            List<BowlingScoreCard> bowlingScoreCards = inning.getBowlingScoreCards();
            for(BowlingScoreCard bowlingScoreCard : bowlingScoreCards){
                BowlingScoreCardOutputDto bowlingScoreCardOutputDto =
                        new BowlingScoreCardOutputDto(bowlingScoreCard.getBowler().getName(),
                                inning.getBowlingTeam().getName(), bowlingScoreCard.getOvers(),
                                bowlingScoreCard.getRunsGiven(), bowlingScoreCard.getWicketsTaken(),inning.getInningId().getInningNum());
                scoreCardOutputDto.getBowlersDetails().add(bowlingScoreCardOutputDto);
            }
        }
        return Optional.of(scoreCardOutputDto);

    }


    public String tournamentPlay(TournamentDto tournamentDto) {
        //checking if tournament is possible or not
        int totalNumberOfTeams = tournamentDto.getTeamIds().size();
        while(totalNumberOfTeams>0&&totalNumberOfTeams%2==0){
            totalNumberOfTeams=totalNumberOfTeams/2;
        }
        if(totalNumberOfTeams!=0){
            return "tournament not possible";
        }

        //entering a list and getting a list back
        List<Integer> playingTeams;
        return null;
    }
}
