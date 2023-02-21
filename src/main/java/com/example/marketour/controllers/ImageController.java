package com.example.marketour.controllers;

import com.example.marketour.model.entities.TourPage;
import com.example.marketour.services.TourPagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final TourPagesService tourPagesService;

    public ImageController(TourPagesService tourPagesService) {
        this.tourPagesService = tourPagesService;
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<Object> getTourImages(@PathVariable Long tourId, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        return ResponseEntity.ok(tourPagesService.getAllTourPages(tourId).stream().map(TourPage::getImage).collect(Collectors.toList()));
    }

    @GetMapping("/{tourId}/first")
    public ResponseEntity<Object> getFirstPageImage(@PathVariable Long tourId, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        return ResponseEntity.ok(tourPagesService.getFirstPage(tourId).getImage());
    }
}
