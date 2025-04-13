package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.Client;
import com.example.barbershopmanagementsystem.Model.Order;
import com.example.barbershopmanagementsystem.Repository.BarberRepository;
import com.example.barbershopmanagementsystem.Repository.ClientRepository;
import com.example.barbershopmanagementsystem.Repository.OrderRepository;
import com.example.barbershopmanagementsystem.Repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;

    public ClientService(ClientRepository clientRepository, BarberRepository barberRepository, ShopRepository shopRepository, OrderRepository orderRepository) {
        this.clientRepository = clientRepository;
        this.shopRepository = shopRepository;
        this.orderRepository = orderRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public String addClient(Client client) {
        clientRepository.save(client);
        return "Client added successfully.";
    }

    public String updateClient(Integer clientId, Client client) {
        //Get the client object and check if it's in the database.
        Client tempClient = clientRepository.findClientById(clientId);
        if (tempClient == null) return "Client not found.";

        //Update the client.
        tempClient.setAge(client.getAge());
        tempClient.setPhoneNumber(client.getPhoneNumber());
        tempClient.setFirstName(client.getFirstName());
        tempClient.setLastName(client.getLastName());

        //Save the client in the database.
        clientRepository.save(tempClient);
        return "Client updated successfully.";
    }

    public String deleteClient(Integer clientId) {
        //Get the client object and check if it's in the database.
        Client tempClient = clientRepository.findClientById(clientId);
        if (tempClient == null) return "Client not found.";

        //Delete the client.
        clientRepository.delete(tempClient);
        return "Client deleted successfully.";
    }

    //This method check client eligibility for the over by comparing the client points and the number of needed points for the over.
    public String checkIfClientEligibleForOver(Integer clientId) {
        //Get the client object and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client if not found";

        //Get the number of points needed from the shop table and check if the client have enough points for the over.
        int numberOfPointsNeeded = shopRepository.findNumberOfPointsNeededForOver();
        if (client.getPoints() < numberOfPointsNeeded)
            return client.getFirstName() + " " + client.getLastName() + " has " + client.getPoints() + " and need " + (numberOfPointsNeeded - client.getPoints()) + " points";

        return client.getFirstName() + " " + client.getLastName() + " has " + client.getPoints() + " and can redeem the over.";
    }

    //The over is for loyal client who has completed orders more than shop.numberOfPointsNeededForOver and can be redeemed using the below method,
    //where the client will have to pay nothing for a not completed order.
    //Using this method clients can redeem the over using their points. The method checks for the clients points and compare it to the number of points needed,
    //then change the price of the order.
    public String redeemOver(Integer clientId, Integer orderId) {
        //Get the client object and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found";

        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Check if the order is canceled.
        if (order.getCanceled()) return "This order is canceled.";

        //Check if the order is completed.
        if (order.getOrderDone()) return "Over only eligible for not finished orders.";

        //Get the number of points needed from the shop table and check if the client have enough points for the over.
        int numberOfPointsNeeded = shopRepository.findNumberOfPointsNeededForOver();
        if (client.getPoints() < numberOfPointsNeeded)
            return client.getFirstName() + " " + client.getLastName() + " has " + client.getPoints() + " points and need " + (numberOfPointsNeeded - client.getPoints()) + " more points";

        //The client has enough points to redeem the over. Change the price of the order and subtract the needed points for the over from the client points.
        order.setOrderPrice(0.0);
        orderRepository.save(order);
        client.setPoints(client.getPoints() - numberOfPointsNeeded);
        clientRepository.save(client);
        return "Over redeemed successfully.";
    }
}
