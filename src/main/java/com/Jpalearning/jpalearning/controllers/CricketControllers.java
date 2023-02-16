package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.AddPlayerIntoTeamDto;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.MatchPlayDto;
import com.Jpalearning.jpalearning.service.CricketServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class CricketControllers {

    @Autowired
    CricketServices cricketServices;
    @PostMapping("/addplayer")
    public String addPlayer(@RequestBody AddPlayerDto addPlayerDto){
        if(cricketServices.addPlayer(addPlayerDto)){
            return "Success";
        }
        else
            return "Error";
    }

    @PostMapping("/addplayerintoteam")
    public String addPlayerIntoTeam(@RequestBody AddPlayerIntoTeamDto addPlayerIntoTeamDto){
        if(cricketServices.addPlayerIntoTeam(addPlayerIntoTeamDto)){
            return "Success";
        }
        else
            return "Error";
    }

    @PostMapping("/addteam")
    public String addTeam(@RequestBody AddTeamDto addTeamDto){
        if(cricketServices.addTeam(addTeamDto)) return "Success";
        else
            return "Error";
    }


    @PostMapping("/play")
    public String MatchPlay(@RequestBody MatchPlayDto matchPlayDto){
        return cricketServices.matchPlay(matchPlayDto);
//        return winnerTeam. map(team -> team.getName() + "has won the match").orElse("Error occured");
    }

    //enum class of names of player
    //script to hit apis multiple time
    //api to make multiple players into team
    //
}
