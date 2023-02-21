package com.example.marketour.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "audio")
@Getter
@Setter
@NoArgsConstructor
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audio_id")
    private Long audioId;

    @Column(name = "description")
    private String description;

    @Lob
    private byte[] data;
}
