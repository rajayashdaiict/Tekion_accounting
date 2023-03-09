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
import java.util.Objects;
import java.util.Optional;

@Service
public class MatchServices {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    MongoServices mongoServices;

    public int createMatch(MatchCreateDto matchCreateDto) {

        System.out.println("hello");
        //getting teams from team ids
        Optional<Team> team1 = teamRepository.findById(matchCreateDto.getTeam1Id());
        Optional<Team> team2 = teamRepository.findById(matchCreateDto.getTeam2Id());

        if (team1.isEmpty() || team2.isEmpty()) {
            return 0;
        }
        if(team1.get().isDeleted()||team2.get().isDeleted()){
            return 0;
        }
        team1.get().setPlayers(new ArrayList<>());
        team2.get().setPlayers(new ArrayList<>());

        //transactional
        //previous match team details
        System.out.println(matchCreateDto.getPlayerIdsTeam1());
        if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())||!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
            System.out.println("fuck you");
            return 0;
        }
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
            if(team1.get().isDeleted()){
                return "cant update the match : team deleted";
            }
            team1.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())){
                return "cant update the match";
            }
            match.get().setTeam1(team1.get());
        }
        if(team2.isPresent()){
            if(team2.get().isDeleted()){
                return "cant update the match : team deleted";
            }
            team2.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
                return "cant update the match";
            }
            match.get().setTeam2(team2.get());
        }
        matchRepository.save(match.get());
        return "match updated";
    }
    public Object getMatch(int matchId){
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isEmpty()){
            return "no match found";
        }
        if(match.get().getWinner()!=null){
            return mongoServices.getMatchES(matchId);
//            return mongoServices.getMatch(matchId);
        }
        else {
            return "match will be playing between "+match.get().getTeam1().getName()+" and "+match.get().getTeam2().getName();
        }
    }
    //its given that team will not be null or deleted
    public boolean addPlayersIntoTeam(List<Integer> playerIds, Team team) {
        System.out.println("fuck you again");
        if(playerIds==null||playerIds.isEmpty())
            return false;
        for (Integer playerId : playerIds) {

            Optional<Player> player = playerRepository.findById(playerId);
            if (player.isEmpty()) return false;
            if(player.get().isDeleted()) return false;

            player.get().setTeam(team);
            playerRepository.save(player.get());

            team.getAllPlayers().add(player.get());
            team.getPlayers().add(player.get());

        }
        teamRepository.save(team);
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
