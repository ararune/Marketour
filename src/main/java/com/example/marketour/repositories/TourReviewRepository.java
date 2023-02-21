package com.example.marketour.repositories;

import com.example.marketour.model.entities.Tour;
import com.example.marketour.model.entities.TourReview;
import com.example.marketour.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourReviewRepository extends JpaRepository<TourReview, Long> {

    TourReview findByTourAndUser(Tour tour, User user);

    List<TourReview> findAllByTour(Tour tour);
}
