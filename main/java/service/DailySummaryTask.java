package org.example.zorvyn.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DailySummaryTask {

    private final PortfolioService portfolioService;

    public DailySummaryTask(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // Runs every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailySummary() {
        Map<String, Double> pl = portfolioService.calculateProfitLoss();
        System.out.println("🔥 Daily Portfolio Summary: " + pl);
    }
}