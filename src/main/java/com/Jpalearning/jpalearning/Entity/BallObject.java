package com.Jpalearning.jpalearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BallObject {
    private int outcome;
    private int batsmanId;
    private int bowlerId;
    private int ballNumber;

    public BallObject(int outcome, int batsmanId, int bowlerId) {
        this.outcome = outcome;
        this.batsmanId = batsmanId;
        this.bowlerId = bowlerId;
    }
}
