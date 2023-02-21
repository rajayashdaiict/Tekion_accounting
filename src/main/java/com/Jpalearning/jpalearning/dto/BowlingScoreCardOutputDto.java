package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BowlingScoreCardOutputDto {

    private String bowlerName;
    private String teamName;
    private int oversBowled;
    private int runsGiven;
    private int wicketsTaken;
    private int inningNum;
}
