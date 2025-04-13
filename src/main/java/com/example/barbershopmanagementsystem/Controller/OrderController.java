package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Order;
import com.example.barbershopmanagementsystem.Service.HelperService;
import com.example.barbershopmanagementsystem.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/order")
public class OrderController {
    private final OrderService orderService;
    private final HelperService helperService;
    @GetMapping("/get-all-orders")
    public ResponseEntity getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @PostMapping("/add-order")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody @Valid Order order, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(orderService.addOrder(order));
    }

    @PutMapping("/update-order/{orderId}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Integer orderId, @RequestBody @Valid Order order, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(orderService.updateOrder(orderId, order));

    }

    @DeleteMapping("/delete-order/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Integer orderId){
        return helperService.getResponse(orderService.deleteOrder(orderId));

    }

    //11-
    @PutMapping("/complete-order/{orderId}")
    public ResponseEntity<ApiResponse> completeOrderStatus(@PathVariable Integer orderId){
        return helperService.getResponse(orderService.completeOrderStatus(orderId));
    }

    //12-
    @PutMapping("/cancel-order/{orderId}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Integer orderId){
        return helperService.getResponse(orderService.cancelOrder(orderId));
    }

    //13-
    @PutMapping("/un-cancel-order/{orderId}")
    public ResponseEntity<ApiResponse> unCancelOrder(@PathVariable Integer orderId){
        return helperService.getResponse(orderService.unCancelOrder(orderId));
    }

    //14-
    @PutMapping("/apply-discount/{orderId}/{numberOfServicesNeeded}/{discountAmount}")
    public ResponseEntity<ApiResponse> applyDiscountToAnOrder(@PathVariable Integer orderId, @PathVariable int numberOfServicesNeeded, @PathVariable double discountAmount){
        return helperService.getResponse(orderService.applyDiscountToAnOrder(orderId, numberOfServicesNeeded, discountAmount));
    }

}
