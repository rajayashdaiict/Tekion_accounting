package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {

    @Autowired
    MongoServices mongoServices;
    Logger logger = LoggerFactory.getLogger(PlayService.class);

    public ScoreCardDto play(InningTeamsDto inningTeamsDto, int overs, ScoreCardDto scoreCardDto) {
        return play(inningTeamsDto, overs , scoreCardDto , null);
    }

    public ScoreCardDto play(InningTeamsDto inningTeamsDto, int overs, ScoreCardDto scoreCardDto, Integer target) {
        logger.debug("inside play service");

        Team battingTeam = inningTeamsDto.getBattingTeam();
        Team bowlingTeam = inningTeamsDto.getBowlingTeam();
        int batsmanNum = 0;

        Player batsman1, batsman2, bowler;
        batsman1 = selectBatsman(battingTeam, batsmanNum++);
        batsman2 = selectBatsman(battingTeam, batsmanNum++);
        bowler = selectBowler(bowlingTeam);
        for (BowlingScoreCardDto bowlingScoreCardDto : scoreCardDto.getBowlingScoreCardDtolist()) {
            if (bowlingScoreCardDto.getPlayer() == bowler) {
                bowlingScoreCardDto.setOversBowled(bowlingScoreCardDto.getOversBowled() + 1);
            }
        }

        logger.debug("starting the match simulation");

        for (int i = 0; i < overs; i++) {
            for (int j = 0; j < 6; j++) {
                Ball ball = new Ball();
                if(target==null)
                    addBall(ball, batsman1, bowler, scoreCardDto,1);
                else
                    addBall(ball,batsman1,bowler,scoreCardDto,2);
                if (ball.getOutcome() == 7) {
                    batsman1 = selectBatsman(battingTeam, batsmanNum++);
                    if (batsman1 == null) {
                        return scoreCardDto;
                    }
                } else {
                    if (ball.getOutcome() % 2 != 0) {
                        switchStrick(batsman1, batsman2);
                    }
                    if (target != null && target < scoreCardDto.getRuns()) {
                        return scoreCardDto;
                    }
                }
            }
            switchStrick(batsman1, batsman2);
            bowler = selectBowler(bowlingTeam, bowler);
            for (BowlingScoreCardDto bowlingScoreCardDto : scoreCardDto.getBowlingScoreCardDtolist()) {
                if (bowlingScoreCardDto.getPlayer() == bowler) {
                    bowlingScoreCardDto.setOversBowled(bowlingScoreCardDto.getOversBowled() + 1);
                }
            }
        }
        logger.info("play service done simulation");
        return scoreCardDto;
    }

    private void addBall(Ball ball, Player batsman1, Player bowler, ScoreCardDto scoreCardDto,int inningNum) {
        mongoServices.addBall(ball,batsman1,bowler,inningNum);
        //batsman update
        for (BattingScoreCardDto battingScoreCardDto : scoreCardDto.getBattingScoreCardDtolist()) {
            if (battingScoreCardDto.getPlayer() == batsman1) {

                if (ball.getOutcome() == 7) {
                    battingScoreCardDto.setOut(true);
                    battingScoreCardDto.setBalls(battingScoreCardDto.getBalls() + 1);
                } else {
                    battingScoreCardDto.setRuns(battingScoreCardDto.getRuns() + ball.getOutcome());
                    battingScoreCardDto.setBalls(battingScoreCardDto.getBalls() + 1);
                    scoreCardDto.setRuns(scoreCardDto.getRuns() + ball.getOutcome());
                }
            }
        }
        //bowler update
        for (BowlingScoreCardDto bowlingScoreCardDto : scoreCardDto.getBowlingScoreCardDtolist()) {
            if (bowlingScoreCardDto.getPlayer() == bowler) {
                if (ball.getOutcome() == 7) {
                    bowlingScoreCardDto.setWicketsTaken(bowlingScoreCardDto.getWicketsTaken() + 1);
                    scoreCardDto.setWickets(scoreCardDto.getWickets() + 1);
                } else {
                    bowlingScoreCardDto.setRunsGiven(bowlingScoreCardDto.getRunsGiven() + ball.getOutcome());
                }
            }
        }

    }

    public Player selectBatsman(Team battingTeam, int batsmanNum) {
        if (batsmanNum >= battingTeam.getPlayers().size()) {
            return null;
        }
        return battingTeam.getPlayers().get(batsmanNum);
    }

    public Player selectBowler(Team bowlingTeam) {
        int teamSize = bowlingTeam.getPlayers().size();
        int randomIndex = (int) (Math.ceil(Math.random() * teamSize) - 1);
        return bowlingTeam.getPlayers().get(randomIndex);
    }

    public Player selectBowler(Team bowlingTeam, Player lastBowler) {
        Player bowler = selectBowler(bowlingTeam);
        if (bowler == lastBowler) {
            return selectBowler(bowlingTeam, lastBowler);
        } else {
            return bowler;
        }
    }

    public void switchStrick(Player batsman1, Player batsman2) {
        Player c = batsman2;
        batsman2 = batsman1;
        batsman1 = c;
    }


}
