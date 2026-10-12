package com.example.vehicleservice.controller;

import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // GET /api/vehicles             -> all vehicles
    // GET /api/vehicles?ownerId=3   -> vehicles of customer 3
    @GetMapping
    public List<Vehicle> getVehicles(@RequestParam(required = false) Long ownerId) {
        return vehicleService.getVehicles(ownerId);
    }

    // POST /api/vehicles
    @PostMapping
    public Vehicle addVehicle(@Valid @RequestBody Vehicle vehicle) {
        return vehicleService.addVehicle(vehicle);
    }

    // PUT /api/vehicles/5
    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    // DELETE /api/vehicles/5
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
