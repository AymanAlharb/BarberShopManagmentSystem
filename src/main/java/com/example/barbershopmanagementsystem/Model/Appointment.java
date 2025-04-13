package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "datetime")
    private LocalDateTime appointmentDate;
    @Column(columnDefinition = "datetime")
    //appointmentData + order.duration.
    private LocalDateTime appointmentFinishTime;
    @NotNull(message = "The Barber Id must not be null.")
    @Column(columnDefinition = "int not null")
    private Integer barberId;
    @NotNull(message = "The Client Id must not be null.")
    @Column(columnDefinition = "int not null")
    private Integer clientId;
    @Column(columnDefinition = "int")
    private Integer orderId;
    //Constructor to create an appointment.
    public Appointment(LocalDateTime appointmentDate, Integer clientId, Integer barberId) {
        this.clientId = clientId;
        this.appointmentDate = appointmentDate;
        this.barberId = barberId;
    }
}
