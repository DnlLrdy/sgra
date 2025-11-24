package com.project.sgra.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String timestamp;

    private double mileage;
    private String maintenanceHistory;
    private double reportedIssues;
    private double vehicleAge;
    private String fuelType;
    private String transmissionType;

    private double engineSize;
    private double lastServiceDate;
    private double serviceHistory;
    private double accidentHistory;
    private double fuelEfficiency;

    private String tireCondition;
    private String brakeCondition;
    private String batteryStatus;

    private String prediction;
    private String accuracy;

}
