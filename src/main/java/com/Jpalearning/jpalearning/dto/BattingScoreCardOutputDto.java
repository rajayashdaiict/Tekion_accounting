package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BattingScoreCardOutputDto {
    private String batsmanName;
    private String teamName;
    private int runs;
    private int balls;
    private boolean isOut;
    private int inningNum;
}
