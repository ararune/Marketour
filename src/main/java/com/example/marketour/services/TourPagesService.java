package com.example.marketour.services;

import com.example.marketour.model.entities.*;
import com.example.marketour.repositories.LocationRepository;
import com.example.marketour.repositories.TourPageRepository;
import com.example.marketour.repositories.TourRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TourPagesService {
    private final TourPageRepository tourPageRepository;
    private final TourRepository tourRepository;
    private final LocationRepository locationRepository;

    public TourPagesService(TourPageRepository tourPageRepository,
                            TourRepository tourRepository,
                            LocationRepository locationRepository) {
        this.tourPageRepository = tourPageRepository;
        this.tourRepository = tourRepository;
        this.locationRepository = locationRepository;
    }

    public List<TourPage> getAllTourPages(Long tourId) {
        return tourPageRepository.findAll().stream().filter(tourPage -> Objects.equals(tourPage.getTour().getTourId(), tourId)).collect(Collectors.toList());
    }

    public TourPage getById(Long pageId) {
        return tourPageRepository.findAll().stream().filter(tourPage -> Objects.equals(tourPage.getTourPageId(), pageId)).findFirst().orElse(null);
    }

    public void reorder(List<Long> newIds, Long tourId) {
        final var old = tourPageRepository.findAll().stream()
                .filter(tourPage -> Objects.equals(tourPage.getTour().getTourId(), tourId))
                .collect(Collectors.toList());
        final var newPages = newIds.stream().map(aLong -> {
            final var occurrence = old.stream().filter(tourPage -> Objects.equals(tourPage.getTourPageId(), aLong)).findFirst().orElse(null);
            if (occurrence != null) {
                occurrence.setPage(newIds.indexOf(aLong));
            }
            return occurrence;
        }).collect(Collectors.toList());

        tourPageRepository.saveAll(newPages);
    }


    public TourPage getFirstPage(Long tourId) {
        return tourPageRepository.findAll().stream().filter(tourPage -> Objects.equals(tourPage.getTour().getTourId(), tourId) && tourPage.getPage() == 0).findFirst().orElse(null);
    }

    public TourPage getLastPage(Long tourId) {
        List<TourPage> tourPages = tourPageRepository.findAll().stream().filter(tourPage -> Objects.equals(tourPage.getTour().getTourId(), tourId)).collect(Collectors.toList());
        int lastPage = tourPages.size() - 1;
        return tourPages.stream().filter(tourPage -> tourPage.getPage() == lastPage).findFirst().orElse(null);
    }

    public void updateTourPage(Long pageId, String title, String body, Image image, Audio audio, Location location) {
        final var existing = tourPageRepository.findAll().stream().filter(tourPage -> Objects.equals(tourPage.getTourPageId(), pageId)).findFirst().orElse(null);
        if (existing != null) {
            existing.setTitle(title);
            existing.setBody(body);
            existing.setImage(image);
            existing.setAudio(audio);
            locationRepository.save(location);
            existing.setLocation(location);
            tourPageRepository.save(existing);
        }

    }

    public void insertNewTourPage(Tour tour, Integer page, String title, String body, Image image, Audio audio, Location location) {
        final var newTourPage = new TourPage();
        newTourPage.setPage(page);
        newTourPage.setTour(tour);
        newTourPage.setBody(body);
        newTourPage.setAudio(audio);
        newTourPage.setLocation(location);
        newTourPage.setImage(image);
        newTourPage.setTitle(title);
        tourPageRepository.save(newTourPage);
    }

    public void removeTourPageById(Long tourPageId) {
        final var tourPage = tourPageRepository.findById(tourPageId).orElse(null);
        if (tourPage != null) {
            tourPage.getTour().getTourPages().remove(tourPage);
            tourRepository.save(tourPage.getTour());
            tourPageRepository.deleteById(tourPageId);
        }

    }
}
