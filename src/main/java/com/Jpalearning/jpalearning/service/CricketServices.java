package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.*;
import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(CricketServices.class);

    public boolean addPlayerIntoTeam(AddPlayerIntoTeamDto addPlayerIntoTeamDto) {

        logger.debug("adding player {} into team {}", addPlayerIntoTeamDto.getPlayerId(),
                addPlayerIntoTeamDto.getTeamId());
        Optional<Player> player = playerRepository.findById(addPlayerIntoTeamDto.getPlayerId());
        logger.debug("validation started for adding player into team");
        if (player.isEmpty()) {
            logger.error("invalid player");
            return false;
        }
        if (player.get().isDeleted()) {
            logger.error("invalid player");
            return false;
        }

        Optional<Team> team = teamRepository.findById(addPlayerIntoTeamDto.getTeamId());
        if (team.isEmpty() || team.get().isDeleted()) {
            logger.error("invalid team");
            return false;
        }
        logger.debug("validation done for player and team");
        team.get().getAllPlayers().add(player.get());
        player.get().setTeam(team.get());
        playerRepository.save(player.get());
        logger.info("adding player into team successfully");
        return true;
    }

    public MatchResultDto matchPlay(int matchId) {
        logger.info("starting a match {}",matchId);
        Optional<Match> match = matchRepository.findById(matchId);
        MatchResultDto matchResultDto = new MatchResultDto();
        logger.debug("validation started for match");
        if (match.isEmpty()) {
            matchResultDto.setErrorMsg("no match found");
            return matchResultDto;
        }
        if (match.get().getWinner() != null) {
            matchResultDto.setErrorMsg("match already played");
            matchResultDto.setWinnerTeam(match.get().getWinner());
            return matchResultDto;
        }

        if (match.get().getTeam1().getPlayers().size() < 3 || match.get().getTeam2().getPlayers().size() < 3) {
            matchResultDto.setErrorMsg("not enough players to play");
            return matchResultDto;
        }

        logger.debug("validation done for match");

        GameplayDto gameplayDto = new GameplayDto(match.get().getTeam1(), match.get().getTeam2(),
                match.get().getOvers(), match.get());
        logger.debug("gameplay service called");
        Team winnerTeam = gameplayService.gameplay(gameplayDto);
        logger.debug("gameplay service finished");
        match.get().setWinner(winnerTeam);

        matchRepository.save(match.get());
        logger.info("match successfully saved");
        matchResultDto.setWinnerTeam(winnerTeam);

        return matchResultDto;

    }

    public Optional<ScoreCardOutputDto> scorecard(int matchId) {
        logger.info("fetching scorecard for match {}",matchId);
        logger.debug("validation started for match/scorecard");
        Match match;
        if (matchRepository.findById(matchId).isEmpty()) {
            logger.error("invalid match");
            return Optional.empty();
        } else {
            match = matchRepository.findById(matchId).get();
        }
        ScoreCardOutputDto scoreCardOutputDto = new ScoreCardOutputDto();
        scoreCardOutputDto.setMatchId(matchId);
        scoreCardOutputDto.setTeam1Id(match.getTeam1().getId());
        scoreCardOutputDto.setTeam2Id(match.getTeam2().getId());

        if (match.getWinner() == null) {
            logger.error("match hasnt played yet");
            return Optional.of(scoreCardOutputDto);
        }

        logger.debug("validation done for match/scorecard");
        List<Inning> innings = match.getInning();
        for (Inning inning : innings) {
            System.out.println(inning.getInningId());
            List<BattingScoreCard> battingScoreCards = inning.getBattingScoreCards();
            for (BattingScoreCard battingScoreCard : battingScoreCards) {
                BattingScoreCardOutputDto battingScoreCardOutputDto = new BattingScoreCardOutputDto(
                        battingScoreCard.getBatsman().getName(), inning.getBattingTeam().getName(),
                        battingScoreCard.getRuns(), battingScoreCard.getBalls(), battingScoreCard.isOut(),
                        inning.getInningId().getInningNum());
                scoreCardOutputDto.getBatsmanDetails().add(battingScoreCardOutputDto);
            }

            List<BowlingScoreCard> bowlingScoreCards = inning.getBowlingScoreCards();
            for (BowlingScoreCard bowlingScoreCard : bowlingScoreCards) {
                BowlingScoreCardOutputDto bowlingScoreCardOutputDto = new BowlingScoreCardOutputDto(
                        bowlingScoreCard.getBowler().getName(), inning.getBowlingTeam().getName(),
                        bowlingScoreCard.getOvers(), bowlingScoreCard.getRunsGiven(),
                        bowlingScoreCard.getWicketsTaken(), inning.getInningId().getInningNum());
                scoreCardOutputDto.getBowlersDetails().add(bowlingScoreCardOutputDto);
            }
        }
        logger.info("scorecard is done");
        return Optional.of(scoreCardOutputDto);

    }

}
