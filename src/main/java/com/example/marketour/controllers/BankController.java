package com.example.marketour.controllers;

import com.example.marketour.model.entities.User;
import com.example.marketour.services.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bank")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/addTokens/{tokens}")
    public ResponseEntity<Object> addTokens(@PathVariable Double tokens, HttpServletRequest request) {
        final var session = request.getSession();
        if (session != null) {
            final var user = (User) session.getAttribute("user");
            if (user != null) {
                bankService.addTokens(user, tokens);
                return ResponseEntity.ok("Successfully added " + tokens + " tokens!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
        }
    }

    @PostMapping("/removeTokens/{tokens}")
    public ResponseEntity<Object> removeTokens(@PathVariable Double tokens, HttpServletRequest request) {
        final var session = request.getSession();
        if (session != null) {
            final var user = (User) session.getAttribute("user");
            if (user != null) {
                if (user.getTokens() < tokens) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdraw amount is too high!");
                }
                bankService.removeTokens(user, tokens);
                return ResponseEntity.ok("Successfully removed " + tokens + " tokens!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
        }
    }

    @GetMapping("/getTokens")
    public ResponseEntity<Object> getTokens(HttpServletRequest request) {
        final var session = request.getSession();
        if (session != null) {
            final var user = (User) session.getAttribute("user");
            if (user != null) {
                return ResponseEntity.ok(user.getTokens());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User logged out!");
        }
    }
}
