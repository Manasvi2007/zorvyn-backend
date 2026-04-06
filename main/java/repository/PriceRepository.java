package org.example.zorvyn.repository;

import org.example.zorvyn.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    // 🔥 Get all data of a specific coin
    List<Price> findByCoin(String coin);

    // 🔥 Get last 10 entries (for charts / history)
    List<Price> findTop10ByCoinOrderByTimestampDesc(String coin);
}