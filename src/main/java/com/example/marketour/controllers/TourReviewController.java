package com.example.marketour.controllers;

import com.example.marketour.model.dtos.SaveRatingBody;
import com.example.marketour.model.entities.Tour;
import com.example.marketour.model.entities.TourReview;
import com.example.marketour.model.entities.User;
import com.example.marketour.repositories.TourRepository;
import com.example.marketour.repositories.TourReviewRepository;
import com.example.marketour.services.TourReviewService;
import com.example.marketour.services.TourService;
import com.example.marketour.services.UserService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class TourReviewController {

    final TourReviewRepository tourReviewRepository;
    final TourRepository tourRepository;
    final TourService tourService;
    final UserService userService;

    final TourReviewService tourReviewService;

    public TourReviewController(TourReviewRepository tourReviewRepository, TourRepository tourRepository, TourService tourService, UserService userService, TourReviewService tourReviewService) {
        this.tourReviewRepository = tourReviewRepository;
        this.tourRepository = tourRepository;
        this.tourService = tourService;
        this.userService = userService;
        this.tourReviewService = tourReviewService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllReviewsForTour(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        final var tourId = (Long) model.getAttribute("tourId");
        return ResponseEntity.ok(tourReviewRepository.findAll().stream().filter(e -> e.getTour().getTourId().equals(tourId)).collect(Collectors.toList()));
    }

    @PostMapping(value = "/saveRating")
    public ResponseEntity<Object> saveRating(HttpServletRequest request, @RequestBody String json) {
            if (request.getSession().getAttribute("user") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
            }
            final var session = request.getSession();
            final var body = new Gson().fromJson(json, SaveRatingBody.class);
            Tour tour = tourService.getTour(Long.valueOf(body.getTour()));
            final var user = (User) session.getAttribute("user");
            tourReviewService.newReview(user, tour, Long.valueOf(body.getRate()));
            return ResponseEntity.ok("Successfully updated rating!");
        }

}




