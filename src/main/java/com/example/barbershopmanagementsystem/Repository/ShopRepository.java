package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
    @Query("select s.numberOfPointsNeededForOver from Shop s")
    Integer findNumberOfPointsNeededForOver();

    @Query("select s.shopInitialized from Shop s")
    Boolean isShopInitialized();
}
