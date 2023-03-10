package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.*;
import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.repository.BattingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.BowlingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.InningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameplayService {

    @Autowired
    Helper helper;
    @Autowired
    PlayService playService;
    @Autowired
    InningRepository inningRepository;
    @Autowired
    BattingScoreCardRepository battingScoreCardRepository;
    @Autowired
    BowlingScoreCardRepository bowlingScoreCardRepository;
    @Autowired
    MongoServices mongoServices;

    Logger logger = LoggerFactory.getLogger(GameplayService.class);

    public Team gameplay(GameplayDto gameplayDto) {

        logger.info("match playing started");
        logger.debug("inside the gameplay service");

        mongoServices.addMatch(gameplayDto.getMatch().getId(), gameplayDto.getTeam1().getId(),
                gameplayDto.getTeam2().getId());

        InningTeamsDto inningTeamsDto = helper.toss(gameplayDto);
        logger.debug("toss done successfully");

        ScoreCardDto scoreCardDto = new ScoreCardDto(inningTeamsDto.getBattingTeam(), inningTeamsDto.getBowlingTeam());

        logger.debug("inning 1 started playing");
        ScoreCardDto inning1ScoreCardDto = playService.play(inningTeamsDto, gameplayDto.getOvers(), scoreCardDto);


        Inning inning1 = Inning.builder().inningId(new InningId(gameplayDto.getMatch().getId(), 1))
                               .match(gameplayDto.getMatch()).battingTeam(inningTeamsDto.getBattingTeam())
                               .bowlingTeam(inningTeamsDto.getBowlingTeam()).totalRuns(inning1ScoreCardDto.getRuns())
                               .build();
        inningRepository.save(inning1);
        for (BattingScoreCardDto battingScoreCardDto : inning1ScoreCardDto.getBattingScoreCardDtolist()) {
            BattingScoreCard battingScoreCard = BattingScoreCard.builder().batsman(battingScoreCardDto.getPlayer())
                                                                .balls(battingScoreCardDto.getBalls())
                                                                .isOut(battingScoreCardDto.isOut())
                                                                .runs(battingScoreCardDto.getRuns()).inning(inning1)
                                                                .build();
            battingScoreCardRepository.save(battingScoreCard);
        }
        for (BowlingScoreCardDto bowlingScoreCardDto : inning1ScoreCardDto.getBowlingScoreCardDtolist()) {
            BowlingScoreCard bowlingScoreCard = BowlingScoreCard.builder().inning(inning1)
                                                                .overs(bowlingScoreCardDto.getOversBowled())
                                                                .runsGiven(bowlingScoreCardDto.getRunsGiven())
                                                                .wicketsTaken(bowlingScoreCardDto.getWicketsTaken())
                                                                .bowler(bowlingScoreCardDto.getPlayer()).build();
            bowlingScoreCardRepository.save(bowlingScoreCard);
        }

        logger.debug("inning 1 done playing and saving data of inning 1");
        logger.info("inning 1 done");
        inningTeamsDto = helper.swapTeams(inningTeamsDto);

        ScoreCardDto scoreCardDto1 = new ScoreCardDto(inningTeamsDto.getBattingTeam(), inningTeamsDto.getBowlingTeam());

        logger.debug("inning 2 started playing");
        ScoreCardDto inning2ScoreCardDto = playService.play(inningTeamsDto, gameplayDto.getOvers(), scoreCardDto1,
                inning1ScoreCardDto.getRuns() + 1);


        Inning inning2 = Inning.builder().inningId(new InningId(gameplayDto.getMatch().getId(), 2))
                               .battingTeam(inningTeamsDto.getBattingTeam())
                               .bowlingTeam(inningTeamsDto.getBowlingTeam()).totalRuns(inning1ScoreCardDto.getRuns())
                               .match(gameplayDto.getMatch()).build();
        inningRepository.save(inning2);

        for (BattingScoreCardDto battingScoreCardDto : inning2ScoreCardDto.getBattingScoreCardDtolist()) {
            BattingScoreCard battingScoreCard = BattingScoreCard.builder().batsman(battingScoreCardDto.getPlayer())
                                                                .balls(battingScoreCardDto.getBalls())
                                                                .isOut(battingScoreCardDto.isOut())
                                                                .runs(battingScoreCardDto.getRuns()).inning(inning2)
                                                                .build();
            battingScoreCardRepository.save(battingScoreCard);
        }
        for (BowlingScoreCardDto bowlingScoreCardDto : inning2ScoreCardDto.getBowlingScoreCardDtolist()) {
            BowlingScoreCard bowlingScoreCard = BowlingScoreCard.builder().inning(inning2)
                                                                .overs(bowlingScoreCardDto.getOversBowled())
                                                                .runsGiven(bowlingScoreCardDto.getRunsGiven())
                                                                .wicketsTaken(bowlingScoreCardDto.getWicketsTaken())
                                                                .bowler(bowlingScoreCardDto.getPlayer()).build();
            bowlingScoreCardRepository.save(bowlingScoreCard);
        }

        logger.debug("inning 2 done playing and saving data of inning 2");
        logger.info("inning 2 done");

        if (inning1ScoreCardDto.getRuns() > inning2ScoreCardDto.getRuns()) {
            logger.debug("data saving in mongo done");
            mongoServices.setWinner(inning1ScoreCardDto.getBattingTeam().getId());
            return inning1ScoreCardDto.getBattingTeam();
        } else {
            logger.debug("data saving in mongo done");
            mongoServices.setWinner(inning2ScoreCardDto.getBattingTeam().getId());
            return inning2ScoreCardDto.getBattingTeam();
        }

    }
}
