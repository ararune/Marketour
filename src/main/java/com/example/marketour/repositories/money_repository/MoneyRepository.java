package com.example.marketour.repositories.money_repository;

import com.example.marketour.model.entities.User;

public interface MoneyRepository {
    void removeMoney(Double amount, User user);

    void addMoney(Double amount, User user);
}
