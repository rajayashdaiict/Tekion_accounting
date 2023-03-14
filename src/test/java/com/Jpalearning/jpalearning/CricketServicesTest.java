package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddPlayerIntoTeamDto;
import com.Jpalearning.jpalearning.dto.GameplayDto;
import com.Jpalearning.jpalearning.dto.MatchResultDto;
import com.Jpalearning.jpalearning.dto.ScoreCardOutputDto;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CricketServicesTest {

    @Mock
    GameplayService gameplayService;
    @Mock
    PlayerServices playerServices;
    @Mock
    TeamServices teamServices;
    @Mock
    MatchServices matchServices;
    @InjectMocks
    CricketServices cricketServices;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addPlayerIntoTeamTest_invalidPlayer(){

        Player player = Player.builder().name("yash").id(2).isDeleted(true).build();

        Mockito.when(playerServices.findById(1)).thenReturn(Optional.empty());
        Mockito.when(playerServices.findById(2)).thenReturn(Optional.of(player));

        AddPlayerIntoTeamDto addPlayerIntoTeamDto1 = new AddPlayerIntoTeamDto(1,1);
        AddPlayerIntoTeamDto addPlayerIntoTeamDto2 = new AddPlayerIntoTeamDto(2,1);

        boolean result1 = cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto1);
        boolean result2 = cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto2);

        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
        verify(playerServices,never()).save(any(Player.class));


    }
    @Test
    public void addPlayerIntoTeamTest_invalidTeam(){
        Player player = Player.builder().name("yash").id(1).build();
        Team team = Team.builder().name("IND").id(1).isDeleted(true).build();

        Mockito.when(playerServices.findById(1)).thenReturn(Optional.of(player));
        Mockito.when(teamServices.findById(1)).thenReturn(Optional.of(team));
        Mockito.when(teamServices.findById(2)).thenReturn(Optional.empty());

        AddPlayerIntoTeamDto addPlayerIntoTeamDto1 = new AddPlayerIntoTeamDto(1,1);
        AddPlayerIntoTeamDto addPlayerIntoTeamDto2 = new AddPlayerIntoTeamDto(1,1);

        boolean result1 = cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto1);
        boolean result2 = cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto2);

        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
        verify(playerServices,never()).save(any(Player.class));

    }
    @Test
    public void addPlayerIntoTeamTest_ReturnTrue(){
        Player player = Player.builder().name("yash").id(1).build();
        Team team = Team.builder().name("IND").id(1).players(new ArrayList<>()).allPlayers(new ArrayList<>()).build();

        Mockito.when(playerServices.findById(1)).thenReturn(Optional.of(player));
        Mockito.when(teamServices.findById(1)).thenReturn(Optional.of(team));

        AddPlayerIntoTeamDto addPlayerIntoTeamDto = new AddPlayerIntoTeamDto(1,1);
        boolean result = cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto);

        Assertions.assertTrue(result);
        Assertions.assertNotNull(team.getAllPlayers());
        Assertions.assertEquals(team,player.getTeam());
        verify(playerServices,times(1)).save(player);
    }

    @Test
    public void matchPlayTest_NoMatchFound(){
        int id = 1;
        when(matchServices.findById(id)).thenReturn(Optional.empty());

        MatchResultDto matchResultDto = cricketServices.matchPlay(id);

        Assertions.assertNotNull(matchResultDto);
        Assertions.assertEquals("no match found",matchResultDto.getErrorMsg());
    }
    @Test
    public void matchPlayTest_MatchAlreadyPlayed(){

        int id =1;
        Team team = Team.builder().name("IND").id(1).build();
        Match match = Match.builder().id(id).winner(team).build();

        when(matchServices.findById(id)).thenReturn(Optional.ofNullable(match));

        MatchResultDto matchResultDto = cricketServices.matchPlay(id);
        Assertions.assertNotNull(matchResultDto);
        Assertions.assertEquals(matchResultDto.getWinnerTeam(),team);

    }
    @Test
    public void matchPlayTest_NotEnoughPlayers(){

        int id =1;
        Team team = Team.builder().name("IND").id(1).allPlayers(new ArrayList<>()).players(new ArrayList<>()).build();
        Team team1 = Team.builder().name("UAS").id(2).allPlayers(new ArrayList<>()).players(new ArrayList<>()).build();

        Match match = Match.builder().team2(team1).team1(team).id(id).build();

        when(matchServices.findById(id)).thenReturn(Optional.ofNullable(match));

        MatchResultDto matchResultDto = cricketServices.matchPlay(id);

        Assertions.assertNotNull(matchResultDto);
        Assertions.assertNull(matchResultDto.getWinnerTeam());
        Assertions.assertEquals(matchResultDto.getErrorMsg(),"not enough players to play");

    }
    @Test
    public void matchPlayTest_CheckWinner(){
        int id =1;
        Team team = Team.builder().name("IND").id(1).build();
        Team team1 = Team.builder().name("UAS").id(2).build();

        List<Player> playerList1 = new ArrayList<>(Arrays.asList(
                new Player(),
                new Player(),
                new Player()
        ));
        List<Player> playerList2 = new ArrayList<>(Arrays.asList(
                new Player(),
                new Player(),
                new Player()
        ));

        team.setPlayers(playerList1);
        team1.setPlayers(playerList2);

        Match match = Match.builder().team2(team1).team1(team).overs(10).id(id).build();

        GameplayDto gameplayDto = new GameplayDto(team,team1,10,match);

        when(matchServices.findById(id)).thenReturn(Optional.ofNullable(match));
        when(gameplayService.gameplay(gameplayDto)).thenReturn(team);

        MatchResultDto matchResultDto = cricketServices.matchPlay(id);
        Assertions.assertNotNull(matchResultDto);
        Assertions.assertEquals(team,matchResultDto.getWinnerTeam());
        verify(gameplayService,times(1)).gameplay(gameplayDto);
        verify(matchServices,times(1)).save(match);

    }
    @Test
    public void scoreCardTest_InvalidMatch(){
        int id = 1;
        when(matchServices.findById(id)).thenReturn(Optional.empty());

        Optional<ScoreCardOutputDto> scorecard = cricketServices.scorecard(id);

        Assertions.assertEquals(Optional.empty(),scorecard);
    }

    @Test
    public void scoreCardTest_ValidMatch(){
        int id =1;
        Team team = Team.builder().name("IND").id(1).build();
        Team team1 = Team.builder().name("UAS").id(2).build();

        Match match = Match.builder().team2(team1).team1(team).id(id).build();

        when(matchServices.findById(id)).thenReturn(Optional.ofNullable(match));

        Optional<ScoreCardOutputDto> scorecard = cricketServices.scorecard(id);

        Assertions.assertTrue(scorecard.isPresent());

    }
}
