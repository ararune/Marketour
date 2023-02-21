package com.example.marketour.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "guide_tour")
@Getter
@Setter
public class GuideTour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "guide_tour_id")
    private Long guideTourId;

    @OneToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "user_id", nullable = false)
    private User guide;

    @OneToOne(mappedBy = "guideTour")
    private Tour tour;

    //in milliseconds
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    //in milliseconds
    @Column(name = "expiration_time")
    private Long expirationTime;
}
