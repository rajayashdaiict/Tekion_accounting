package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.utils.ConstanceFile;
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

    public Player addPlayer(AddPlayerDto addPlayerDto) {

        logger.debug("adding player {}", addPlayerDto);

        if (addPlayerDto.getName() == null || addPlayerDto.getName().isEmpty()) {
            throw new RuntimeException(ConstanceFile.INVALID_INPUT);
        }

        Player player = Player.builder().name(addPlayerDto.getName()).build();
        return playerRepository.save(player);
    }

    //if you want to delete data of player from repository
    public boolean deletePlayer(int id) {
        Optional<Player> player = playerRepository.findById(id);
        player.ifPresent(value -> playerRepository.delete(value));
        return true;
    }

    public Optional<PlayerDto> getPlayer(int id) {

        logger.debug("fetching player {}", id);
        Optional<Player> player = playerRepository.findById(id);

        if (player.isEmpty()) {
            throw new RuntimeException(ConstanceFile.INVALID_INPUT);
        }
        PlayerDto playerDto = new PlayerDto();

        if (player.get().isDeleted()) {
            throw new RuntimeException(ConstanceFile.INVALID_INPUT);
        }

        playerDto.setName(player.get().getName());
        playerDto.setId(player.get().getId());
        if (player.get().getTeam() != null) {
            playerDto.setTeamName(player.get().getTeam().getName());
        }
        logger.info("player fetching successful");
        return Optional.of(playerDto);
    }

    public Optional<Player> findById(int id){
        return playerRepository.findById(id);
    }
    public void save(Player player){
        playerRepository.save(player);
    }

    public Player updatePlayer(int id, AddPlayerDto addPlayerDto) {
        logger.debug("updating player {}", id);
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()) {
            Player addPlayer = addPlayer(addPlayerDto);
            if (addPlayer != null) {
                logger.info("no player exist so creating a new player");
                return addPlayer;
            } else {
                throw new RuntimeException(ConstanceFile.INVALID_INPUT);
            }
        } else {
            if (addPlayerDto.getName() == null || addPlayerDto.getName().isEmpty()) {
                throw new RuntimeException(ConstanceFile.INVALID_INPUT);
            }
            player.get().setName(addPlayerDto.getName());
            Player updatedPlayer = playerRepository.save(player.get());
            logger.info("player updated");
            return updatedPlayer;
        }
    }

    public Player softDelete(int id) {
        logger.debug("deleting player {}", id);
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()) {
            throw new RuntimeException(ConstanceFile.INVALID_INPUT);
        }
        player.get().setDeleted(true);
        player.get().setTeam(null);
        Player player1 = playerRepository.save(player.get());
        logger.info("player deleted");
        return player1;
    }
}
