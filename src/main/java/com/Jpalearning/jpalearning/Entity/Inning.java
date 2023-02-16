package com.Jpalearning.jpalearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Inning {

    @EmbeddedId
    private InningId inningId;
    @OneToOne(mappedBy = "inning",cascade = CascadeType.ALL)
    private BattingScoreCard  battingScoreCard;
    @OneToOne(mappedBy = "inning",cascade = CascadeType.ALL)
    private BowlingScoreCard bowlingScoreCard;
    @OneToOne
    @JoinColumn(name = "battingTeam")
    private Team battingTeam;
    @OneToOne
    @JoinColumn(name = "bowlingTeam")
    private Team bowlingTeam;
    @ManyToOne
    @JoinColumn(name = "`match`")
    private Match match;
    private int totalRuns;

}


