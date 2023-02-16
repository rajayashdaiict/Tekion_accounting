package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.*;
import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.repository.BattingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.BowlingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.InningRepository;
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

    public Team gameplay(GameplayDto gameplayDto) {

        InningTeamsDto inningTeamsDto = helper.toss(gameplayDto);

        System.out.println("-------checkpoint----- toss done");
        System.out.println(inningTeamsDto.getBattingTeam().getName());

        ScoreCardDto scoreCardDto = new ScoreCardDto(inningTeamsDto.getBattingTeam(), inningTeamsDto.getBowlingTeam());

        System.out.println("hello");

        System.out.println("----checkpoint--- playservice started for inning 1");

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

        System.out.println("----checkpoint--- playservice finished for inning 1");

        helper.swapTeams(inningTeamsDto);

        ScoreCardDto scoreCardDto1 = new ScoreCardDto(inningTeamsDto.getBattingTeam(), inningTeamsDto.getBowlingTeam());


        System.out.println("----checkpoint--- playservice started for inning 2");

        ScoreCardDto inning2ScoreCardDto = playService.play(inningTeamsDto, gameplayDto.getOvers(), scoreCardDto1,
                inning1ScoreCardDto.getRuns() + 1);

        Inning inning2 = Inning.builder().inningId(new InningId(gameplayDto.getMatch().getId(), 2))
                               .battingTeam(inningTeamsDto.getBattingTeam())
                               .bowlingTeam(inningTeamsDto.getBowlingTeam()).totalRuns(inning1ScoreCardDto.getRuns())
                               .match(gameplayDto.getMatch()).build();
        inningRepository.save(inning2);


        System.out.println("----checkpoint--- playservice finished for inning 2");

        if (inning1ScoreCardDto.getRuns() > inning2ScoreCardDto.getRuns()) {
            return inning1ScoreCardDto.getBattingTeam();
        } else {
            return inning2ScoreCardDto.getBattingTeam();
        }

    }
}
