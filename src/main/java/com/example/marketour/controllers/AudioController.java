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
@RequestMapping("/audio")
public class AudioController {
    private final TourPagesService tourPagesService;

    public AudioController(TourPagesService tourPagesService) {
        this.tourPagesService = tourPagesService;
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<Object> getTourAudio(@PathVariable Long tourId, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
        return ResponseEntity.ok(tourPagesService.getAllTourPages(tourId).stream().map(TourPage::getAudio).collect(Collectors.toList()));
    }
}
