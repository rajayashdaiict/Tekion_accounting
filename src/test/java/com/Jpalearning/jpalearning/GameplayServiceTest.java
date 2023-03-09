package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Inning;
import com.Jpalearning.jpalearning.Entity.InningId;
import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.GameplayDto;
import com.Jpalearning.jpalearning.dto.InningTeamsDto;
import com.Jpalearning.jpalearning.dto.ScoreCardDto;
import com.Jpalearning.jpalearning.repository.BattingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.BowlingScoreCardRepository;
import com.Jpalearning.jpalearning.repository.InningRepository;
import com.Jpalearning.jpalearning.service.GameplayService;
import com.Jpalearning.jpalearning.service.Helper;
import com.Jpalearning.jpalearning.service.MongoServices;
import com.Jpalearning.jpalearning.service.PlayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameplayServiceTest {
    @Mock
    Helper helper;
    @Mock
    PlayService playService;
    @Mock
    InningRepository inningRepository;
    @Mock
    BattingScoreCardRepository battingScoreCardRepository;
    @Mock
    BowlingScoreCardRepository bowlingScoreCardRepository;
    @Mock
    MongoServices mongoServices;
    @InjectMocks
    GameplayService gameplayService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void gamePlayTest() {
        Team team1 = Team.builder().id(1).name("IND").build();
        Team team2 = Team.builder().id(2).name("AUS").build();
        Match match = Match.builder().id(1).team1(team1).overs(10).team2(team2).build();
        GameplayDto gameplayDto = new GameplayDto(team1, team2, 10, match);

        InningTeamsDto inningTeamsDto = new InningTeamsDto(team1, team2);
        InningTeamsDto inningTeamsDtoSwaped = new InningTeamsDto(team2, team1);
        ScoreCardDto scoreCardDtoInning1 = new ScoreCardDto(inningTeamsDto.getBattingTeam(),
                inningTeamsDto.getBowlingTeam());

        ScoreCardDto scoreCardDtoInning2 = new ScoreCardDto(inningTeamsDtoSwaped.getBattingTeam(),
                inningTeamsDtoSwaped.getBowlingTeam());

        Inning inning1 = Inning.builder().inningId(new InningId(1, 1)).battingTeam(inningTeamsDto.getBattingTeam())
                               .bowlingTeam(inningTeamsDto.getBowlingTeam()).match(match).build();

        Inning inning2 = Inning.builder().inningId(new InningId(1, 2))
                               .battingTeam(inningTeamsDtoSwaped.getBattingTeam())
                               .bowlingTeam(inningTeamsDtoSwaped.getBowlingTeam()).match(match).build();


        when(helper.toss(any())).thenReturn(inningTeamsDto);
        when(playService.play(inningTeamsDto, match.getOvers(),scoreCardDtoInning1)).thenReturn(scoreCardDtoInning1);
        when(playService.play(inningTeamsDtoSwaped,match.getOvers(),scoreCardDtoInning2,1)).thenReturn(scoreCardDtoInning2);
        when(helper.swapTeams(inningTeamsDto)).thenReturn(inningTeamsDtoSwaped);

        Team result = gameplayService.gameplay(gameplayDto);

        verify(mongoServices,times(1)).addMatch(1,1,2);
        verify(helper,times(1)).toss(gameplayDto);
        verify(helper,times(1)).swapTeams(inningTeamsDto);
        verify(inningRepository,times(1)).save(inning1);
        verify(inningRepository,times(1)).save(inning2);
        verify(mongoServices,times(1)).setWinner(anyInt());

    }

}
