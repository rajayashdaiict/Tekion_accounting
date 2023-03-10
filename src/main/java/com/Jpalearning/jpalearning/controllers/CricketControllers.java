package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.dto.*;
import com.Jpalearning.jpalearning.service.CricketServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @PostMapping("/play/{id}")
    public String matchPlay(@PathVariable int id){
        MatchResultDto matchResultDto = cricketServices.matchPlay(id);
        if(matchResultDto.getErrorMsg()!=null){
            return matchResultDto.getErrorMsg();
        }
        else {
            return matchResultDto.getWinnerTeam().getName();
        }
    }
    @GetMapping("/scorecard/{id}")
    public Optional<ScoreCardOutputDto> getScorecard(@PathVariable int id){
        return cricketServices.scorecard(id);
    }


}
