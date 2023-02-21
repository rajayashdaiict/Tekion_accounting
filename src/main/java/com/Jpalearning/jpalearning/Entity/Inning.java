package com.Jpalearning.jpalearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Inning {

    @EmbeddedId
    private InningId inningId;
    @OneToMany(mappedBy = "inning",cascade = CascadeType.ALL)
    private List<BattingScoreCard> battingScoreCards;
    @OneToMany(mappedBy = "inning",cascade = CascadeType.ALL)
    private List<BowlingScoreCard> bowlingScoreCards;
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


