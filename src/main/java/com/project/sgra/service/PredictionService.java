package com.project.sgra.service;

import com.project.sgra.dto.BusData;
import com.project.sgra.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Service
public class PredictionService {

    @Autowired
    private WekaModelService wekaService;

    @Autowired
    private PredictionRepository repository;

    public String predictAndSave(BusData data) throws Exception {
        Instances structure = wekaService.getDatasetStructure();
        Instance instance = new DenseInstance(structure.numAttributes());
        instance.setDataset(structure);

        // 1️⃣ Vehicle_Model fijo (solo "Bus")
        instance.setValue(0, "Bus");

        // 2️⃣ Numeric
        instance.setValue(1, data.getMileage());
        instance.setValue(3, data.getReportedIssues());
        instance.setValue(4, data.getVehicleAge());
        instance.setValue(7, data.getEngineSize());
        instance.setValue(8, data.getLastServiceDate());
        instance.setValue(9, data.getServiceHistory());
        instance.setValue(10, data.getAccidentHistory());
        instance.setValue(11, data.getFuelEfficiency());

        // 3️⃣ Nominal
        setNominalValue(instance, 2, data.getMaintenanceHistory());
        setNominalValue(instance, 5, data.getFuelType());
        setNominalValue(instance, 6, data.getTransmissionType());
        setNominalValue(instance, 12, data.getTireCondition());
        setNominalValue(instance, 13, data.getBrakeCondition());
        setNominalValue(instance, 14, data.getBatteryStatus());

        // --- Predicción ---
        String prediction = wekaService.predict(instance);
        data.setPredictionResult(prediction);
        repository.save(data);

        return prediction;
    }

    private void setNominalValue(Instance instance, int attrIndex, String value) {
        if (value == null || value.trim().isEmpty()) {
            System.out.println("⚠️ Valor nulo o vacío para atributo: " + instance.dataset().attribute(attrIndex).name());
            return;
        }
        int index = instance.dataset().attribute(attrIndex).indexOfValue(value.trim());
        if (index == -1) {
            System.out.println("⚠️ Valor '" + value + "' no válido para atributo: " +
                    instance.dataset().attribute(attrIndex).name());
            return;
        }
        instance.setValue(attrIndex, value.trim());
    }
}