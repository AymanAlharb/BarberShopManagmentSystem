package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double servicePrice;
    @NotEmpty(message = "The Service can not be null.")
    @Size(min = 4, message = "The Service must be more than three characters long.")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String serviceName;
    @NotNull(message = "The Durations In Minute can not be null.")
    @Column(columnDefinition = "int not null")
    //How long the service takes.
    private Integer durationsInMinute;
}
