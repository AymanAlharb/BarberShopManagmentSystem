package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Service.HelperService;
import com.example.barbershopmanagementsystem.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/shop")
public class ShopController {
    private final ShopService shopService;
    private final HelperService helperService;

    //27-
    @GetMapping("/get-shop-total-revenue")
    public ResponseEntity<ApiResponse> getShopTotalRevenue(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(shopService.getShopTotalRevenue()));
    }

    @PutMapping("/initialize-shop")
    public ResponseEntity<ApiResponse> initializeShop(){
        return helperService.getResponse(shopService.initializeShop());
    }

    @GetMapping("/is-shop-initialized")
    public ResponseEntity<ApiResponse> isShopInitialized(){
        return helperService.getResponse(shopService.isShopInitialized());
    }
}
