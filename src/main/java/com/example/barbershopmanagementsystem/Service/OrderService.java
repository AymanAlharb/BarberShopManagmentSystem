package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.Appointment;
import com.example.barbershopmanagementsystem.Model.Client;
import com.example.barbershopmanagementsystem.Model.Order;
import com.example.barbershopmanagementsystem.Repository.AppointmentRepository;
import com.example.barbershopmanagementsystem.Repository.BarberRepository;
import com.example.barbershopmanagementsystem.Repository.ClientRepository;
import com.example.barbershopmanagementsystem.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public OrderService(OrderRepository orderRepository, AppointmentService appointmentService, AppointmentRepository appointmentRepository, ClientRepository clientRepository, BarberRepository barberRepository) {
        this.orderRepository = orderRepository;
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.barberRepository = barberRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public String addOrder(Order order) {
        //Get the appointment object and check if it's in the database. * No orders can be made without an appointment.
        Appointment tempAppointment = appointmentRepository.findAppointmentById(order.getAppointmentId());
        if (tempAppointment == null) return "Appointment not found.";
        orderRepository.save(order);
        return "Order added successfully.";
    }

    public String updateOrder(Integer orderId, Order order) {
        //Get the order object and check if it's in the database.
        Order tempOrder = orderRepository.findOrderById(orderId);
        if (tempOrder == null) return "Order not found.";
        tempOrder.setOrderPrice(order.getOrderPrice());
        tempOrder.setAppointmentId((order.getAppointmentId()));
        orderRepository.save(tempOrder);
        return "Order updated successfully.";
    }

    public String deleteOrder(Integer orderId) {
        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";
        orderRepository.delete(order);
        return "Order deleted successfully.";
    }

    //This method completes an order.
    public String completeOrderStatus(Integer orderId) {
        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Check if the order is already completed.
        if (order.getOrderDone()) return "Order already completed.";
        order.setOrderDone(true);
        orderRepository.save(order);

        //Get the client to increase the client points.
        Client client = clientRepository.findClientById(order.getClientId());

        //If the order price is 0 this means that this order has redeemed over in it and this over does not increase the clients points.
        if (order.getOrderPrice() != 0) client.setPoints(client.getPoints() + 1);
        clientRepository.save(client);
        return "Order completed successfully.";
    }

    //This method cancels an order.
    public String cancelOrder(Integer orderId) {
        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Only un completed orders can be canceled so check if the order is completed.
        if (order.getOrderDone()) return "Completed orders can not be canceled.";

        //Check if the order is already canceled.
        if (order.getCanceled()) return "Ordered already canceled";
        order.setCanceled(true);
        Appointment appointment = appointmentRepository.findAppointmentById(order.getAppointmentId());
        appointmentRepository.delete(appointment);
        orderRepository.save(order);
        return "Order canceled successfully.";
    }

    //This method uncancels orders
    public String unCancelOrder(Integer orderId) {
        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Only un completed orders can be canceled so check if the order is completed.
        if (order.getOrderDone()) return "this order is completed orders can not be canceled.";
        Appointment appointment = appointmentRepository.findAppointmentById(order.getAppointmentId());
        Appointment appointment1 =  appointmentRepository.checkBarberAvailability(order.getBarberId(), appointment.getAppointmentDate(), appointment.getAppointmentFinishTime());
        //Check if the barber available.
        if(appointment1 == null) return "Barber is not available anymore.";
        //Check if the order is already canceled.
        if (!order.getCanceled()) return "Order not canceled";

        //Uncancle the oder.
        order.setCanceled(false);

        appointmentRepository.save(appointment);
        orderRepository.save(order);
        return "Order uncanceled successfully.";
    }

    //This method is used by owners to apply a discount on an order, by comparing the number of completed services by the client and the number of completed services needed.
    public String applyDiscountToAnOrder(Integer orderId, int numberOfServicesNeeded, double discountAmount) {
        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Check if the order is completed.
        if (order.getOrderDone()) return "Order completed! you can not apply discount on completed orders.";
        Integer clientId = order.getClientId();

        //Get the client completed services and compare it to the number of services needed.
        int totalClientServices = orderRepository.countAllCompletedOrdersByClientId(clientId);
        if (totalClientServices >= numberOfServicesNeeded) {
            order.setOrderPrice(order.getOrderPrice() - order.getOrderPrice() * discountAmount);
            orderRepository.save(order);
            return "Discount applied successfully. New order price is: " + order.getOrderPrice();
        }
        return "Client need " + (numberOfServicesNeeded - totalClientServices) + " more services to have a discount.";
    }

}



