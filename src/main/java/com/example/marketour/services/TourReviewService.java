package com.example.marketour.services;

import com.example.marketour.model.entities.Tour;
import com.example.marketour.model.entities.TourReview;
import com.example.marketour.model.entities.User;
import com.example.marketour.repositories.TourReviewRepository;
import com.example.marketour.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TourReviewService {
    private final TourReviewRepository tourReviewRepository;
    private final UserRepository userRepository;

    public TourReviewService(TourReviewRepository tourReviewRepository, UserRepository userRepository) {
        this.tourReviewRepository = tourReviewRepository;
        this.userRepository = userRepository;
    }

    public List<TourReview> getAllReviewsOfTour(Tour tour) {
        return tourReviewRepository.findAll().stream().filter(e -> Objects.equals(e.getTour().getTourId(), tour.getTourId())).collect(Collectors.toList());
    }

    public List<Long> getAllRates(){
        return tourReviewRepository.findAll().stream().map(e -> e.getRate()).collect(Collectors.toList());
    }

    public Double calculateRateForTour(Tour tour) {
        final var allReviews = getAllReviewsOfTour(tour);
        return allReviews.stream().map(TourReview::getRate).reduce(Long::sum).get().doubleValue() / allReviews.size();
    }

    public void addNewReview(User user, String text, Long rate, Tour tour) {
        final var time = System.currentTimeMillis();
        final var newTourReview = new TourReview();
        newTourReview.setTour(tour);
        newTourReview.setUser(user);
        newTourReview.setRate(rate);
        tourReviewRepository.save(newTourReview);
    }

    public void updateReview(TourReview tourReview) {
        final var existing = tourReviewRepository.findAll().stream().filter(tourReview1 -> Objects.equals(tourReview1.getTourReviewId(), tourReview.getTourReviewId())).findFirst().orElse(null);
        if (existing != null) {
            tourReviewRepository.save(
                    existing.toBuilder()
                            .rate(tourReview.getRate()).build());
        }
    }

    public void newReview(User user, Tour tour, Long rate) {
        TourReview review = tourReviewRepository.findByTourAndUser(tour, user);
        //If review exists, update
        if (review != null) {
            review.setRate(rate);
            tourReviewRepository.save(review);
        }
        //New review
        else {
            review = new TourReview();
            review.setTour(tour);
            review.setRate(rate);
            review.setUser(user);
            tourReviewRepository.save(review);
        }
    }
}
