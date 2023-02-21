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
public class BattingScoreCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "batsman")
    private Player batsman;
    private int runs;
    private int balls;
    private boolean isOut;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "match_id", referencedColumnName = "matchId"),
            @JoinColumn(name = "inning_num", referencedColumnName = "inningNum")
    })
    private Inning inning;
}
