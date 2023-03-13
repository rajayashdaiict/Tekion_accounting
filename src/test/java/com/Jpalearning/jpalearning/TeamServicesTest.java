package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.TeamDto;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.service.TeamServices;
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

import static org.mockito.Mockito.times;

@SpringBootTest
public class TeamServicesTest {
    @Mock
    TeamRepository teamRepository;
    @InjectMocks
    TeamServices teamServices;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTeamTest_ReturnsTrue(){
        AddTeamDto addTeamDto = new AddTeamDto("IND");
        Team team = Team.builder().name("IND").isDeleted(false).build();

        when(teamRepository.save(team)).thenReturn(team);
        Team returnedTeam = teamServices.addTeam(addTeamDto);

        Assertions.assertEquals( "IND", returnedTeam.getName());
        verify(teamRepository,times(1)).save(team);
    }
    @Test
    public void addTeamTest_ReturnsFalse(){
        AddTeamDto addTeamDto = new AddTeamDto("");
        AddTeamDto addTeamDto1 = new AddTeamDto();

        Assertions.assertThrows(RuntimeException.class,()->teamServices.addTeam(addTeamDto));
        Assertions.assertThrows(RuntimeException.class,()->teamServices.addTeam(addTeamDto1));

        verify(teamRepository,never()).save(any(Team.class));
    }

    @Test
    public void getTeam_TeamWithoutPlayers(){
        Team team =
                Team.builder().id(1).name("IND").isDeleted(false).allPlayers(new ArrayList<>()).players(new ArrayList<>()).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        Team team1 = teamServices.getTeam(1);

        Assertions.assertEquals(team1.getId(),1);
        Assertions.assertEquals(team1.getName(),"IND");
    }

    @Test
    public void getTeamTest_NullTeam(){
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class,()->teamServices.getTeam(1));
    }

    @Test
    public void getTeamTest_WithPlayers(){
        Player player1 = Player.builder().name("yash").id(1).build();
        Player player2 = Player.builder().name("virat").id(2).build();
        Team team = Team.builder().id(1).name("IND").players(new ArrayList<>(Arrays.asList(
                player1,player2
        ))).allPlayers(Arrays.asList(
                player1,player2
        )).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        Team team1 = teamServices.getTeam(1);

        Assertions.assertEquals(team1.getName(),"IND");
    }

    @Test
    public void getTeamTest_DeletedTeam(){
        Team team = Team.builder().name("IND").isDeleted(true).id(1).build();
        TeamDto teamDto = TeamDto.builder().isDeleted(true).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        Mockito.when(teamRepository.save(team)).thenReturn(team);

        Team team1 = teamServices.getTeam(1);

        Assertions.assertEquals(team,team1);
    }

    @Test
    public void updateTeamTest_NewTeamCreated(){
        AddTeamDto addTeamDto = new AddTeamDto("IND");

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        TeamServices teamServicesSpy = spy(teamServices);

        doReturn(new Team()).when(teamServicesSpy).addTeam(addTeamDto);
        String result = teamServicesSpy.updateTeam(1, addTeamDto);
        Assertions.assertEquals(result,"new Team made");
        verify(teamServicesSpy,times(1)).addTeam(addTeamDto);

    }

    @Test
    public void updateTeamTest_NewTeamCantbeCreated(){
        AddTeamDto addTeamDto = new AddTeamDto();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        TeamServices teamServicesSpy = spy(teamServices);
        doReturn(null).when(teamServicesSpy).addTeam(addTeamDto);
        String result = teamServicesSpy.updateTeam(1, addTeamDto);
        Assertions.assertEquals(result,"cant make the new team");
        verify(teamServicesSpy,times(1)).addTeam(addTeamDto);
    }

    @Test
    public void updateTeamTest_TeamCatUpdate(){
        Team team = Team.builder().id(1).name("IND").build();
        AddTeamDto addTeamDto = new AddTeamDto();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        TeamServices teamServicesSpy = spy(teamServices);
        String result = teamServicesSpy.updateTeam(1, addTeamDto);
        Assertions.assertEquals(result,"cant update the new team");
        verify(teamServicesSpy,never()).addTeam(addTeamDto);
        verify(teamRepository,never()).save(any());
    }

    @Test
    public void updateTeamTest_TeamUpdated(){
        Team team = Team.builder().id(1).name("IND").build();
        AddTeamDto addTeamDto = new AddTeamDto("AUS");

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        TeamServices teamServicesSpy = spy(teamServices);
        String result = teamServicesSpy.updateTeam(1, addTeamDto);
        Assertions.assertEquals(result,"team updated");

        verify(teamServicesSpy,never()).addTeam(addTeamDto);
        team.setName(addTeamDto.getName());
        verify(teamRepository,times(1)).save(team);
    }

    @Test
    public void DeleteTeamTest_ReturnFalse(){
        int id=1;
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = teamServices.deleteTeam(id);

        Assertions.assertFalse(result);
        verify(teamRepository,never()).save(any());
    }

    @Test
    public void DeleteTeamTest_ReturnTrue_NoPlayers(){
        int id=1;
        Team team = Team.builder().id(1).name("IND").allPlayers(new ArrayList<>()).players(new ArrayList<>()).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        boolean result = teamServices.deleteTeam(1);

        Assertions.assertTrue(result);

        team.setDeleted(true);
        verify(teamRepository,times(1)).save(team);
    }
    @Test
    public void DeleteTeamTest_ReturnTrue_WithPlayers(){
        Player player1 = Player.builder().name("yash").id(1).build();
        Player player2 = Player.builder().name("virat").id(2).build();
        Team team = Team.builder().id(1).name("IND").players(new ArrayList<>(Arrays.asList(
                player1,player2
        ))).allPlayers(Arrays.asList(
                player1,player2
        )).build();
        player1.setTeam(team);
        player2.setTeam(team);

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        boolean result = teamServices.deleteTeam(1);
        Assertions.assertTrue(result);
        Assertions.assertNull(player1.getTeam());
        Assertions.assertNull(player1.getTeam());
        verify(teamRepository,times(1)).save(team);
    }




}
