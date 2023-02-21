package com.example.marketour.controllers;

import com.example.marketour.model.entities.User;
import com.example.marketour.model.entities.UserType;
import com.example.marketour.services.TourService;
import com.example.marketour.services.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionsService transactionsService;
    private final TourService tourService;

    public TransactionController(TransactionsService transactionsService, TourService tourService) {
        this.transactionsService = transactionsService;
        this.tourService = tourService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllTransactionsOfUser(HttpServletRequest request) {
        final var user = request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        final var transactions = transactionsService.getAllOfUser((User) user);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/buy/{tourId}")
    public ResponseEntity<Object> buyTour(HttpServletRequest request, @PathVariable Long tourId) {
        final var session = request.getSession();
        if (session != null) {
            final var user = (User) session.getAttribute("user");
            if (user != null) {
                if (user.getUserType() == UserType.guide) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a tourist, can't buy tours!");
                }
                final var tour = tourService.findById(tourId);
                final var tokens = user.getTokens();
                if (tokens < tour.getPrice()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough tokens!");
                }
                tourService.addTouristTour(user, tour);
                transactionsService.newTransaction(user, tour);
                return ResponseEntity.ok("Successfully bought " + tour.getName() + " tour for " + tour.getPrice() + " tokens!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
        }

    }
}
