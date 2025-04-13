package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Pattern(regexp = "up|down")
    @Column(columnDefinition = "varchar(4)")
    private String vote;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer clientId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer reviewId;
}
