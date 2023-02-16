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
public class BowlingScoreCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "batsman")
    private Player bowler;
    private int overs;
    private int runsGiven;
    private int wicketsTaken;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "match_id", referencedColumnName = "matchId"),
            @JoinColumn(name = "inning_num", referencedColumnName = "inningNum")
    })
    private Inning inning;

}
