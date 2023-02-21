package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreCardOutputDto {

    private int matchId;
    private int team1Id;
    private int team2Id;
    private List<BattingScoreCardOutputDto> batsmanDetails = new ArrayList<>();
    private List<BowlingScoreCardOutputDto> bowlersDetails = new ArrayList<>();

}
