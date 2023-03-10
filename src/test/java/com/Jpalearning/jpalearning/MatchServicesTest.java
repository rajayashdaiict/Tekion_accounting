package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.MatchCreateDto;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.service.MatchServices;
import com.Jpalearning.jpalearning.service.MongoServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MatchServicesTest {

    @Mock
    PlayerRepository playerRepository;
    @Mock
    TeamRepository teamRepository;
    @Mock
    MatchRepository matchRepository;
    @Mock
    MongoServices mongoServices;
    @InjectMocks
    MatchServices matchServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addPlayersIntoTeamTest_NoPlayersGiven() {
        Team team = Team.builder().name("IND").id(1).build();
        List<Integer> players = new ArrayList<>(Arrays.asList());
        boolean result = matchServices.addPlayersIntoTeam(players, team);
        Assertions.assertFalse(result);
    }

    @Test
    public void addPlayersIntoTeamTest_playersGiven() {
        Team team = Team.builder().name("IND").id(1).build();
        List<Integer> players = new ArrayList<>(Arrays.asList(1, 2));
        Player player = Player.builder().name("dbajk").id(1).build();
        Player player1 = Player.builder().name("dhasb").id(2).build();

        Mockito.when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        Mockito.when(playerRepository.findById(2)).thenReturn(Optional.of(player1));

        boolean result = matchServices.addPlayersIntoTeam(players, team);

        Assertions.assertTrue(result);
        verify(playerRepository, times(players.size())).save(any(Player.class));
        Assertions.assertNotNull(team.getPlayers());
        Assertions.assertNotNull(team.getAllPlayers());
        verify(teamRepository, times(1)).save(any(Team.class));
    }


    @Test
    public void createMatchTest_TeamsNotFound() {
        MatchCreateDto matchCreateDto = new MatchCreateDto();
        matchCreateDto.setTeam1Id(1);
        matchCreateDto.setTeam2Id(2);
        matchCreateDto.setOvers(10);
        matchCreateDto.setPlayerIdsTeam1(new ArrayList<>(Arrays.asList(1, 2)));
        matchCreateDto.setPlayerIdsTeam2(new ArrayList<>(Arrays.asList(3, 4)));
        Player player1 = Player.builder().id(1).name("yash").build();
        Player player2 = Player.builder().id(2).name("djhsa").build();
        Player player3 = Player.builder().id(3).name("dbkja").build();
        Player player4 = Player.builder().id(4).name("bfies").build();
        Team team = Team.builder().id(1).name("IND").build();
        Team team1 = Team.builder().id(2).name("AUS").build();

        Mockito.when(teamRepository.findById(3)).thenReturn(Optional.empty());
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        int matchId = matchServices.createMatch(matchCreateDto);

        Assertions.assertEquals(matchId, 0);
        verify(matchRepository, never()).save(any());
    }

    @Test
    public void createMatchTest_DeletedTeams() {
        MatchCreateDto matchCreateDto = new MatchCreateDto();
        matchCreateDto.setTeam1Id(1);
        matchCreateDto.setTeam2Id(2);
        matchCreateDto.setOvers(10);
        matchCreateDto.setPlayerIdsTeam1(new ArrayList<>(Arrays.asList(1, 2)));
        matchCreateDto.setPlayerIdsTeam2(new ArrayList<>(Arrays.asList(3, 4)));
        Player player1 = Player.builder().id(1).name("yash").build();
        Player player2 = Player.builder().id(2).name("djhsa").build();
        Player player3 = Player.builder().id(3).name("dbkja").build();
        Player player4 = Player.builder().id(4).name("bfies").build();
        Team team = Team.builder().id(1).name("IND").isDeleted(true).build();
        Team team1 = Team.builder().id(2).name("AUS").build();

        Mockito.when(teamRepository.findById(2)).thenReturn(Optional.of(team1));
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        int matchId = matchServices.createMatch(matchCreateDto);
        Assertions.assertEquals(matchId, 0);
        verify(matchRepository, never()).save(any());

    }

    @Test
    public void createMatchTest_expectAMatchId() {
        MatchCreateDto matchCreateDto = new MatchCreateDto();
        matchCreateDto.setTeam1Id(1);
        matchCreateDto.setTeam2Id(2);
        matchCreateDto.setOvers(10);
        matchCreateDto.setPlayerIdsTeam1(new ArrayList<>(Arrays.asList(1, 2)));
        matchCreateDto.setPlayerIdsTeam2(new ArrayList<>(Arrays.asList(3, 4)));
        Player player1 = Player.builder().id(1).name("yash").build();
        Player player2 = Player.builder().id(2).name("djhsa").build();
        Player player3 = Player.builder().id(3).name("dbkja").build();
        Player player4 = Player.builder().id(4).name("bfies").build();
        Team team = Team.builder().id(1).name("IND").build();
        Team team1 = Team.builder().id(2).name("AUS").build();

        Match match = Match.builder().team1(team).team2(team1).overs(matchCreateDto.getOvers()).build();

        MatchServices matchServicesSpy = Mockito.spy(matchServices);

        Mockito.when(teamRepository.findById(2)).thenReturn(Optional.of(team1));
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        doReturn(true).when(matchServicesSpy).addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(), team);
        doReturn(true).when(matchServicesSpy).addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(), team1);


        int result = matchServicesSpy.createMatch(matchCreateDto);

        System.out.println(result);
        verify(matchRepository, times(1)).save(any(Match.class));
        Assertions.assertEquals(result, anyInt());

    }

    @Test
    public void updateMatchTest_NewMatchCreated() {
        MatchCreateDto matchCreateDto = new MatchCreateDto();

        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.empty());

        MatchServices matchServicesspy = spy(matchServices);
        String result = matchServicesspy.updateMatch(matchCreateDto, 1);

        Assertions.assertNotNull(result);

        verify(matchServicesspy, times(1)).createMatch(any(MatchCreateDto.class));

    }

    @Test
    public void updateMatchTest_MatchAlreadyPlayed() {
        MatchCreateDto matchCreateDto = new MatchCreateDto();
        Team team = new Team();
        Match match = Match.builder().id(1).winner(team).build();
        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        MatchServices matchServicesspy = spy(matchServices);
        String result = matchServicesspy.updateMatch(matchCreateDto, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("match is already played", result);

    }

    @Test
    public void updateMatchTest_updateTeams() {
        Team team1 = Team.builder().id(1).name("IND").build();
        Team team2 = Team.builder().id(2).name("AUS").build();

        Match match = Match.builder().id(1).team1(team1).team2(team2).overs(10).build();
        MatchCreateDto matchCreateDto = new MatchCreateDto();
        matchCreateDto.setTeam1Id(1);
        matchCreateDto.setTeam2Id(2);
        matchCreateDto.setOvers(10);

        MatchServices matchServicesSpy = spy(matchServices);

        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.of(match));
        Mockito.when(teamRepository.findById(2)).thenReturn(Optional.of(team2));
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team1));

        doReturn(true).when(matchServicesSpy).addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(), team1);
        doReturn(true).when(matchServicesSpy).addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(), team2);

        String result = matchServicesSpy.updateMatch(matchCreateDto, 1);

        Assertions.assertEquals("match updated",result);
        verify(matchRepository,times(1)).save(any(Match.class));

    }

    @Test
    public void getMatchTest_MatchNotFound() {
        int id = 1;
        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.empty());

        Object match = matchServices.getMatch(1);

        Assertions.assertEquals("no match found", match);

    }

    @Test
    public void getMatchTest_MatchWillBePlayed() {
        int id = 1;
        Team team = Team.builder().id(1).name("IND").build();
        Team team1 = Team.builder().id(2).name("AUS").build();
        Match match = Match.builder().id(1).team1(team1).team2(team).build();

        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        Object result = matchServices.getMatch(1);

        Assertions.assertEquals("match will be playing between AUS and IND", result);

    }

    @Test
    public void getMatchTest_MongoMatchObject() {
        int id = 1;
        Team team = Team.builder().id(1).name("IND").build();
        Match match = Match.builder().id(1).winner(team).build();

        Mockito.when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        Object result = matchServices.getMatch(1);

        verify(mongoServices, times(1)).getMatch(anyInt());

    }

    @Test
    public void deleteMatchTest_MatchNotFound() {
        int id = 1;

        Mockito.when(matchRepository.findById(id)).thenReturn(Optional.empty());

        String result = matchServices.deleteMatch(id);

        Assertions.assertEquals("there is no such match", result);
        verify(matchRepository, never()).deleteById(anyInt());
    }

    @Test
    public void deleteMatchTest_CantDeleteMatch() {
        int id = 1;
        Team team = Team.builder().id(1).name("IND").build();
        Match match = Match.builder().winner(team).build();

        Mockito.when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        String result = matchServices.deleteMatch(id);

        Assertions.assertEquals("I can still remove it if you want though ", result);
        verify(matchRepository, never()).deleteById(anyInt());
    }

    @Test
    public void deleteMatchTest_MatchDeleted() {
        int id = 1;
        Match match = Match.builder().build();

        Mockito.when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        String result = matchServices.deleteMatch(id);

        Assertions.assertEquals("match is removed", result);
        verify(matchRepository, times(1)).deleteById(id);
    }

}
