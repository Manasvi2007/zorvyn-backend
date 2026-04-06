package org.example.zorvyn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String coin;
    private double amount;
    private String type;

    private LocalDateTime createdAt;

    public Trade() {}

    public Trade(String coin, double amount, String type) {
        this.coin = coin;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getCoin() { return coin; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCoin(String coin) { this.coin = coin; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}