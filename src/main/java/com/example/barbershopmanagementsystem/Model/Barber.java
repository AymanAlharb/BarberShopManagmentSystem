package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Barber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Positive(message = "The age should be a positive number.")
    private Integer age;
    @NotEmpty(message = "The First Name can not be null.")
    @Size(min = 4, message = "The First Name must be more than three characters long.")
    @Column(columnDefinition = "varchar(20) not null")
    private String firstName;
    @NotEmpty(message = "The Last Name can not be null.")
    @Size(min = 4, message = "The Last Name must be more than three characters long.")
    @Column(columnDefinition = "varchar(20) not null")
    private String lastName;
    @Size(min = 10, max = 10, message = "The Phone Number must be 10 characters long.")
    @Pattern(regexp = "05[0-9]{8}")
    @NotEmpty(message = "The Phone Number can not be null.")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String phoneNumber;
}
