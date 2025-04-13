package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.ServiceItem;
import com.example.barbershopmanagementsystem.Repository.ServiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;

    public List<ServiceItem> getAllServiceItems() {
        return serviceItemRepository.findAll();
    }

    public String addServiceItem(ServiceItem serviceItem) {
        if(serviceItemRepository.findAllByServiceName(serviceItem.getServiceName()) != null) return "Service already exists.";
        serviceItemRepository.save(serviceItem);
        return "Service item added successfully.";
    }

    public String updateServiceItem(Integer serviceItemId, ServiceItem serviceItem) {
        ServiceItem tempServiceItem = serviceItemRepository.findServiceItemById(serviceItemId);
        if (tempServiceItem == null) return "Service item not found.";

        tempServiceItem.setServiceName(serviceItem.getServiceName());
        tempServiceItem.setServicePrice(serviceItem.getServicePrice());
        serviceItemRepository.save(tempServiceItem);
        return "Service item updated successfully.";
    }

    public String deleteServiceItem(Integer serviceItemId) {
        ServiceItem tempServiceItem = serviceItemRepository.findServiceItemById(serviceItemId);
        if (tempServiceItem == null) return "Service item not found.";

        serviceItemRepository.delete(tempServiceItem);
        return "Service item deleted successfully.";
    }
}
