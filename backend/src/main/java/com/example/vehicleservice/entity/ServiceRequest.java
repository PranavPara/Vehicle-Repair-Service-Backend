package com.example.vehicleservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

// Table: service_requests
// One row = one repair job for one vehicle.
// The "status" moves like this:
// REPORTED -> DIAGNOSIS -> ESTIMATE -> APPROVED -> REPAIR -> COMPLETED -> DELIVERED
// (or CANCELLED)
@Entity
@Table(name = "service_requests")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vehicle vehicle;

    @NotBlank(message = "Please write the problem")
    private String description;

    private String status;

    // The mechanic who does the work (chosen by the admin)
    @ManyToOne
    private User mechanic;

    // Filled in by the mechanic
    private String diagnosis;
    private double partsCost;
    private double labourCost;

    // partsCost + labourCost
    private double totalCost;

    // PENDING or PAID
    private String paymentStatus;

    private LocalDate requestDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getMechanic() { return mechanic; }
    public void setMechanic(User mechanic) { this.mechanic = mechanic; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public double getPartsCost() { return partsCost; }
    public void setPartsCost(double partsCost) { this.partsCost = partsCost; }

    public double getLabourCost() { return labourCost; }
    public void setLabourCost(double labourCost) { this.labourCost = labourCost; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
}
