package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchCreateDto {
    private int team1Id;
    private int team2Id;
    private int overs;
    private List<Integer> playerIdsTeam1;
    private List<Integer> playerIdsTeam2;

}
