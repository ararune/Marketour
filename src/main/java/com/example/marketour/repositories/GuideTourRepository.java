package com.example.marketour.repositories;

import com.example.marketour.model.entities.GuideTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideTourRepository extends JpaRepository<GuideTour, Long> {
}
