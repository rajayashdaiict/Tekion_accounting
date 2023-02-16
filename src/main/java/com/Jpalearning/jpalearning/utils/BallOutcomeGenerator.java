package com.Jpalearning.jpalearning.utils;

import org.springframework.stereotype.Service;
@Service
public class BallOutcomeGenerator {
    public static int generateOutcome(){
        int lowerLimit=0;
        int upperLimit=7;
        return (int) (Math.ceil(Math.random() * (upperLimit - lowerLimit + 1)) - 1) + lowerLimit;
    }
}
