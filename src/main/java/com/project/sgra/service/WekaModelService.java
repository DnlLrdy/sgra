package com.project.sgra.service;



import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;

@Service
public class WekaModelService {

    private Classifier model;
    private Instances datasetStructure;

    public WekaModelService() {
        // Constructor vacío: inicialización en @PostConstruct
    }

    @PostConstruct
    public void init() {
        // Cambia la ruta aquí según dónde esté el archivo:
        // - Si lo dejaste en src/main/resources/static -> "static/maintenance.model"
        // - Si lo pusiste en src/main/resources -> "maintenance.model"
        String resourcePath = "static/maintenance.model";

        try (InputStream modelStream = new ClassPathResource(resourcePath).getInputStream()) {
            if (modelStream == null) {
                throw new IllegalStateException("No se encontró el archivo '" + resourcePath + "' en el classpath.");
            }
            this.model = (Classifier) SerializationHelper.read(modelStream);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando el modelo Weka desde '" + resourcePath + "': " + e.getMessage(), e);
        }

        // Definir estructura del dataset (igual que antes)
        ArrayList<Attribute> attributes = new ArrayList<>();

        ArrayList<String> models = new ArrayList<>();
        models.add("Bus");
        attributes.add(new Attribute("Vehicle_Model", models));

        attributes.add(new Attribute("Mileage"));

        ArrayList<String> maintHist = new ArrayList<>();
        maintHist.add("Poor");
        maintHist.add("Average");
        maintHist.add("Good");
        attributes.add(new Attribute("Maintenance_History", maintHist));

        attributes.add(new Attribute("Reported_Issues"));
        attributes.add(new Attribute("Vehicle_Age"));

        ArrayList<String> fuelType = new ArrayList<>();
        fuelType.add("Electric");
        fuelType.add("Petrol");
        fuelType.add("Diesel");
        attributes.add(new Attribute("Fuel_Type", fuelType));

        ArrayList<String> trans = new ArrayList<>();
        trans.add("Automatic");
        trans.add("Manual");
        attributes.add(new Attribute("Transmission_Type", trans));

        attributes.add(new Attribute("Engine_Size"));
        attributes.add(new Attribute("Last_Service_Date"));
        attributes.add(new Attribute("Service_History"));
        attributes.add(new Attribute("Accident_History"));
        attributes.add(new Attribute("Fuel_Efficiency"));

        ArrayList<String> tire = new ArrayList<>();
        tire.add("New");
        tire.add("Good");
        tire.add("Worn Out");
        attributes.add(new Attribute("Tire_Condition", tire));

        ArrayList<String> brake = new ArrayList<>();
        brake.add("Good");
        brake.add("Worn Out");
        brake.add("New");
        attributes.add(new Attribute("Brake_Condition", brake));

        ArrayList<String> battery = new ArrayList<>();
        battery.add("Weak");
        battery.add("New");
        battery.add("Good");
        attributes.add(new Attribute("Battery_Status", battery));

        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("Yes");
        classValues.add("No");
        attributes.add(new Attribute("Need_Maintenance", classValues));

        datasetStructure = new Instances("PredictionData", attributes, 0);
        datasetStructure.setClassIndex(datasetStructure.numAttributes() - 1);
    }

    public String predict(weka.core.Instance instance) throws Exception {
        if (model == null) {
            throw new IllegalStateException("Modelo Weka no inicializado.");
        }
        double predIndex = model.classifyInstance(instance);
        return datasetStructure.classAttribute().value((int) predIndex);
    }

    public Instances getDatasetStructure() {
        return datasetStructure;
    }
}
