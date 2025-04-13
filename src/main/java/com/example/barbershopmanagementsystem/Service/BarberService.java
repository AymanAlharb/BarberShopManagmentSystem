package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.Appointment;
import com.example.barbershopmanagementsystem.Model.Barber;
import com.example.barbershopmanagementsystem.Model.Order;
import com.example.barbershopmanagementsystem.Repository.AppointmentRepository;
import com.example.barbershopmanagementsystem.Repository.BarberRepository;
import com.example.barbershopmanagementsystem.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberService {
    private final BarberRepository barberRepository;
    private final OrderRepository orderRepository;
    private final HelperService helperService;
    private final ReviewService reviewService;
    private final AppointmentRepository appointmentRepository;

    public BarberService(BarberRepository barberRepository, OrderRepository orderRepository, HelperService helperService, ReviewService reviewService, AppointmentRepository appointmentRepository) {
        this.barberRepository = barberRepository;
        this.orderRepository = orderRepository;
        this.helperService = helperService;
        this.reviewService = reviewService;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }

    public String addBarber(Barber barber) {
        barberRepository.save(barber);
        return "Barber added successfully.";
    }

    public String updateBarber(Integer barberId, Barber barber) {
        Barber tempBarber = barberRepository.findBarberById(barberId);
        if (tempBarber == null) return "Barber not found.";
        tempBarber.setAge(barber.getAge());
        tempBarber.setPhoneNumber(barber.getPhoneNumber());
        tempBarber.setFirstName(barber.getFirstName());
        tempBarber.setLastName(barber.getLastName());
        barberRepository.save(tempBarber);
        return "Barber updated successfully.";
    }

    public String deleteBarber(Integer barberId) {
        Barber tempBarber = barberRepository.findBarberById(barberId);
        if (tempBarber == null) return "Barber not found.";
        barberRepository.delete(tempBarber);
        return "Barber deleted successfully.";
    }

    //This method is to get a barber total revenue.
    public String getBarberTotalRevenue(Integer barberId) {
        List<Order> orderList = orderRepository.findCompletedOrdersByBarberId(barberId);
        return "The barber total revenue is " + helperService.calculateRevenue(orderList) + ".";
    }


    //This method is to a barber status.
    public String getBarberStats(Integer barberId) {
        //Get the barber object and check if it's in the database.
        Barber barber = barberRepository.findBarberById(barberId);
        if (barber == null) return "Barber not found.";

        //Get the barber appointments.
        List<Appointment> barberAppointments = appointmentRepository.findBarberAppointments(barberId);

        //Calculate the barber total number of canceled appointments.
        int numberOfCanceledAppointments = 0;
        for (Appointment appointment : barberAppointments)
            if (orderRepository.findOrderById(appointment.getOrderId()).getCanceled()) numberOfCanceledAppointments++;

        //Get the barber total number of appointments.
        int numberOfTotalAppointments = barberAppointments.size();

        //Get the barber average rating.
        String barberAvgRating = reviewService.getBarberAvgRating(barberId);
        return barberAvgRating + " And has " + numberOfTotalAppointments + " appointments. And " + numberOfCanceledAppointments + " canceled appointments. And " + getBarberTotalRevenue(barberId);
    }
}
