package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
