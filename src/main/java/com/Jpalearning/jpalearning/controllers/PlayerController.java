package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.service.PlayerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    PlayerServices playerServices;

    @PostMapping("/add")
    public String addPlayer(@RequestBody AddPlayerDto addPlayerDto){
        if(playerServices.addPlayer(addPlayerDto)){
            return "Success";
        }
        else
            return "Error";
    }

//    @DeleteMapping("/{id}")
//    public String deletePlayer(@PathVariable int id){
//        if(playerServices.deletePlayer(id)){
//            return "Success";
//        }
//        else
//            return "Error";
//    }
    @GetMapping("/{id}")
    public Optional<PlayerDto> getPlayer(@PathVariable int id){
        return playerServices.getPlayer(id);
    }
    @PutMapping("/{id}")
    public String updatePlayer(@PathVariable int id,@RequestBody AddPlayerDto addPlayerDto){
        return playerServices.updatePlayer(id,addPlayerDto);
    }



}
