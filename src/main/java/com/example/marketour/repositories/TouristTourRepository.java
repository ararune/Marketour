package com.example.marketour.repositories;

import com.example.marketour.model.entities.TouristTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristTourRepository extends JpaRepository<TouristTour, Long> {
}
