package com.example.marketour.services;

import com.example.marketour.model.entities.User;
import com.example.marketour.repositories.UserRepository;
import com.example.marketour.repositories.money_repository.MoneyRepository;
import com.example.marketour.repositories.money_repository.MoneyRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class BankService {
    private final MoneyRepository moneyRepository = new MoneyRepositoryImpl();
    private final UserRepository userRepository;

    public BankService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addTokens(User user, Double amount) {
        user.setTokens(user.getTokens() + amount);
        userRepository.save(user);
        moneyRepository.removeMoney(amount, user);
    }

    public void removeTokens(User user, Double amount) {
        user.setTokens(user.getTokens() - amount);
        userRepository.save(user);
        moneyRepository.addMoney(amount, user);
    }

    public void removeFunds(User user, Double amount) {
        moneyRepository.removeMoney(amount, user);
    }

    public void addFunds(User user, Double amount) {
        moneyRepository.addMoney(amount, user);
    }

    public void setTokens(User user, Double amount) {
        final var oldTokens = user.getTokens();
        user.setTokens(amount);
        final double difference = oldTokens - amount;
        userRepository.save(user);
        if (difference > 0) {
            moneyRepository.addMoney(difference, user);
        } else {
            moneyRepository.removeMoney(difference, user);
        }
    }
}
