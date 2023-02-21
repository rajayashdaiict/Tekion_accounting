package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.Entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScoreCardDto {

    public ScoreCardDto(Team battingTeam,Team bowlingTeam){

        this.battingTeam=battingTeam;
        this.bowlingTeam=bowlingTeam;

        for(int i=0;i<battingTeam.getPlayers().size();i++){
            battingScoreCardDtolist.add(new BattingScoreCardDto(battingTeam.getPlayers().get(i)));
        }
        for(int i=0;i<battingTeam.getPlayers().size();i++){
            bowlingScoreCardDtolist.add(new BowlingScoreCardDto(bowlingTeam.getPlayers().get(i)));
        }
    }

    private Team battingTeam;
    private Team bowlingTeam;
    private int runs;
    private int wickets;
    private List<BattingScoreCardDto> battingScoreCardDtolist = new ArrayList<>();
    private List<BowlingScoreCardDto> bowlingScoreCardDtolist = new ArrayList<>();
}
