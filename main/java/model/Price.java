package org.example.zorvyn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String coin;
    private double price;

    private LocalDateTime timestamp;

    public Price() {}

    public Price(String coin, double price) {
        this.coin = coin;
        this.price = price;
        this.timestamp = LocalDateTime.now(); // ✅ FIXED
    }

    public Long getId() { return id; }
    public String getCoin() { return coin; }
    public double getPrice() { return price; }
    public LocalDateTime getTimestamp() { return timestamp; } // ✅ FIXED

    public void setCoin(String coin) { this.coin = coin; }
    public void setPrice(double price) { this.price = price; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; } // ✅ FIXED
}