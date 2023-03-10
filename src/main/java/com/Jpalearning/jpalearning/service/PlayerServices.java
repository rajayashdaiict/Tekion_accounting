package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServices {
    @Autowired
    PlayerRepository playerRepository;
    Logger logger = LoggerFactory.getLogger(PlayerServices.class);
    public boolean addPlayer(AddPlayerDto addPlayerDto) {

        logger.debug("adding player {}",addPlayerDto.getName());
        if(addPlayerDto.getName()==null||addPlayerDto.getName().isEmpty())
            return false;

        Player player = Player.builder().name(addPlayerDto.getName()).build();
        logger.info("saved player successfully");
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

        logger.debug("fetching player {}",id);
        Optional<Player> player = playerRepository.findById(id);

        if(player.isEmpty()) {
            logger.error("invalid player");
            return Optional.empty();
        }
        PlayerDto playerDto = new PlayerDto();

        if(player.get().isDeleted()){
            logger.error("invalid player");
            playerDto.setDeleted(true);
            return Optional.of(playerDto);
        }

        playerDto.setName(player.get().getName());
        playerDto.setId(player.get().getId());
        if(player.get().getTeam()!=null){
            playerDto.setTeamName(player.get().getTeam().getName());
        }
        logger.info("player fetching successful");
        return Optional.of(playerDto);
    }

    public String  updatePlayer(int id,AddPlayerDto addPlayerDto){
        logger.debug("updating player {}",id);
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty()){
            if(addPlayer(addPlayerDto)) {
                logger.info("no player exist so creating a new player");
                return "new player created";
            }
            else {
                logger.error("cant update player");
                return "cant update player";
            }
        }
        else {
            if(addPlayerDto.getName()==null||addPlayerDto.getName().isEmpty()) {
                logger.error("cant update player");
                return "cant update player";
            }
            player.get().setName(addPlayerDto.getName());
            playerRepository.save(player.get());
            logger.info("player updated");
            return "player updated";
        }
    }
    public boolean softDelete(int id){
        logger.debug("deleting player {}",id);
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty()){
            logger.error("invalid player");
            return false;
        }
        player.get().setDeleted(true);
        player.get().setTeam(null);
        playerRepository.save(player.get());
        logger.info("player deleted");
        return true;
    }
}
