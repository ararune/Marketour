package com.example.marketour.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long transactionId;


    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "user_id")
    private User guide;

    @ManyToOne
    @JoinColumn(name = "tourist_user_id", referencedColumnName = "user_id")
    private User tourist;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "purchase_time")
    private Long purchaseTime;

}
