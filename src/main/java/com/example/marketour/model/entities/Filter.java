package com.example.marketour.model.entities;

import lombok.Data;
import org.springframework.data.domain.Range;

@Data
public class Filter {
    final Country country;
    final City city;
    final Range<Double> priceRange;

    final boolean reviewSort;

    public Filter(Country country, City city, Range<Double> priceRange, boolean reviewSort) {
        this.country = country;
        this.city = city;
        this.priceRange = priceRange;
        this.reviewSort = reviewSort;
    }
}
