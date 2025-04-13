package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.ServiceItem;
import com.example.barbershopmanagementsystem.Service.HelperService;
import com.example.barbershopmanagementsystem.Service.ServiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/service-item")
public class ServiceItemController {

    private final ServiceItemService serviceItemService;
    private final HelperService helperService;

    @GetMapping("/get-all-service-items")
    public ResponseEntity<List<ServiceItem>> getAllServiceItems() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceItemService.getAllServiceItems());
    }

    @PostMapping("/add-service-item")
    public ResponseEntity<ApiResponse> addServiceItem(@RequestBody @Valid ServiceItem serviceItem, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(serviceItemService.addServiceItem(serviceItem));
    }

    @PutMapping("/update-service-item/{serviceItemId}")
    public ResponseEntity<ApiResponse> updateServiceItem(@PathVariable Integer serviceItemId,
                                                         @RequestBody @Valid ServiceItem serviceItem, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(serviceItemService.updateServiceItem(serviceItemId, serviceItem));
    }

    @DeleteMapping("/delete-service-item/{serviceItemId}")
    public ResponseEntity<ApiResponse> deleteServiceItem(@PathVariable Integer serviceItemId) {
        return helperService.getResponse(serviceItemService.deleteServiceItem(serviceItemId));
    }
}
