package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.Entity.BattingScoreCard;
import com.Jpalearning.jpalearning.Entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattingScoreCardDto {

    public BattingScoreCardDto(Player player){
        this.player=player;
        runs=0;
        balls=0;
        isOut=false;
    }
    private Player player;
    private int runs;
    private int balls;
    private boolean isOut;

}
