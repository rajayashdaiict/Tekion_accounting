package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.MatchMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchMongoRepo extends MongoRepository<MatchMongo,String> {
    @Query(value = "{'matchId': ?0}")
    Optional<MatchMongo> findByMatchId(int matchId);
}
