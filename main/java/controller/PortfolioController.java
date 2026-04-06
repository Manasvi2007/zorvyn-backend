package org.example.zorvyn.controller;

import org.example.zorvyn.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/profit-loss")
    public Map<String, Double> getProfitLoss() {
        return portfolioService.calculateProfitLoss();
    }
}
