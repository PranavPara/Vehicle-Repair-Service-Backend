package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    // all requests of one customer
    List<ServiceRequest> findByVehicleOwnerId(Long customerId);

    // all requests given to one mechanic
    List<ServiceRequest> findByMechanicId(Long mechanicId);

    // is there any request for this vehicle?
    boolean existsByVehicleId(Long vehicleId);
}
