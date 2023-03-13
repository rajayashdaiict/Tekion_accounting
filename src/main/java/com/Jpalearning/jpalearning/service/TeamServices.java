package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.TeamDto;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServices {

    @Autowired
    TeamRepository teamRepository;
    Logger logger = LoggerFactory.getLogger(TeamServices.class);
    public boolean addTeam(AddTeamDto addTeamDto) {
        logger.debug("adding team {}",addTeamDto.getName());
        if(addTeamDto.getName()==null||addTeamDto.getName().isEmpty())
            return false;

        Team team = Team.builder().name(addTeamDto.getName()).build();
        teamRepository.save(team);
        logger.info("added team {}",addTeamDto.getName());
        return true;
    }

    public boolean deleteTeam(int id){
        logger.debug("deleting team {}",id);
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()){
            logger.error("invalid team");
            return false;
        }
        else {
            List<Player> players = team.get().getPlayers();
            for(Player player:players){
                player.setTeam(null);
            }
            //team has no player left check
            team.get().setDeleted(true);
            teamRepository.save(team.get());
            logger.info("team deleted successfully");
            return true;
        }
    }

    public Optional<TeamDto> getTeam(int id){
        logger.debug("fetching team {}",id);
        Optional<Team> team = teamRepository.findById(id);
        if(team.isEmpty()){
            logger.error("invalid team");
            return Optional.empty();
        } else if (team.get().isDeleted()) {
            return Optional.of(TeamDto.builder().isDeleted(true).build());
        } else{
            List<Player> players = team.get().getPlayers();
            List<Player> allPlayers = team.get().getAllPlayers();

            List<Integer> playerIds = new ArrayList<>();
            List<Integer> allPlayerIds = new ArrayList<>();
            for(Player player : players){
                playerIds.add(player.getId());
            }
            for(Player player : allPlayers){
                allPlayerIds.add(player.getId());
            }
            logger.info("returning team");
            return Optional.of(
                    TeamDto.builder().name(team.get().getName()).id(team.get().getId()).currentPlayerIds(playerIds).allPlayerIds(allPlayerIds).build());
        }
    }


    public String updateTeam(int id, AddTeamDto addTeamDto){
        logger.debug("updating team {}",id);
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()){
            if(addTeam(addTeamDto))
                return "new Team made";
            else
                return "cant make the new team";

        }
        else {
            if(addTeamDto.getName()==null||addTeamDto.getName().isEmpty()){
                logger.error("cant update the new team");
                return "cant update the new team";
            }
            team.get().setName(addTeamDto.getName());
            logger.info("team updated");
            teamRepository.save(team.get());
            return "team updated";
        }
    }
}
