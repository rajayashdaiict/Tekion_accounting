package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Team;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameplayDto {
    private Team team1;
    private Team team2;
    private int overs;
    private Match match;

}
