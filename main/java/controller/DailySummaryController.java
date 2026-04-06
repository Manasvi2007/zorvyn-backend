package org.example.zorvyn.controller;

import org.example.zorvyn.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin(origins = "*")
public class DailySummaryController {

    private final PortfolioService portfolioService;

    public DailySummaryController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/today")
    public Map<String, Object> getTodaySummary() {

        Map<String, Double> pl = portfolioService.calculateProfitLoss();

        double total = pl.values().stream().mapToDouble(Double::doubleValue).sum();

        Map<String, Object> res = new HashMap<>();
        res.put("profitLoss", pl);
        res.put("totalPL", total);

        return res;
    }
    @GetMapping("/recommend")
    public String recommend() {

        Map<String, Double> pl = portfolioService.calculateProfitLoss();

        double totalPL = pl.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalPL > 1000)
            return "🚀 Strong Bull Market — Buy aggressively";
        else if (totalPL > 200)
            return "📈 Uptrend — Buy on dips";
        else if (totalPL < -1000)
            return "⚠️ Crash risk — Exit market";
        else if (totalPL < 0)
            return "📉 Downtrend — Avoid buying";
        else
            return "🤔 Sideways market — Hold";
    }
}