package com.example.marketour.services;

import com.example.marketour.model.entities.Tour;
import com.example.marketour.model.entities.Transaction;
import com.example.marketour.model.entities.User;
import com.example.marketour.repositories.GuideTourRepository;
import com.example.marketour.repositories.TransactionRepository;
import com.example.marketour.repositories.UserRepository;
import com.example.marketour.repositories.money_repository.MoneyRepository;
import com.example.marketour.repositories.money_repository.MoneyRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final GuideTourRepository guideTourRepository;
    private final UserRepository userRepository;

    private final MoneyRepository moneyRepository = new MoneyRepositoryImpl();

    public TransactionsService(TransactionRepository transactionRepository, GuideTourRepository guideTourRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.guideTourRepository = guideTourRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllOfUser(User user) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getTourist().getUserId(), user.getUserId()) || Objects.equals(transaction.getGuide().getUserId(), user.getUserId()))
                .collect(Collectors.toList());
    }

    public void newTransaction(User user, Tour tour) {
        final var transaction = new Transaction();
        final var gTour = guideTourRepository.findAll().stream().filter(guideTour -> Objects.equals(guideTour.getTour().getTourId(), tour.getTourId())).findFirst().orElse(null);
        if (gTour != null) {
            transaction.setGuide(gTour.getGuide());
            final var guide = gTour.getGuide();
            user.setTokens(user.getTokens() - tour.getPrice());

            guide.setTokens(guide.getTokens() + tour.getPrice());
            userRepository.save(user);
            userRepository.save(guide);
            moneyRepository.addMoney(tour.getPrice(), guide);
            moneyRepository.removeMoney(tour.getPrice(), user);

        }
        transaction.setTourist(user);
        transaction.setTour(tour);
        transaction.setPurchaseTime(System.currentTimeMillis());
        transactionRepository.save(transaction);

    }
}
