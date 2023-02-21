package com.example.marketour.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tour_page")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TourPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tour_page_id")
    private Long tourPageId;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "page", nullable = false)
    private Integer page;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body")
    private String body;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "location_id")
    private Location location;


}
