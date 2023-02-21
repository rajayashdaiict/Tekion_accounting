package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.MatchPlayDto;
import com.Jpalearning.jpalearning.dto.MatchResultDto;
import com.Jpalearning.jpalearning.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TournamentPlayService {
    @Autowired
    CricketServices cricketServices;
    public Optional<List<Team>> oneStagePlay(List<Team> teams,int overs){
        List<Team> winnerTeams = new ArrayList<>();
        while(!teams.isEmpty()){
            Team team1 = selectTeam(teams);
            Team team2 = selectTeam(teams);
            MatchPlayDto matchPlayDto = new MatchPlayDto(team1.getId(), team2.getId(), overs);
            MatchResultDto matchResultDto = cricketServices.matchPlay(matchPlayDto);
            if(matchResultDto.getErrorMsg()!=null){
                //return null
                return Optional.empty();
            }
            winnerTeams.add(matchResultDto.getWinnerTeam());
        }
        return Optional.of(winnerTeams);

    }
    public int randomIndex(List<Team> teams){
        int size = teams.size();
        int randomNumber = (int) (Math.ceil(Math.random() * size) - 1);
        return randomNumber;
    }
    public Team selectTeam(List<Team> teams){
        Team team=teams.get(randomIndex(teams));
        teams.remove(team);
        return team;
    }
}
