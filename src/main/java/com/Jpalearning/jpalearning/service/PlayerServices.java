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

        if(addPlayerDto.getName()==null||addPlayerDto.getName().isEmpty())
            return false;

        Player player = Player.builder().name(addPlayerDto.getName()).build();
        playerRepository.save(player);
        return true;
    }

    //if you want to delete data of player from repository
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

        if(player.get().isDeleted()){
            playerDto.setDeleted(true);
            return Optional.of(playerDto);
        }

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
            if(addPlayer(addPlayerDto))
                return "new player created";
            else
                return "cant update player";
        }
        else {
            if(addPlayerDto.getName()==null||addPlayerDto.getName().isEmpty())
                return "cant update player";
            player.get().setName(addPlayerDto.getName());
            playerRepository.save(player.get());
            return "player updated";
        }
    }
    public boolean softDelete(int id){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty()){
            return false;
        }
        player.get().setDeleted(true);
        player.get().setTeam(null);
        playerRepository.save(player.get());
        return true;
    }
}
