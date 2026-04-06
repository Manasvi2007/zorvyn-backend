package org.example.zorvyn.service;

import org.example.zorvyn.model.Price;
import org.example.zorvyn.model.Trade;
import org.example.zorvyn.repository.PriceRepository;
import org.example.zorvyn.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioService {

    private final TradeRepository tradeRepo;
    private final PriceRepository priceRepo;

    public PortfolioService(TradeRepository tradeRepo, PriceRepository priceRepo) {
        this.tradeRepo = tradeRepo;
        this.priceRepo = priceRepo;
    }

    // 🔥 Calculate profit/loss per coin
    public Map<String, Double> calculateProfitLoss() {
        List<Trade> trades = tradeRepo.findAll();
        Map<String, Double> holdings = new HashMap<>();
        Map<String, Double> invested = new HashMap<>();

        for (Trade t : trades) {
            String coin = t.getCoin().toLowerCase();
            double amt = t.getAmount();
            double latestPrice = getLatestPrice(coin);

            if (t.getType().equalsIgnoreCase("buy")) {
                holdings.put(coin, holdings.getOrDefault(coin, 0.0) + amt);
                invested.put(coin, invested.getOrDefault(coin, 0.0) + amt * latestPrice);
            } else if (t.getType().equalsIgnoreCase("sell")) {
                holdings.put(coin, holdings.getOrDefault(coin, 0.0) - amt);
                invested.put(coin, invested.getOrDefault(coin, 0.0) - amt * latestPrice);
            }
        }

        Map<String, Double> profitLoss = new HashMap<>();
        for (String coin : holdings.keySet()) {
            double currentValue = holdings.get(coin) * getLatestPrice(coin);
            double pl = currentValue - invested.getOrDefault(coin, 0.0);
            profitLoss.put(coin, pl);
        }

        return profitLoss;
    }

    private double getLatestPrice(String coin) {
        List<Price> prices = priceRepo.findTop10ByCoinOrderByTimestampDesc(coin);
        return prices.isEmpty() ? 0 : prices.get(0).getPrice();
    }
}