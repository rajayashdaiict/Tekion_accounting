package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.Entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InningTeamsDto {
    private Team battingTeam;
    private Team bowlingTeam;
}
