package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Appointment;
import com.example.barbershopmanagementsystem.Model.Barber;
import com.example.barbershopmanagementsystem.Service.AppointmentService;
import com.example.barbershopmanagementsystem.Service.BarberService;
import com.example.barbershopmanagementsystem.Service.HelperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/barber")
public class BarberController {
    private final BarberService barberService;
    private final AppointmentService appointmentService;
    private final HelperService helperService;

    @GetMapping("/get-all-barbers")
    public ResponseEntity<List<Barber>> getAllBarbers() {
        return ResponseEntity.status(HttpStatus.OK).body(barberService.getAllBarbers());
    }

    @PostMapping("/add-barber")
    public ResponseEntity<ApiResponse> addBarber(@RequestBody @Valid Barber barber, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(barberService.addBarber(barber));
    }

    @PutMapping("/update-barber/{barberId}")
    public ResponseEntity<ApiResponse> updateBarber(@PathVariable Integer barberId, @RequestBody @Valid Barber barber, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(barberService.updateBarber(barberId, barber));
    }

    @DeleteMapping("/delete-barber/{barberId}")
    public ResponseEntity<ApiResponse> deleteBarber(@PathVariable Integer barberId) {
        return helperService.getResponse(barberService.deleteBarber(barberId));
    }

    //4-
    @GetMapping("get-barber-appointments/{barberId}")
    public ResponseEntity<List<Appointment>> getBarberAppointments(@PathVariable Integer barberId) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getBarberAppointments(barberId));
    }

    //5-
    @GetMapping("/get-barber-total-revenue/{barberId}")
    public ResponseEntity<ApiResponse> getShopTotalRevenue(@PathVariable Integer barberId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(barberService.getBarberTotalRevenue(barberId)));
    }

    //6-
    @GetMapping("/get-barber-stats/{barberId}")
    public ResponseEntity<ApiResponse> getBarberStats(@PathVariable Integer barberId) {
        return helperService.getResponse(barberService.getBarberStats(barberId));
    }
}
