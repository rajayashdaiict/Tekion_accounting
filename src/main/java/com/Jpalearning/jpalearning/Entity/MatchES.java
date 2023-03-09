package com.Jpalearning.jpalearning.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "person")

@Data
@AllArgsConstructor
@Builder
public class MatchES {

    public MatchES(){
        inning1Details = new ArrayList<>();
        inning2Details = new ArrayList<>();
    }
    @Id
    private int matchId;
    private int team1Id;
    private int team2Id;
    private int winnerTeamId;
    private List<BallObject> inning1Details;
    private List<BallObject> inning2Details;
}
