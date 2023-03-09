package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.service.PlayerServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.times;

@SpringBootTest
public class PlayerControllerTest {

    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    PlayerServices playerServices;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getPlayerTest(){
        Player player = Player.builder().name("yash").id(1).isDeleted(false).build();
        Player player1 = Player.builder().name("yash1").id(2).isDeleted(false).build();
        Team team = Team.builder().name("IND").id(1).build();
        player1.setTeam(team);
        Player player2 = Player.builder().name("yash2").id(3).isDeleted(true).build();

        PlayerDto playerDto = PlayerDto.builder().name("yash").id(1).build();
        PlayerDto playerDto1 = PlayerDto.builder().name("yash1").id(2).teamName("IND").build();
        PlayerDto playerDto2 = PlayerDto.builder().isDeleted(true).build();

        Mockito.when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        Mockito.when(playerRepository.findById(2)).thenReturn(Optional.of(player1));
        Mockito.when(playerRepository.findById(4)).thenReturn(Optional.empty());
        Mockito.when(playerRepository.findById(3)).thenReturn(Optional.of(player2));

        Optional<PlayerDto> resultPlayer = playerServices.getPlayer(1);
        Optional<PlayerDto> resultPlayer1 = playerServices.getPlayer(2);
        Optional<PlayerDto> resultPlayer2 = playerServices.getPlayer(4);
        Optional<PlayerDto> resultPlayer3 = playerServices.getPlayer(3);

        Assertions.assertEquals(resultPlayer,Optional.of(playerDto));
        Assertions.assertEquals(resultPlayer1,Optional.of(playerDto1));
        Assertions.assertEquals(resultPlayer2,Optional.empty());
        Assertions.assertEquals(resultPlayer3,Optional.of(playerDto2));
    }

    @Test
    public void postPlayerTest(){
        AddPlayerDto addPlayerDto = new AddPlayerDto("Yash");
        Player player = Player.builder().name("Yash").build();
        AddPlayerDto addPlayerDto1 = new AddPlayerDto("");
        Player player1 = Player.builder().name("").build();

        Mockito.when(playerRepository.save(player)).thenReturn(player);
        Mockito.when(playerRepository.save(player1)).thenReturn(player1);

        boolean result = playerServices.addPlayer(addPlayerDto);
        boolean result1 = playerServices.addPlayer(addPlayerDto1);

        Assertions.assertTrue(result);
        Assertions.assertFalse(result1);
    }

    @Test
    public void updatePlayerTest(){
        AddPlayerDto addPlayerDto = new AddPlayerDto("Yash");
        Player player = Player.builder().name("Utsav").id(1).build();
        AddPlayerDto addPlayerDto1 = new AddPlayerDto("");

        Mockito.when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        Mockito.when(playerRepository.findById(2)).thenReturn(Optional.empty());

        Mockito.when(playerRepository.save(player)).thenReturn(player);

        String result1 = playerServices.updatePlayer(1, addPlayerDto);
        String result2 = playerServices.updatePlayer(2, addPlayerDto);
        String result3 = playerServices.updatePlayer(3, addPlayerDto1);
        String result4 = playerServices.updatePlayer(1, addPlayerDto1);

        Assertions.assertEquals(result1,"player updated");
        Assertions.assertEquals(result2,"new player created");
        Assertions.assertEquals(result3,"cant update player");
        Assertions.assertEquals(result4,"cant update player");
    }

    @Test
    public void softDeleteTest(){
        Team team = new Team();
        Player player = Player.builder().name("yash").id(1).isDeleted(false).team(team).build();

        Mockito.when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        boolean result = playerServices.softDelete(1);

        Assertions.assertTrue(result);
        Assertions.assertTrue(player.isDeleted());
        Assertions.assertNull(player.getTeam());
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    public void softDeleteTest_WhenPlayerNotFound(){
        Mockito.when(playerRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertFalse(playerServices.softDelete(1));

        verify(playerRepository , never()).save(any(Player.class));

    }




}
