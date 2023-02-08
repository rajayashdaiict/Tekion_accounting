package com.Cricket.com.cricketProject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CricketController {
    public static Pair<ScoreCard,ScoreCard> matchResults;

    public void setMatchResults(Pair<ScoreCard, ScoreCard> matchResults) {
        this.matchResults = matchResults;
    }
    @RequestMapping("/cricket")
    @ResponseBody
    public ResponseEntity<?> getMatchResults() {
        return ResponseEntity.ok(matchResults);
    }

}
