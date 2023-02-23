package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.MatchMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchMongoRepo extends MongoRepository<MatchMongo,String> {

}
