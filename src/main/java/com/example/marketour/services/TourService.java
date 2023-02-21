package com.example.marketour.services;

import com.example.marketour.model.entities.*;
import com.example.marketour.repositories.GuideTourRepository;
import com.example.marketour.repositories.TourRepository;
import com.example.marketour.repositories.TouristTourRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.marketour.model.entities.Tour.customFilter;
import static java.lang.Math.*;


@Service
public class TourService {
    private final TouristTourRepository touristTourRepository;
    private final GuideTourRepository guideTourRepository;
    private final TourRepository tourRepository;


    public TourService(TouristTourRepository touristTourRepository, GuideTourRepository guideTourRepository,
                       TourRepository tourRepository) {
        this.touristTourRepository = touristTourRepository;
        this.guideTourRepository = guideTourRepository;
        this.tourRepository = tourRepository;
    }

    public static double distance(double lat1, double lon1,
                                  double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        lat1 = toRadians(lat1);
        lat2 = toRadians(lat2);

        double a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) *
                        cos(lat1) * cos(lat2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return R * c;
    }

    public List<Tour> getAllTouristsTours(User user, Filter filter) {
        return touristTourRepository.findAll().stream().filter(tour -> tour.getTourist().sameUser(user) && user.getUserType() == UserType.tourist && customFilter(tour.getTour(), filter)).map(TouristTour::getTour).collect(Collectors.toList());
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Tour> getAllGuideTours(User user, Filter filter) {
        var stream = guideTourRepository.findAll().stream().filter(tour -> tour.getGuide().sameUser(user) && user.getUserType() == UserType.guide && customFilter(tour.getTour(), filter));
        if(filter != null && filter.isReviewSort()) {
            stream = stream.sorted((a,b) -> b.getTour().getAverageRating().compareTo(a.getTour().getAverageRating()));
        }
        return stream.map(GuideTour::getTour).collect(Collectors.toList());
    }

    public Tour getTour(Long tourId) {
        return tourRepository.findAll().stream().filter(tour -> Objects.equals(tour.getTourId(), tourId)).findFirst().orElse(null);
    }

    public void deleteTour(Long tourId) {
        tourRepository.deleteById(tourId);
    }

    public List<Tour> getAllToursOnMarketplace(Filter filter) {
//        return guideTourRepository.findAll().stream().map(GuideTour::getTour).
//                filter(e -> e.isVisibleOnMarket() && filter(e, filter)).collect(Collectors.toList());
        var stream = guideTourRepository.findAll().stream().map(GuideTour::getTour)
                .filter(e -> e!=null && e.isVisibleOnMarket() && customFilter(e, filter));
        if(filter != null && filter.isReviewSort()) {
            stream = stream.sorted((a,b) -> b.getAverageRating().compareTo(a.getAverageRating()));
        }
        return stream.collect(Collectors.toList());
    }

    public Tour findById(Long tourId) {
        return tourRepository.findAll().stream().filter(tour -> tour.getTourId().equals(tourId)).findFirst().orElse(null);
    }

    public void updateTour(Tour tour) {
        final var existing = tourRepository.findAll().stream().filter(tour1 -> Objects.equals(tour1.getTourId(), tour.getTourId())).findFirst().orElse(null);
        if (existing != null) {
            tourRepository.save(
                    existing.toBuilder()
                            .name(tour.getName())
                            .price(tour.getPrice())
                            .city(tour.getCity())
                            .country(tour.getCountry())
                            .description(tour.getDescription())
                            .visibleOnMarket(tour.isVisibleOnMarket()).build());
        }
    }

    public void addTouristTour(User user, Tour tour) {
        final var touristTour = new TouristTour();
        touristTour.setTourist(user);
        touristTour.setTour(tour);
        touristTour.setLastUsed(System.currentTimeMillis());
        touristTourRepository.save(touristTour);
        tour.getTouristTours().add(touristTour);
    }

    public void addGuideTour(User user, Tour tour) {
        final var guideTour = new GuideTour();
        guideTour.setGuide(user);
        guideTour.setCreateTime(System.currentTimeMillis());
        tour.setGuideTour(guideTour);
        tourRepository.save(tour);
    }

    public List<Long> getNearbyIds(Double longitude, Double latitude) {
        return tourRepository.findAll().stream().filter(tour -> {
            final var location = Objects.requireNonNull(tour.getTourPages().stream().findFirst().orElse(null)).getLocation();
            final var tourLat = location.getLatitude();
            final var tourLong = location.getLongitude();
            return distance(tourLat, tourLong, latitude, longitude) <= 3; // 3(KM) RADIUS - A MAGIC NUMBER
        }).map(Tour::getTourId).collect(Collectors.toList());
    }
}
