package com.example.barbershopmanagementsystem.Service;
import com.example.barbershopmanagementsystem.Model.*;
import com.example.barbershopmanagementsystem.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final OrderRepository orderRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ClientRepository clientRepository1, BarberRepository barberRepository1, ServiceItemRepository serviceItemRepository, OrderRepository orderRepository) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository1;
        this.barberRepository = barberRepository1;
        this.serviceItemRepository = serviceItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public String deleteAppointment(Integer appointmentId) {
        Appointment tempAppointment = appointmentRepository.findAppointmentById(appointmentId);
        if (tempAppointment == null) return "Appointment not found.";
        appointmentRepository.delete(tempAppointment);
        return "Appointment deleted successfully.";
    }

    //This method is to request an appointment.
    public String requestAppointment(Integer clientId, Integer barberId, LocalDateTime appointmentDate, String serviceName) {
        //Call the appointmentOperations method and check the status of the operations.
        String status = appointmentOperations(clientId, barberId, appointmentDate, serviceName);

        //If the appointment not created return the status.
        if (status != null) return status;
        return "Appointment added successfully.";
    }

    //This method is to reschedule an appointment.
    public String rescheduleAppointment(Integer appointmentId, Integer clientId, Integer barberId, LocalDateTime appointmentDate, String serviceName) {
        //Get the appointment object and check if it's in the database.
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);
        if(appointment == null) return "Appointment not found";
        String status = appointmentOperations(clientId, barberId, appointmentDate, serviceName);
        if (status != null) return status;
        appointmentRepository.delete(appointment);
        return "Appointment updated successfully.";
    }

    //This is a helper method creating or updating an appointment.
    private String appointmentOperations(Integer clientId, Integer barberId, LocalDateTime appointmentDate, String serviceName) {
        //Get the objects.
        Client client = clientRepository.findClientById(clientId);
        Barber barber = barberRepository.findBarberById(barberId);
        ServiceItem serviceItem = serviceItemRepository.findAllByServiceName(serviceName);

        //Validate the appointment.
        String status = validateAppointment(client, clientId, barberId, barber, serviceItem, appointmentDate);

        //If the appointment not valid return the status.
        if(!status.equalsIgnoreCase("Valid")) return status;

        //Create the appointment.
        createAppointment(clientId, barberId, appointmentDate, serviceItem);
        return null;
    }

    public String validateAppointment(Client client, Integer clientId, Integer barberId, Barber barber, ServiceItem serviceItem, LocalDateTime appointmentDate){
        //Check if the client not in the database.
        if (client == null) return "Client not found.";

        //Check if the barber not in the database.
        if (barber == null) return "Barber not found.";

        //Check if the serviceItem not in the database.
        if (serviceItem == null) return "Service not available.";

        //Calculate the appointment finish time.
        LocalDateTime finishTime = appointmentDate.plusMinutes(serviceItem.getDurationsInMinute());

        //Check if the barber is available.
        if (checkBarberAvailability(barberId, appointmentDate, finishTime) != null) return "Barber not available.";

        //Check if the clint have an appointment at this time.
        if (checkClientAvailability(clientId, appointmentDate, finishTime) != null) return "Client already has an appointment at this time.";
        return "Valid";
    }

    //This is a helper method to create an appointment.
    public void createAppointment(Integer clientId, Integer barberId, LocalDateTime appointmentDate, ServiceItem serviceItem){
        //Create the appointment and the order.
        Appointment appointment = new Appointment(appointmentDate, clientId, barberId);
        appointment.setAppointmentFinishTime(appointmentDate.plusMinutes(serviceItem.getDurationsInMinute()));
        appointmentRepository.save(appointment);
        Order order = new Order();
        order.setAppointmentId(appointment.getId());
        order.setOrderPrice(serviceItem.getServicePrice());
        order.setServiceItemId(serviceItem.getId());
        order.setServiceItemId(serviceItem.getId());
        order.setBarberId(barberId);
        order.setClientId(clientId);
        orderRepository.save(order);
        appointment.setOrderId(order.getId());
        appointmentRepository.save(appointment);

    }
    public Appointment checkBarberAvailability(Integer barberId, LocalDateTime date, LocalDateTime finishTime) {
        return appointmentRepository.checkBarberAvailability(barberId, date, finishTime);
    }

    public List<Appointment> getBarberAppointments(Integer barberId) {
        return appointmentRepository.findBarberAppointments(barberId);
    }

    public List<Appointment> getClientAppointments(Integer clientId) {
        return appointmentRepository.findClientAppointments(clientId);
    }

    public Appointment checkClientAvailability(Integer clientId, LocalDateTime date, LocalDateTime finishTime) {
        return appointmentRepository.checkClientAvailability(clientId, date, finishTime);
    }

    //Change the barber of an appointment.
    public String changeBarber(Integer appointmentId, Integer barberId){
        //Get the appointment object and check if it's in the database.
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);
        if(appointment == null) return "Appointment not found.";

        //Get the barber object and check if it's in the database.
        Barber barber = barberRepository.findBarberById(barberId);
        if(barber == null) return "Barber not found.";

        //Get the order object.
        Order order = orderRepository.findOrderById(appointment.getOrderId());

        //Check if it's a canceled order.
        if(order.getCanceled()) return "Canceled order can not be modified.";

        //Check if it's a completed order.
        if(order.getOrderDone()) return "Completed orders can not be modified.";

        //Check if the appointment has the same barber.
        if(barberId.equals(appointment.getBarberId())) return "Appointment already has this barber.";

        //Check if the barber available.
        if (checkBarberAvailability(barberId, appointment.getAppointmentDate(), appointment.getAppointmentFinishTime()) != null) return "Barber not available.";

        //Change the barber.
        appointment.setBarberId(barberId);
        appointmentRepository.save(appointment);
        return "Barber changed successfully.";
    }


}
