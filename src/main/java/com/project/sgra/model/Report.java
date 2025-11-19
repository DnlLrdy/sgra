package com.project.sgra.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String timestamp; // fecha ISO
    private double mileage;
    private String maintenanceHistory;
    private String prediction;
    private String accuracy;
    private int vehicleAge;
    private String fuelType;
    private String transmissionType;
    private int reportedIssues;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }

    public String getMaintenanceHistory() { return maintenanceHistory; }
    public void setMaintenanceHistory(String maintenanceHistory) { this.maintenanceHistory = maintenanceHistory; }

    public String getPrediction() { return prediction; }
    public void setPrediction(String prediction) { this.prediction = prediction; }

    public String getAccuracy() { return accuracy; }
    public void setAccuracy(String accuracy) { this.accuracy = accuracy; }

    public int getVehicleAge() { return vehicleAge; }
    public void setVehicleAge(int vehicleAge) { this.vehicleAge = vehicleAge; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmissionType() { return transmissionType; }
    public void setTransmissionType(String transmissionType) { this.transmissionType = transmissionType; }

    public int getReportedIssues() { return reportedIssues; }
    public void setReportedIssues(int reportedIssues) { this.reportedIssues = reportedIssues; }
}