package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import com.Jpalearning.jpalearning.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CricketServices {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    GameplayService gameplayService;


    public boolean addPlayer(AddPlayerDto addPlayerDto) {
        Player player = Player.builder().name(addPlayerDto.getName()).build();
        playerRepository.save(player);
        return true;
    }

    public boolean addPlayerIntoTeam(AddPlayerIntoTeamDto addPlayerIntoTeamDto) {

        Optional<Player> player = playerRepository.findById(addPlayerIntoTeamDto.getPlayerId());
        if (player.isEmpty()) {
            return false;
        }

        Optional<Team> team = teamRepository.findById(addPlayerIntoTeamDto.getTeamId());
        if (team.isEmpty()) {
            return false;
        }

        team.get().setPlayers(List.of(player.get()));
        System.out.println(team.get().getPlayers());
//        teamRepository.save(team.get());
        player.get().setTeam(team.get());
        playerRepository.save(player.get());
        return true;
    }

    public boolean addTeam(AddTeamDto addTeamDto) {
        Team team = Team.builder().name(addTeamDto.getName()).build();
        teamRepository.save(team);
        return true;
    }

    public String matchPlay(MatchPlayDto matchPlayDto) {
        //getting teams from their team ids
        Optional<Team> team1 = teamRepository.findById(matchPlayDto.getTeam1Id());
        Optional<Team> team2 = teamRepository.findById(matchPlayDto.getTeam2Id());
        if(team1.isEmpty()||team2.isEmpty()){
            return "Teams does not exist";
        }
        Match match = Match.builder().overs(matchPlayDto.getOvers()).team1(team1.get()).team2(team2.get()).build();
        matchRepository.save(match);

        System.out.println("----checkpoint--- gameplayservice start");

        GameplayDto gameplayDto = new GameplayDto(team1.get(),team2.get(), match.getOvers(),match);
        Team winnerTeam = gameplayService.gameplay(gameplayDto);

        System.out.println("----checkpoint--- gameplayservice done");

        matchRepository.save(match);

        return winnerTeam.getName();
    }
}
