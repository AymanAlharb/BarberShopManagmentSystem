package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "The order price can not be null.")
    @Column(columnDefinition = "int not null")
    private Double orderPrice;
    @Column(columnDefinition = "boolean")
    private Boolean orderDone = false;
    @Column(columnDefinition = "boolean")
    private Boolean canceled = false;
    @Column(columnDefinition = "int")
    private Integer serviceItemId;
    @Column(columnDefinition = "int")
    private Integer appointmentId;
    @Column(columnDefinition = "int")
    private Integer barberId;
    @Column(columnDefinition = "int")
    private Integer clientId;
    @Column(columnDefinition = "int")
    private Integer reviewId;

}
