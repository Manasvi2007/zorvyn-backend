package org.example.zorvyn.controller;

import org.example.zorvyn.model.Trade;
import org.example.zorvyn.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin
public class TradeController {

    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    // CREATE (Buy/Sell)
    @PostMapping
    public Trade save(@RequestBody Trade t) {
        return service.saveTrade(t);
    }

    // READ (All trades)
    @GetMapping
    public List<Trade> all() {
        return service.getAllTrades();
    }

    // FILTER (by coin)
    @GetMapping("/filter")
    public List<Trade> filterByCoin(@RequestParam String coin) {
        return service.filterByCoin(coin);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteTrade(@PathVariable Long id) {
        service.deleteTrade(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Trade updateTrade(@PathVariable Long id, @RequestBody Trade updated) {

        Trade t = service.getById(id);

        t.setAmount(updated.getAmount());
        t.setCoin(updated.getCoin());
        t.setType(updated.getType());

        return service.saveTrade(t);
    }
}