package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Integer> {
    Barber findBarberById(Integer id);

}
