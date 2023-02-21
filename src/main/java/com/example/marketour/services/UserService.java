package com.example.marketour.services;

import com.example.marketour.model.entities.*;
import com.example.marketour.repositories.ImageRepository;
import com.example.marketour.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public UserService(UserRepository userRepository,
                       ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public User checkCredentialsExist(String username, String password) {
        return userRepository.findAll().stream().filter(user -> Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)).findFirst().orElse(null);
    }

    public User checkUsernameExists(String username) {
        return userRepository.findAll().stream().filter(user -> Objects.equals(user.getUsername(), username)).findFirst().orElse(null);
    }

    public User createUser(String username, String password, UserType userType, Double tokens, City city, Country country) {
        final var newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setUserType(userType);
        newUser.setTokens(tokens);
        newUser.setCountry(country);
        newUser.setCity(city);
        return userRepository.save(newUser);
    }

    public void setAvatar(byte[] avatar, User user) {
        final var image = new Image();
        image.setDescription(user.getUsername() + " avatar");
        imageRepository.save(image);
        image.setData(avatar);
        user.setImage(image);
        userRepository.save(user);
    }

    public User getUser(Object userId) {
        return userRepository.findAll().stream().filter(user -> Objects.equals(user.getUserId(), userId)).findFirst().orElse(null);
    }
}
