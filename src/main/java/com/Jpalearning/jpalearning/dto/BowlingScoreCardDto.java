package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.Entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BowlingScoreCardDto {

    public BowlingScoreCardDto(Player player){
        this.player=player;
        oversBowled=0;
        runsGiven=0;
        wicketsTaken=0;
    }
    private Player player;
    private int oversBowled;
    private int runsGiven;
    private int wicketsTaken;
}
