package com.Jpalearning.jpalearning.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "matchInformation")
@Data
@AllArgsConstructor
@Builder
public class MatchMongo {
    public MatchMongo(){
        inning1Details = new ArrayList<>();
        inning2Details = new ArrayList<>();
    }
    private int matchId;
    private int team1Id;
    private int team2Id;
    private int winnerTeamId;
    private List<BallObject> inning1Details;
    private List<BallObject> inning2Details;
}
