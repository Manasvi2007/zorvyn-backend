package org.example.zorvyn.repository;
import java.util.List;

import org.example.zorvyn.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findByCoin(String coin);
}