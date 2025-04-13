package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(Integer id);

    @Query("select o from Order o where o.orderDone = true")
    List<Order> findCompletedOrders();

    @Query("select o from Order o where o.orderDone = true and o.barberId = ?1")
    List<Order> findCompletedOrdersByBarberId(Integer barberId);

    @Query("select count(o) from Order o where o.clientId = ?1 and o.orderDone = true")
    Integer countAllCompletedOrdersByClientId(Integer clientId);
}
