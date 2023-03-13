package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.dto.AddPlayerDto;
import com.Jpalearning.jpalearning.dto.PlayerDto;
import com.Jpalearning.jpalearning.service.PlayerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cricket/player")
public class PlayerController {
    @Autowired
    PlayerServices playerServices;

    @PostMapping("/")
    public ResponseEntity<Player> addPlayer(@RequestBody AddPlayerDto addPlayerDto){
        Player player;
        try {
            player = playerServices.addPlayer(addPlayerDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable int id){
        Player player;
        try {
            player = playerServices.softDelete(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable int id){
        Optional<PlayerDto>  player;
        try {
            player = playerServices.getPlayer(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player.get(), HttpStatus.OK);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable int id,@RequestBody AddPlayerDto addPlayerDto){
        Player player;
        try {
            player = playerServices.updatePlayer(id, addPlayerDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }



}
