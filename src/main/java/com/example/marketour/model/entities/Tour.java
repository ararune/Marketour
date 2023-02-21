package com.example.marketour.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tour")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Tour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tour_id")
    private Long tourId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "visible_on_market", nullable = false)
    private boolean visibleOnMarket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "guide_tour_id")
    @JsonIgnore
    private GuideTour guideTour;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TouristTour> touristTours;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TourPage> tourPages;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TourReview> tourReviews;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

    public Long getAverageRating() {
        return tourReviews.stream().map(TourReview::getRate).reduce(Long::sum).orElse(0L);
    }

    public Double calculateRateForTour() {
        return tourReviews.stream().map(TourReview::getRate).reduce(Long::sum).orElse(0L).doubleValue() / tourReviews.size();
    }

    public static boolean customFilter(Tour tour, Filter filter) {
        return filter == null || ((filter.city == null || tour.city.equals(filter.city.name())) &&
                (filter.country == null || tour.country.equals(filter.country.name())) &&
                (filter.priceRange == null || filter.priceRange.contains(tour.price)));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}

