package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.Order;
import com.example.barbershopmanagementsystem.Model.Shop;
import com.example.barbershopmanagementsystem.Repository.AppointmentRepository;
import com.example.barbershopmanagementsystem.Repository.OrderRepository;
import com.example.barbershopmanagementsystem.Repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    private final OrderRepository orderRepository;
    private final HelperService helperService;
    private final ShopRepository shopRepository;

    public ShopService(OrderRepository orderRepository, HelperService helperService, AppointmentRepository appointmentRepository, ShopRepository shopRepository) {
        this.orderRepository = orderRepository;
        this.helperService = helperService;
        this.shopRepository = shopRepository;
    }

    public String getShopTotalRevenue() {
        List<Order> orderList = orderRepository.findCompletedOrders();
        return "The shop total revenue is " + helperService.calculateRevenue(orderList);
    }

    public String initializeShop() {
        if (shopRepository.isShopInitialized() == null) {
            Shop shop = new Shop();
            shop.setShopInitialized(true);
            shopRepository.save(shop);
            return "Shop initialized successfully.";
        }
        return "Shop already initialized.";
    }

    public String isShopInitialized() {
        if (shopRepository.isShopInitialized() == null) {
            return "Shop initialized.";
        }
        return "Shop not initialized.";
    }

}
