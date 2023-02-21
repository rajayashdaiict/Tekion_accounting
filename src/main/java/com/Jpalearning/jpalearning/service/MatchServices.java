package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddPlayerIntoTeamDto;
import com.Jpalearning.jpalearning.dto.MatchCreateDto;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import com.Jpalearning.jpalearning.repository.PlayerRepository;
import com.Jpalearning.jpalearning.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServices {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    MatchRepository matchRepository;

    public int createMatch(MatchCreateDto matchCreateDto) {
        System.out.println("checkpoint 1------------------------------------------------");
        //getting teams from team ids
        Optional<Team> team1 = teamRepository.findById(matchCreateDto.getTeam1Id());
        Optional<Team> team2 = teamRepository.findById(matchCreateDto.getTeam2Id());
        if (team1.isEmpty() || team2.isEmpty()) {
            return 0;
        }
        System.out.println("checkpoint 2----------------------------------------   -------");
        team1.get().setPlayers(new ArrayList<>());
        team2.get().setPlayers(new ArrayList<>());

        System.out.println("checkpoint 3----------------------------------------   -------");
        //transactional
        if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())||!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
            return 0;
        }
        System.out.println("checkpoint 4----------------------------------------   -------");
        Match match = Match.builder().team1(team1.get()).team2(team2.get()).overs(matchCreateDto.getOvers()).build();
        matchRepository.save(match);
        return match.getId();
    }
    public String updateMatch(MatchCreateDto matchCreateDto , int matchId){
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isEmpty()){
            return "new match created with id" + createMatch(matchCreateDto);
        }
        if(match.get().getWinner()!=null){
            return "match is already played";
        }
        match.get().setOvers(matchCreateDto.getOvers());
        Optional<Team> team1 = teamRepository.findById(matchCreateDto.getTeam1Id());
        Optional<Team> team2 = teamRepository.findById(matchCreateDto.getTeam2Id());

        //transactional here
        if(team1.isPresent()){
            team1.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())){
                return "cant update the match";
            }
            match.get().setTeam1(team1.get());
        }
        if(team2.isPresent()){
            team2.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
                return "cant update the match";
            }
            match.get().setTeam2(team2.get());
        }
        return "match updated";
    }
    public String getMatch(int matchId){
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isEmpty()){
            return "no match found";
        }
        if(match.get().getWinner()!=null){
            return "match has already been played and winner is"+ match.get().getWinner().getName();
        }
        else {
            return "match will be playing between "+match.get().getTeam1().getName()+" and "+match.get().getTeam2().getName();
        }
    }
    public boolean addPlayersIntoTeam(List<Integer> playerIds, Team team) {
        if(playerIds==null)
            return false;
        for (Integer playerId : playerIds) {

            System.out.println("checkpoint 1.1----------------------------------------   -------");
            Optional<Player> player = playerRepository.findById(playerId);
            if (player.isEmpty()) return false;
            if(player.get().isDeleted()) return false;

            System.out.println("checkpoint 1.2----------------------------------------   -------");
            player.get().setTeam(team);
            playerRepository.save(player.get());

            System.out.println("checkpoint 1.3----------------------------------------   -------");
            team.getAllPlayers().add(player.get());
            System.out.println("checkpoint 1.5----------------------------------------   -------");
            team.getPlayers().add(player.get());

            teamRepository.save(team);
            System.out.println("checkpoint 1.4----------------------------------------   -------");
        }
        return true;
    }

    public String deleteMatch(int id) {
        if(matchRepository.findById(id).isEmpty()){
            return "there is no such match";
        }
        if(matchRepository.findById(id).get().getWinner()!=null){
            return "I can still remove it if you want though ";
        }
        matchRepository.deleteById(id);
        return "match is removed";
    }
}
