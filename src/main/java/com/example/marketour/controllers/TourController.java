package com.example.marketour.controllers;

import com.example.marketour.model.dtos.LocationBody;
import com.example.marketour.model.dtos.SaveTourBody;
import com.example.marketour.model.entities.Filter;
import com.example.marketour.model.entities.Tour;
import com.example.marketour.model.entities.User;
import com.example.marketour.model.entities.UserType;
import com.example.marketour.services.TourService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourController {
    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;

    }

    @PostMapping("/nearby")
    public ResponseEntity<Object> getNearby(HttpServletRequest request, @RequestBody String json) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        final var body = new Gson().fromJson(json, LocationBody.class);
        final var nearbyTours = tourService.getNearbyIds(body.getLongitude(), body.getLatitude());
        return ResponseEntity.ok(nearbyTours);
    }


    @PostMapping("/saveTour")
    public ResponseEntity<Object> saveTour(HttpServletRequest request, @RequestBody String json) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        final var body = new Gson().fromJson(json, SaveTourBody.class);


        final var newTour = Tour.builder()
                .tourId(Long.valueOf(body.getTourId()))
                .name(body.getName())
                .description(body.getDescription())
                .city(body.getCity())
                .country(body.getCountry())
                .price(Double.valueOf(body.getPrice()))
                .visibleOnMarket(body.isVisibleOnMarket()).build();

        tourService.updateTour(newTour);
        return ResponseEntity.ok("Successfully updated tour!");
    }

    @GetMapping("/ofThisUser")
    public ResponseEntity<Object> getAllToursOfThisUser(HttpServletRequest request, Model model) {
        final var session = request.getSession();
        if (session != null) {
            final var user = (User) session.getAttribute("user");
            if (user != null) {
                final var filter = (Filter) model.getAttribute("filter");
                final List<Tour> result;
                if (user.getUserType() == UserType.tourist) {
                    result = tourService.getAllTouristsTours(user, filter);
                } else {
                    result = tourService.getAllGuideTours(user, filter);
                }
                if (result != null) {
                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tours not found for that user!");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tours not found for that user!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }

    }

    @GetMapping("/getTour/{tourId}")
    public ResponseEntity<Object> getTour(HttpServletRequest request, @PathVariable String tourId) {
        final var session = request.getSession();
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        final var result = tourService.getTour(Long.valueOf(tourId));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour not found!");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllTours(HttpServletRequest request) {
        final var session = request.getSession();
        if (session.getAttribute("user") != null) {
            final var result = tourService.getAllTours();
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tours not found for that user!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }

    }

    @GetMapping("/market")
    public ResponseEntity<Object> getAllToursOnMarket(HttpServletRequest request, Model model) {
        final var session = request.getSession();
        if (session.getAttribute("user") != null) {
            final var filter = (Filter) model.getAttribute("filter");
            final var result = tourService.getAllToursOnMarketplace(filter);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tours not found for that user!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }

    }

    @GetMapping("/delete/{tourId}")
    public ResponseEntity<Object> removeTour(HttpServletRequest request, @PathVariable String tourId) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        tourService.deleteTour(Long.valueOf(tourId));
        return ResponseEntity.ok("Successfully removed tour!");
    }
}
