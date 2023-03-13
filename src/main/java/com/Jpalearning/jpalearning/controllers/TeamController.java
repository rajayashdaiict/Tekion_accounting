package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.dto.TeamDto;
import com.Jpalearning.jpalearning.service.TeamServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cricket/team")
public class TeamController {

    @Autowired
    TeamServices teamServices;
    @PostMapping("/")
    public ResponseEntity<Team> addTeam(@RequestBody AddTeamDto addTeamDto){
        Team team;
        try {
            team = teamServices.addTeam(addTeamDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String deleteTeam(@PathVariable int id){
        if(teamServices.deleteTeam(id)) return "Success";
        else
            return "Error";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable int id){
        Team team;
        try {
            team = teamServices.getTeam(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(team, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public String updateTeam(@PathVariable int id,@RequestBody AddTeamDto addTeamDto){
        return teamServices.updateTeam(id,addTeamDto);
    }


}
