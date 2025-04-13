package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Appointment;
import com.example.barbershopmanagementsystem.Service.AppointmentService;
import com.example.barbershopmanagementsystem.Service.HelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final HelperService helperService;

    @GetMapping("/get-all-appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllAppointments());
    }

    @DeleteMapping("/delete-appointment/{appointmentId}")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable Integer appointmentId) {
        return helperService.getResponse(appointmentService.deleteAppointment(appointmentId));
    }

    //1-
    @PostMapping("/book-appointment/{clientId}/{barberId}/{date}/{serviceName}")
    public ResponseEntity<ApiResponse> bookAppointment(@PathVariable Integer clientId, @PathVariable Integer barberId, @PathVariable LocalDateTime date, @PathVariable String serviceName) {
        return helperService.getResponse(appointmentService.requestAppointment(clientId, barberId, date, serviceName));
    }

    //2-
    @PutMapping("/reschedule-appointment/{appointmentId}/{clientId}/{barberId}/{date}/{serviceName}")
    public ResponseEntity<ApiResponse> rescheduleAppointment(@PathVariable Integer appointmentId, @PathVariable Integer clientId, @PathVariable Integer barberId, @PathVariable LocalDateTime date, @PathVariable String serviceName) {
        return helperService.getResponse(appointmentService.rescheduleAppointment(appointmentId, clientId, barberId, date, serviceName));
    }

    //3-
    @PutMapping("/change-barber/{appointmentId}/{barberId}")
    public ResponseEntity<ApiResponse> changeBarber(@PathVariable Integer appointmentId, @PathVariable Integer barberId){
        return helperService.getResponse(appointmentService.changeBarber(appointmentId, barberId));
    }
}
