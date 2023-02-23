package com.Jpalearning.jpalearning.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collation = "matchInformation")
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
    private List<BallMongo> inning1Details;
    private List<BallMongo> inning2Details;
}
