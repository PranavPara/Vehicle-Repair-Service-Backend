package com.example.vehicleservice.service;

import com.example.vehicleservice.entity.User;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.exception.MyException;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.repository.UserRepository;
import com.example.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ServiceRequestRepository requestRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository,
                          ServiceRequestRepository requestRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    // If ownerId is given we return only that customer's vehicles, otherwise all vehicles
    public List<Vehicle> getVehicles(Long ownerId) {
        if (ownerId != null) {
            return vehicleRepository.findByOwnerId(ownerId);
        }
        return vehicleRepository.findAll();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicle.getOwner() == null || vehicle.getOwner().getId() == null) {
            throw new MyException("The vehicle needs an owner");
        }
        User owner = userRepository.findById(vehicle.getOwner().getId())
                .orElseThrow(() -> new MyException("Owner not found"));
        vehicle.setOwner(owner);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle newData) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new MyException("Vehicle not found"));

        vehicle.setRegistrationNumber(newData.getRegistrationNumber());
        vehicle.setBrand(newData.getBrand());
        vehicle.setModel(newData.getModel());
        vehicle.setVehicleType(newData.getVehicleType());
        vehicle.setManufacturingYear(newData.getManufacturingYear());
        vehicle.setMileage(newData.getMileage());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        if (requestRepository.existsByVehicleId(id)) {
            throw new MyException("This vehicle has service requests, so it cannot be deleted");
        }
        vehicleRepository.deleteById(id);
    }
}
