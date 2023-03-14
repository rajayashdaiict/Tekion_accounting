package com.Jpalearning.jpalearning.service;

import com.Jpalearning.jpalearning.Entity.Match;
import com.Jpalearning.jpalearning.Entity.Player;
import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.MatchCreateDto;
import com.Jpalearning.jpalearning.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServices {

    @Autowired
    MatchRepository matchRepository;
    @Autowired
    MongoServices mongoServices;
    @Autowired
    TeamServices teamServices;
    @Autowired
    PlayerServices playerServices;

    Logger logger = LoggerFactory.getLogger(MatchServices.class);

    public int createMatch(MatchCreateDto matchCreateDto) {

        logger.info("creating a match");
        logger.trace("validation started for creating a team");
        //getting teams from team ids
        Optional<Team> team1 = teamServices.findById(matchCreateDto.getTeam1Id());
        Optional<Team> team2 = teamServices.findById(matchCreateDto.getTeam2Id());

        if (team1.isEmpty() || team2.isEmpty()) {
            logger.error("invalid teams");
            return 0;
        }
        if(team1.get().isDeleted()||team2.get().isDeleted()){
            logger.error("invalid teams");
            return 0;
        }
        team1.get().setPlayers(new ArrayList<>());
        team2.get().setPlayers(new ArrayList<>());

        logger.trace("adding players into teams");
        if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())||!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
            logger.error("cant add players to the team");
            return 0;
        }
        logger.debug("validation done for creating a team");
        Match match = Match.builder().team1(team1.get()).team2(team2.get()).overs(matchCreateDto.getOvers()).build();
        matchRepository.save(match);
        logger.info("match successfully created and saved");
        return match.getId();
    }
    public String updateMatch(MatchCreateDto matchCreateDto , int matchId){
        logger.debug("updating a match");
        logger.debug("validation started for updating a match");
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isEmpty()){
            logger.debug("no match exist so creating a new match");
            return "new match created with id" + createMatch(matchCreateDto);
        }
        if(match.get().getWinner()!=null){
            logger.info("match is already played");
            return "match is already played";
        }
        match.get().setOvers(matchCreateDto.getOvers());
        Optional<Team> team1 = teamServices.findById(matchCreateDto.getTeam1Id());
        Optional<Team> team2 = teamServices.findById(matchCreateDto.getTeam2Id());

        logger.info("updating team1");
        if(team1.isPresent()){
            if(team1.get().isDeleted()){
                logger.debug("cant update the match : team deleted");
                return "cant update the match : team deleted";
            }
            team1.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam1(),team1.get())){
                logger.debug("cant update the match");
                return "cant update the match";
            }
            match.get().setTeam1(team1.get());
        }
        logger.info("updating team2");
        if(team2.isPresent()){
            if(team2.get().isDeleted()){
                logger.debug("cant update the match : team deleted");
                return "cant update the match : team deleted";
            }
            team2.get().setPlayers(new ArrayList<>());
            if(!addPlayersIntoTeam(matchCreateDto.getPlayerIdsTeam2(),team2.get())){
                logger.debug("cant update the match");
                return "cant update the match";
            }
            match.get().setTeam2(team2.get());
        }
        logger.debug("validation done");
        matchRepository.save(match.get());
        logger.info("match updated");
        return "match updated";
    }
    public Object getMatch(int matchId){
        logger.debug("fetching a match");
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isEmpty()){
            logger.error("no match found");
            return "no match found";
        }
        if(match.get().getWinner()!=null){
            logger.info("getting match of {}",matchId);
            return mongoServices.getMatchES(matchId);
//            return mongoServices.getMatch(matchId);
        }
        else {
            logger.info("match has not played yet between {} and {}",match.get().getTeam1().getName(),match.get().getTeam2().getName());
            return "match will be playing between "+match.get().getTeam1().getName()+" and "+match.get().getTeam2().getName();
        }
    }
    //its given that team will not be null or deleted
    public boolean addPlayersIntoTeam(List<Integer> playerIds, Team team) {
        logger.debug("adding players into team {}",team.getName());
        if(playerIds==null||playerIds.isEmpty()) {
            logger.error("invalid players");
            return false;
        }
        for (Integer playerId : playerIds) {

            Optional<Player> player = playerServices.findById(playerId);
            if (player.isEmpty()) {
                logger.error("invalid player");
                return false;
            }
            if(player.get().isDeleted()) {
                logger.error("invalid player");
                return false;
            }

            player.get().setTeam(team);
            playerServices.save(player.get());

            team.getAllPlayers().add(player.get());
            team.getPlayers().add(player.get());

        }
        logger.info("players added into team");
        teamServices.save(team);
        return true;
    }

    public String deleteMatch(int id) {
        logger.debug("deleting match of id {}",id);
        if(matchRepository.findById(id).isEmpty()){
            logger.error("there is no such match");
            return "there is no such match";
        }
        if(matchRepository.findById(id).get().getWinner()!=null){
            logger.error("I can still remove it if you want though ");
            return "I can still remove it if you want though ";
        }
        logger.info("match deleted successfully");
        matchRepository.deleteById(id);
        return "match is removed";
    }

    public Optional<Match> findById(int id){
        return matchRepository.findById(id);
    }
    public Match save(Match match){
        return matchRepository.save(match);
    }
}
