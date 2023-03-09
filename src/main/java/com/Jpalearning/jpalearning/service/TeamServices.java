package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.TeamDto;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServices {

    @Autowired
    TeamRepository teamRepository;
    public boolean addTeam(AddTeamDto addTeamDto) {

        if(addTeamDto.getName()==null||addTeamDto.getName().isEmpty())
            return false;

        Team team = Team.builder().name(addTeamDto.getName()).build();
        teamRepository.save(team);
        return true;
    }

    public boolean deleteTeam(int id){
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()){
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
            return true;
        }
    }

    public Optional<TeamDto> getTeam(int id){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isEmpty()){
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
            return Optional.of(
                    TeamDto.builder().name(team.get().getName()).id(team.get().getId()).currentPlayerIds(playerIds).allPlayerIds(allPlayerIds).build());

        }
    }

    public String updateTeam(int id, AddTeamDto addTeamDto){
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()){
            if(addTeam(addTeamDto))
                return "new Team made";
            else
                return "cant make the new team";
        }
        else {
            if(addTeamDto.getName()==null||addTeamDto.getName().isEmpty())
                return "cant update the new team";
            team.get().setName(addTeamDto.getName());
            teamRepository.save(team.get());
            return "team updated";
        }
    }
}
