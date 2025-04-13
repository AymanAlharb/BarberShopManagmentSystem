package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelperService {

    public ResponseEntity<ApiResponse> getResponse(String statusMessage) {
        if (statusMessage.contains("not found"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(statusMessage));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(statusMessage));
    }

    public int calculateRevenue(List<Order> orderList){
        int  totalRevenue = 0;
        for (Order order : orderList) totalRevenue += order.getOrderPrice();
        return totalRevenue;
    }
}
