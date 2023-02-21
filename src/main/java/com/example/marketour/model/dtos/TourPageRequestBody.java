package com.example.marketour.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TourPageRequestBody {
    private Long pageId;
    private Integer page;

    private String title;

    private String body;

    private Double longitude;

    private Double latitude;

    private String locationName;

    private String country;
    private String city;
    private String imageDescription;
    private String audioDescription;
}
