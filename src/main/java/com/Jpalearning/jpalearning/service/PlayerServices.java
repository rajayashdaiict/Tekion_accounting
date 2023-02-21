package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServices {
    @Autowired
    PlayerRepository playerRepository;
    public boolean addPlayer(AddPlayerDto addPlayerDto) {
        Player player = Player.builder().name(addPlayerDto.getName()).build();
        playerRepository.save(player);
        return true;
    }
    public boolean deletePlayer(int id){
        Optional<Player> player = playerRepository.findById(id);
        player.ifPresent(value -> playerRepository.delete(value));
        return true;
    }
    public Optional<PlayerDto> getPlayer(int id){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty())
            return Optional.empty();
        PlayerDto playerDto = new PlayerDto();
        playerDto.setName(player.get().getName());
        playerDto.setId(player.get().getId());
        if(player.get().getTeam()!=null){
            playerDto.setTeamName(player.get().getTeam().getName());
        }
        return Optional.of(playerDto);
    }

    public String  updatePlayer(int id,AddPlayerDto addPlayerDto){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty()){
            Player newPlayer = Player.builder().name(addPlayerDto.getName()).build();
            playerRepository.save(newPlayer);
            return "new player created";
        }
        else {
            player.get().setName(addPlayerDto.getName());
            playerRepository.save(player.get());
            return "player updated";
        }
    }
}
