package com.example.marketour.model.entities;

import lombok.*;

import javax.persistence.*;
@AllArgsConstructor
@Entity
@Table(name = "tour_review")
@Getter
@Setter
@Builder(toBuilder = true)
public class TourReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tour_review_id")
    private Long tourReviewId;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //@Column(name = "text", nullable = true)
    //private String text;

    @Column(name = "rate")
    private Long rate;

    public TourReview() {

    }

    //millis since epoch
    //@Column(name = "time", nullable = false)
    //private Long time;
}
