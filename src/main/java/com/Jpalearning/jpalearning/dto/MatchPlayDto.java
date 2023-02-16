package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchPlayDto {
    private int team1Id;
    private int team2Id;
    private int overs;
}
