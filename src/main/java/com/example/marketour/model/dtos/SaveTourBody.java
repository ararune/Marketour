package com.example.marketour.model.dtos;

import lombok.Data;

@Data
public class SaveTourBody {
    final String tourId;
    final String name;
    final String description;
    final String startLocationName;
    final String longitudeStart;
    final String latitudeStart;
    final String endLocationName;
    final String longitudeEnd;
    final String latitudeEnd;
    final String country;
    final String city;
    final String price;
    final boolean visibleOnMarket;


}
