package org.example.zorvyn.service;

import org.example.zorvyn.model.Trade;
import org.example.zorvyn.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository repo;

    public TradeService(TradeRepository repo) {
        this.repo = repo;
    }

    // CREATE
    public Trade saveTrade(Trade t) {
        t.setCreatedAt(LocalDateTime.now()); // auto timestamp
        return repo.save(t);
    }

    // READ ALL
    public List<Trade> getAllTrades() {
        return repo.findAll();
    }

    // FILTER
    public List<Trade> filterByCoin(String coin) {
        return repo.findByCoin(coin);
    }

    // DELETE
    public void deleteTrade(Long id) {
        repo.deleteById(id);
    }

    // GET ONE
    public Trade getById(Long id) {
        return repo.findById(id).orElseThrow();
    }
}