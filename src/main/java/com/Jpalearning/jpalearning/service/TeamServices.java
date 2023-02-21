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
        System.out.println(addTeamDto.getName());
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
            teamRepository.delete(team.get());
            return true;
        }
    }

    public Optional<TeamDto> getTeam(int id){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isEmpty()){
            return Optional.empty();
        }
        else{
            List<Player> players = team.get().getPlayers();
            List<Integer> playerIds = new ArrayList<>();
            for(Player player : players){
                playerIds.add(player.getId());
            }
            TeamDto teamDto = new TeamDto(team.get().getId(),team.get().getName(),playerIds);
            return Optional.of(teamDto);
        }
    }

    public String updateTeam(int id, AddTeamDto addTeamDto){
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()){
            Team newTeam = Team.builder().name(addTeamDto.getName()).build();
            teamRepository.save(newTeam);
            return "new Team made";
        }
        else {
            team.get().setName(addTeamDto.getName());
            teamRepository.save(team.get());
            return "team updated";
        }
    }
}
