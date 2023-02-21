package com.example.marketour.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
@Getter
@ToString
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @JsonIgnore
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    @JsonIgnore
    private UserType userType;
    @NotEmpty(message = "Username cannot be empty.")
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @NotEmpty(message = "Password cannot be empty.")
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private City city;
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;
    @Column(name = "tokens", nullable = false)
    @JsonIgnore
    private Double tokens;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private List<Transaction> transactions;


    public boolean sameUser(User other) {
        return Objects.equals(other.getUserId(), userId);
    }
}