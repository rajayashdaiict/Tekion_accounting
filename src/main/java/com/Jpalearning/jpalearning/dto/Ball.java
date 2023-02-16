package com.Jpalearning.jpalearning.dto;

import com.Jpalearning.jpalearning.utils.BallOutcomeGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Ball {
    public Ball(){
        outcome = BallOutcomeGenerator.generateOutcome();
    }
    private int outcome;
}
