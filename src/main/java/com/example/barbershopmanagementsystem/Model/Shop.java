package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
//Since the system is designed for one shop The attribute of the shop models are hardcoded.
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int")
    private int numberOfPointsNeededForOver = 2;
    //When the system server running for the first time the shopInitialized need to be true using the initializeShop endpoints.
    private Boolean shopInitialized = false;
}
