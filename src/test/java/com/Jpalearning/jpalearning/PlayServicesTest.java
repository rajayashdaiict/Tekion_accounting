package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.InningTeamsDto;
import com.Jpalearning.jpalearning.dto.ScoreCardDto;
import com.Jpalearning.jpalearning.service.MongoServices;
import com.Jpalearning.jpalearning.service.PlayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PlayServicesTest {

    @Mock
    MongoServices mongoServices;
    @InjectMocks
    PlayService playService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void playTest_NULLTarget(){
        Team team1 = Team.builder().name("IND").id(1).build();
        Team team2 = Team.builder().name("AUS").id(2).build();
        Player player1 = Player.builder().id(1).name("yash").build();
        Player player2 = Player.builder().id(2).name("yash").build();
        Player player3 = Player.builder().id(3).name("yash").build();
        Player player4 = Player.builder().id(4).name("yash").build();
        Player player5 = Player.builder().id(5).name("yash").build();
        Player player6 = Player.builder().id(6).name("yash").build();

        List<Player> team1List = new ArrayList<>(Arrays.asList(
                player1,player2,player3
        ));
        List<Player> team2List = new ArrayList<>(Arrays.asList(
                player4,player5,player6
        ));

        team1.setPlayers(team1List);
        team2.setPlayers(team2List);

        InningTeamsDto inningTeamsDto = new InningTeamsDto(team1,team2);

        ScoreCardDto scoreCardDto = new ScoreCardDto(inningTeamsDto.getBattingTeam(),inningTeamsDto.getBowlingTeam());

        PlayService playServiceSpy = Mockito.spy(playService);

        ScoreCardDto result = playServiceSpy.play(inningTeamsDto, 10, scoreCardDto);

        Mockito.verify(playServiceSpy,Mockito.atLeastOnce()).selectBowler(inningTeamsDto.getBowlingTeam());
        Mockito.verify(playServiceSpy, Mockito.atLeastOnce()).selectBatsman(Mockito.eq(inningTeamsDto.getBattingTeam()),
                Mockito.anyInt());


    }
}
