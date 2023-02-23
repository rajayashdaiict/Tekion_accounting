package com.Jpalearning.jpalearning.controllers;

import com.Jpalearning.jpalearning.dto.MatchCreateDto;
import com.Jpalearning.jpalearning.service.MatchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    MatchServices matchServices;
    @PostMapping("/create")
    public int createMatch(@RequestBody MatchCreateDto matchCreateDto){
        return matchServices.createMatch(matchCreateDto);
    }

    @PutMapping("/{id}")
    public String updateMatch(@RequestBody MatchCreateDto matchCreateDto,@PathVariable int id){
        return matchServices.updateMatch(matchCreateDto,id);
    }
    @GetMapping("{id}")
    public Object getMatch(@PathVariable int id){
        return matchServices.getMatch(id);
    }
    @DeleteMapping("{id}")
    public String deleteMatch(@PathVariable int id){
        return matchServices.deleteMatch(id);
    }
//    @GetMapping("/{id}")
//    public Optional<MatchInformationDto> getMatch(@PathVariable int id){
//
//    }


}
