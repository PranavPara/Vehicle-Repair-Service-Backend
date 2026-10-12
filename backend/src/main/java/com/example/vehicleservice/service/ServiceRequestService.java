package com.example.vehicleservice.service;

import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.entity.User;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.exception.MyException;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.repository.UserRepository;
import com.example.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// This is the most important class. It controls the repair steps.
@Service
public class ServiceRequestService {

    // The steps of a repair, in order
    private static final List<String> STEPS = List.of(
            "REPORTED", "DIAGNOSIS", "ESTIMATE", "APPROVED", "REPAIR", "COMPLETED", "DELIVERED");

    private final ServiceRequestRepository requestRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public ServiceRequestService(ServiceRequestRepository requestRepository,
                                 VehicleRepository vehicleRepository,
                                 UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    // customerId  -> the requests of one customer
    // mechanicId  -> the jobs of one mechanic
    // nothing     -> all requests (for the admin)
    public List<ServiceRequest> getRequests(Long customerId, Long mechanicId) {
        if (customerId != null) {
            return requestRepository.findByVehicleOwnerId(customerId);
        }
        if (mechanicId != null) {
            return requestRepository.findByMechanicId(mechanicId);
        }
        return requestRepository.findAll();
    }

    public ServiceRequest getRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new MyException("Service request not found"));
    }

    // Customer makes a new request
    public ServiceRequest createRequest(ServiceRequest request) {
        if (request.getVehicle() == null || request.getVehicle().getId() == null) {
            throw new MyException("Please choose a vehicle");
        }
        Vehicle vehicle = vehicleRepository.findById(request.getVehicle().getId())
                .orElseThrow(() -> new MyException("Vehicle not found"));

        request.setVehicle(vehicle);
        request.setStatus("REPORTED");
        request.setRequestDate(LocalDate.now());
        return requestRepository.save(request);
    }

    // Admin chooses a mechanic. The request moves to DIAGNOSIS.
    public ServiceRequest assignMechanic(Long id, Long mechanicId) {
        ServiceRequest request = getRequest(id);

        if (!request.getStatus().equals("REPORTED") && !request.getStatus().equals("DIAGNOSIS")) {
            throw new MyException("You can only choose a mechanic before the estimate");
        }

        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new MyException("Mechanic not found"));

        request.setMechanic(mechanic);
        request.setStatus("DIAGNOSIS");
        return requestRepository.save(request);
    }

    // Mechanic saves the diagnosis and the costs
    public ServiceRequest saveRepair(Long id, ServiceRequest data) {
        ServiceRequest request = getRequest(id);

        if (!request.getStatus().equals("DIAGNOSIS") && !request.getStatus().equals("REPAIR")) {
            throw new MyException("You can only change repair details during diagnosis or repair");
        }
        if (data.getPartsCost() < 0 || data.getLabourCost() < 0) {
            throw new MyException("Costs cannot be negative");
        }

        // the diagnosis can only be changed in the DIAGNOSIS step
        if (request.getStatus().equals("DIAGNOSIS")) {
            request.setDiagnosis(data.getDiagnosis());
        }
        request.setPartsCost(data.getPartsCost());
        request.setLabourCost(data.getLabourCost());
        return requestRepository.save(request);
    }

    // Move the request to a new step
    public ServiceRequest changeStatus(Long id, String newStatus) {
        ServiceRequest request = getRequest(id);
        int currentStep = STEPS.indexOf(request.getStatus());

        // Cancel is allowed until the repair starts
        if (newStatus.equals("CANCELLED")) {
            if (currentStep == -1 || currentStep > STEPS.indexOf("APPROVED")) {
                throw new MyException("This request cannot be cancelled now");
            }
            request.setStatus("CANCELLED");
            return requestRepository.save(request);
        }

        // Any other change must be exactly the next step
        int newStep = STEPS.indexOf(newStatus);
        if (newStep == -1 || newStep != currentStep + 1) {
            throw new MyException("You cannot move from " + request.getStatus() + " to " + newStatus);
        }
        if (newStatus.equals("DIAGNOSIS")) {
            throw new MyException("Choose a mechanic to start the diagnosis");
        }

        if (newStatus.equals("ESTIMATE")) {
            if (request.getDiagnosis() == null || request.getDiagnosis().trim().isEmpty()) {
                throw new MyException("Write the diagnosis first");
            }
            if (request.getPartsCost() + request.getLabourCost() <= 0) {
                throw new MyException("Enter the parts cost or the labour cost first");
            }
        }

        // The total is worked out when the estimate is made and again when the repair is completed
        if (newStatus.equals("ESTIMATE") || newStatus.equals("COMPLETED")) {
            request.setTotalCost(request.getPartsCost() + request.getLabourCost());
        }
        // The bill is ready when the repair is completed
        if (newStatus.equals("COMPLETED")) {
            request.setPaymentStatus("PENDING");
        }

        request.setStatus(newStatus);
        return requestRepository.save(request);
    }

    // Admin marks the bill as paid
    public ServiceRequest markAsPaid(Long id) {
        ServiceRequest request = getRequest(id);

        if (!request.getStatus().equals("COMPLETED") && !request.getStatus().equals("DELIVERED")) {
            throw new MyException("There is no invoice for this request yet");
        }
        request.setPaymentStatus("PAID");
        return requestRepository.save(request);
    }
}
