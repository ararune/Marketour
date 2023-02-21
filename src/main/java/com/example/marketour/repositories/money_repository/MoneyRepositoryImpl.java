package com.example.marketour.repositories.money_repository;

import com.example.marketour.model.entities.User;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyRepositoryImpl implements MoneyRepository {
    @Override
    public void removeMoney(Double amount, User user) {
        if (amount - 1 <= 0.001) {
            Logger.getGlobal().log(Level.INFO, "Removed " + amount + " dollar from bank for " + user.getUsername() + "!");
        } else {
            Logger.getGlobal().log(Level.INFO, "Removed " + amount + " dollars from bank for " + user.getUsername() + "!");
        }
    }

    @Override
    public void addMoney(Double amount, User user) {
        if (amount - 1 <= 0.001) {
            Logger.getGlobal().log(Level.INFO, "Added " + amount + " dollar to bank for " + user.getUsername() + "!");

        } else {
            Logger.getGlobal().log(Level.INFO, "Added " + amount + " dollars to bank for " + user.getUsername() + "!");
        }
    }
}
