package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.service.CricketServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CricketControllers {

    @Autowired
    CricketServices cricketServices;

    @PostMapping("/addplayerintoteam")
    public String addPlayerIntoTeam(@RequestBody AddPlayerIntoTeamDto addPlayerIntoTeamDto){
        if(cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto)){
            return "Success";
        }
        else
            return "Error";
    }

    @PostMapping("/play")
    public String matchPlay(@RequestBody MatchPlayDto matchPlayDto){
        MatchResultDto matchResultDto = cricketServices.matchPlay(matchPlayDto);
        if(matchResultDto.getErrorMsg()!=null){
            return matchResultDto.getErrorMsg();
        }
        else {
            return matchResultDto.getWinnerTeam().getName();
        }
    }

    public String tournamentPlay(@RequestBody TournamentDto tournamentDto){
        return cricketServices.tournamentPlay(tournamentDto);
    }

}
