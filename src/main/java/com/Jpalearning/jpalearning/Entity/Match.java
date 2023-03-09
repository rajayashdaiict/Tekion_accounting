package com.Jpalearning.jpalearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "match_table")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int overs;
    @OneToOne
    @JoinColumn(name = "winnerTeam")
    @Builder.Default
    private Team winner=null;
    @OneToOne
    @JoinColumn(name = "Team1")
    private Team team1;
    @OneToOne
    @JoinColumn(name = "Team2")
    private Team team2;
    @OneToMany(mappedBy = "match",cascade = CascadeType.ALL)
    private List<Inning> inning;

}
