package org.example.zorvyn.controller;

import org.example.zorvyn.model.Price;
import org.example.zorvyn.service.MarketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/price/btc")
    public String getBtcPrice() {
        return marketService.getBtcPrice();
    }

    @GetMapping("/sentiment")
    public String getSentiment() {
        return marketService.getSentiment();
    }

    @GetMapping("/live/prices")
    public Map<String, Object> getLivePrices() {
        return marketService.getLivePrices();
    }

    @GetMapping("/prices")
    public Map<String, Object> getPrices() {
        return marketService.getPrices();
    }

    // 🔥 BONUS ENDPOINT (for charts/history)
    @GetMapping("/history/{coin}")
    public List<Price> getHistory(@PathVariable String coin) {
        return marketService.getHistory(coin);
    }
}