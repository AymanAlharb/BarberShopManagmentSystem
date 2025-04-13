package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Integer> {
    ServiceItem findServiceItemById(Integer serviceItemId);

    ServiceItem findAllByServiceName(String ServiceName);
}
