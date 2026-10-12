package com.example.vehicleservice.controller;

import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService requestService;

    public ServiceRequestController(ServiceRequestService requestService) {
        this.requestService = requestService;
    }

    // GET /api/requests                  -> all requests
    // GET /api/requests?customerId=3     -> requests of customer 3
    // GET /api/requests?mechanicId=2     -> jobs of mechanic 2
    @GetMapping
    public List<ServiceRequest> getRequests(@RequestParam(required = false) Long customerId,
                                            @RequestParam(required = false) Long mechanicId) {
        return requestService.getRequests(customerId, mechanicId);
    }

    // GET /api/requests/5
    @GetMapping("/{id}")
    public ServiceRequest getRequest(@PathVariable Long id) {
        return requestService.getRequest(id);
    }

    // POST /api/requests
    @PostMapping
    public ServiceRequest createRequest(@Valid @RequestBody ServiceRequest request) {
        return requestService.createRequest(request);
    }

    // PUT /api/requests/5/assign/2   -> give request 5 to mechanic 2
    @PutMapping("/{id}/assign/{mechanicId}")
    public ServiceRequest assignMechanic(@PathVariable Long id, @PathVariable Long mechanicId) {
        return requestService.assignMechanic(id, mechanicId);
    }

    // PUT /api/requests/5/repair   -> save diagnosis and costs
    @PutMapping("/{id}/repair")
    public ServiceRequest saveRepair(@PathVariable Long id, @RequestBody ServiceRequest data) {
        return requestService.saveRepair(id, data);
    }

    // PUT /api/requests/5/status/APPROVED   -> move to a new step
    @PutMapping("/{id}/status/{newStatus}")
    public ServiceRequest changeStatus(@PathVariable Long id, @PathVariable String newStatus) {
        return requestService.changeStatus(id, newStatus);
    }

    // PUT /api/requests/5/pay   -> mark the invoice as paid
    @PutMapping("/{id}/pay")
    public ServiceRequest markAsPaid(@PathVariable Long id) {
        return requestService.markAsPaid(id);
    }
}
