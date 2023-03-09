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
        boolean result = teamServices.addTeam(addTeamDto);

        Assertions.assertTrue(result);
        verify(teamRepository,times(1)).save(team);
    }
    @Test
    public void addTeamTest_ReturnsFalse(){
        AddTeamDto addTeamDto = new AddTeamDto("");
        AddTeamDto addTeamDto1 = new AddTeamDto();

        boolean result1 = teamServices.addTeam(addTeamDto);
        boolean result2 = teamServices.addTeam(addTeamDto1);

        Assertions.assertFalse(result1||result2);
        verify(teamRepository,never()).save(any(Team.class));
    }

    @Test
    public void getTeam_TeamWithoutPlayers(){
        Team team = Team.builder().id(1).name("IND").isDeleted(false).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        Optional<TeamDto> teamDto = teamServices.getTeam(1);

        Assertions.assertEquals(teamDto.get().getId(),1);
        Assertions.assertEquals(teamDto.get().getCurrentPlayerIds(), new ArrayList<>());
        Assertions.assertEquals(teamDto.get().getName(),"IND");
    }

    @Test
    public void getTeamTest_NullTeam(){
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertEquals(teamServices.getTeam(1),Optional.empty());
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

        Optional<TeamDto> teamDto = teamServices.getTeam(1);

        Assertions.assertEquals(teamDto.get().getName(),"IND");
        Assertions.assertEquals(teamDto.get().getCurrentPlayerIds(),new ArrayList<>(Arrays.asList(
                player1.getId(),player2.getId()
        )));
        Assertions.assertEquals(teamDto.get().getAllPlayerIds(), new ArrayList<>(Arrays.asList(
                player1.getId(),player2.getId()
        )));
    }

    @Test
    public void getTeamTest_DeletedTeam(){
        Team team = Team.builder().name("IND").isDeleted(true).id(1).build();
        TeamDto teamDto = TeamDto.builder().isDeleted(true).build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        Optional<TeamDto> result = teamServices.getTeam(1);

        Assertions.assertEquals(result,Optional.of(teamDto));
    }

    @Test
    public void updateTeamTest_NewTeamCreated(){
        AddTeamDto addTeamDto = new AddTeamDto("IND");

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        TeamServices teamServicesSpy = spy(teamServices);
        String result = teamServicesSpy.updateTeam(1, addTeamDto);
        Assertions.assertEquals(result,"new Team made");
        verify(teamServicesSpy,times(1)).addTeam(addTeamDto);

    }

    @Test
    public void updateTeamTest_NewTeamCantbeCreated(){
        AddTeamDto addTeamDto = new AddTeamDto();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        TeamServices teamServicesSpy = spy(teamServices);
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
        Team team = Team.builder().id(1).name("IND").build();

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
