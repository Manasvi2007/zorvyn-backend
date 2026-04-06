package org.example.zorvyn.service;

import org.example.zorvyn.model.Price;
import org.example.zorvyn.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private RestTemplate restTemplate;

    // OLD METHODS (kept)
    public String getBtcPrice() {
        return "BTC Price is 61780";
    }

    public String getSentiment() {
        return "Market sentiment is Fear";
    }

    // 🔥 MAIN METHOD (LIVE API + DB SAVE)
    public Map<String, Object> getLivePrices() {

        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,solana&vs_currencies=inr";

        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "API not responding");
            return error;
        }

        Map btc = (Map) response.get("bitcoin");
        Map eth = (Map) response.get("ethereum");
        Map sol = (Map) response.get("solana");

        Number btcNum = (Number) btc.get("inr");
        Number ethNum = (Number) eth.get("inr");
        Number solNum = (Number) sol.get("inr");

        double btcPrice = btcNum.doubleValue();
        double ethPrice = ethNum.doubleValue();
        double solPrice = solNum.doubleValue();

        // ✅ SAVE USING PRICE (FINAL FIX)
        priceRepository.save(new Price("bitcoin", btcPrice));
        priceRepository.save(new Price("ethereum", ethPrice));
        priceRepository.save(new Price("solana", solPrice));

        return response;
    }

    // 🔥 USED BY FRONTEND (/prices)
    public Map<String, Object> getPrices() {
        return getLivePrices();
    }

    // 🔥 BONUS: history (makes project strong)
    public List<Price> getHistory(String coin) {
        return priceRepository.findTop10ByCoinOrderByTimestampDesc(coin);
    }
}