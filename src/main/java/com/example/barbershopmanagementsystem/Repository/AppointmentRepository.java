package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment findAppointmentById(Integer id);

    @Query("select a from Appointment a where a.barberId = ?1 and (a.appointmentDate between ?2 and ?3 or a.appointmentFinishTime between ?2 and ?3)")
    Appointment checkBarberAvailability(Integer barberId, LocalDateTime date, LocalDateTime appointmentFinishTime);

    @Query("Select a from Appointment a where a.clientId = ?1 and (a.appointmentDate BETWEEN ?2 AND ?3 OR a.appointmentFinishTime BETWEEN ?2 AND ?3)")
    Appointment checkClientAvailability(Integer clientId, LocalDateTime date, LocalDateTime appointmentFinishTime);

    @Query("select a from Appointment a where a.barberId = ?1")
    List<Appointment> findBarberAppointments(Integer barberId);

    @Query("select a from Appointment a where a.clientId = ?1")
    List<Appointment> findClientAppointments(Integer clientId);

}
