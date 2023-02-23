package com.Jpalearning.jpalearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BallMongo {
    private int outcome;
    private int batsmanId;
    private int bowlerId;
    private int ballNumber;

    public BallMongo(int outcome, int batsmanId, int bowlerId) {
        this.outcome = outcome;
        this.batsmanId = batsmanId;
        this.bowlerId = bowlerId;
    }
}
