package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Appointment;
import com.example.barbershopmanagementsystem.Model.Client;
import com.example.barbershopmanagementsystem.Service.AppointmentService;
import com.example.barbershopmanagementsystem.Service.ClientService;
import com.example.barbershopmanagementsystem.Service.HelperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/client")
public class ClientController {
    private final ClientService clientService;
    private final AppointmentService appointmentService;
    private final HelperService helperService;

    @GetMapping("/get-all-clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllClients());
    }

    @PostMapping("/add-client")
    public ResponseEntity<ApiResponse> addClient(@RequestBody @Valid Client client, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(clientService.addClient(client));
    }

    @PutMapping("/update-client/{clientId}")
    public ResponseEntity<ApiResponse> updateClient(@PathVariable Integer clientId, @RequestBody @Valid Client client, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(clientService.updateClient(clientId, client));
    }

    @DeleteMapping("/delete-client/{clientId}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Integer clientId) {
        return helperService.getResponse(clientService.deleteClient(clientId));
    }

    //7-
    @GetMapping("/check-barber-availability/{barberId}/{date}")
    public ResponseEntity<ApiResponse> checkBarberAvailability(@PathVariable Integer barberId, @PathVariable LocalDateTime date, LocalDateTime finishTime) {
        Appointment appointment = appointmentService.checkBarberAvailability(barberId, date, finishTime);
        if (appointment == null)
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Barber not available."));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Barber available."));
    }

    //8-
    @GetMapping("/get-client-appointments/{clientId}")
    public ResponseEntity<List<Appointment>> getClientAppointments(@PathVariable Integer clientId) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getClientAppointments(clientId));
    }

    //9-
    @GetMapping("/check-if-client-eligible-for-over/{clientId}")
    public ResponseEntity<ApiResponse> checkIfClientEligibleForOver(@PathVariable Integer clientId) {
        return helperService.getResponse(clientService.checkIfClientEligibleForOver(clientId));
    }

    //10-
    @PutMapping("/redeem-over/{clientId}/{orderId}")
    public ResponseEntity<ApiResponse> redeemOver(@PathVariable Integer clientId, @PathVariable Integer orderId) {
        return helperService.getResponse(clientService.redeemOver(clientId, orderId));
    }

}
