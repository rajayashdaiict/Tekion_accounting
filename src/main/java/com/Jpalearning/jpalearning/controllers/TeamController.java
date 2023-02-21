package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.AddTeamDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.dto.TeamDto;
import com.Jpalearning.jpalearning.service.TeamServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    TeamServices teamServices;
    @PostMapping("/add")
    public String addTeam(@RequestBody AddTeamDto addTeamDto){
        if(teamServices.addTeam(addTeamDto)) return "Success";
        else
            return "Error";
    }

    @DeleteMapping("/{id}")
    public String deleteTeam(@PathVariable int id){
        if(teamServices.deleteTeam(id)) return "Success";
        else
            return "Error";
    }

    @GetMapping("/{id}")
    public Optional<TeamDto> getTeam(@PathVariable int id){
        return teamServices.getTeam(id);
    }
    @PutMapping("/{id}")
    public String updateTeam(@PathVariable int id,@RequestBody AddTeamDto addTeamDto){
        return teamServices.updateTeam(id,addTeamDto);
    }


}
