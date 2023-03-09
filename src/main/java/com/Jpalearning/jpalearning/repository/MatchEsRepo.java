package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.MatchES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEsRepo extends ElasticsearchRepository<MatchES,Integer> {


}
