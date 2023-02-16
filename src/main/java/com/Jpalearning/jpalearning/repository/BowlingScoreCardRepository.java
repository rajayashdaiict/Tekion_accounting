package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.BowlingScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BowlingScoreCardRepository extends JpaRepository<BowlingScoreCard,Integer> {

}
