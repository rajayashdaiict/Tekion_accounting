package com.Jpalearning.jpalearning.repository;

import com.Jpalearning.jpalearning.Entity.Inning;
import com.Jpalearning.jpalearning.Entity.InningId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InningRepository extends JpaRepository<Inning, InningId> {

}
