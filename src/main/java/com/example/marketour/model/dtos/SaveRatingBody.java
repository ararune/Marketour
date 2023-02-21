package com.example.marketour.model.dtos;

import lombok.Data;

@Data
public class SaveRatingBody {
    final String tourReviewId;
    final String tour;
    final String user;
    final String rate;

}
