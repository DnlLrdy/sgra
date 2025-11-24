package com.project.sgra.controller;

import com.project.sgra.dto.BusData;
import com.project.sgra.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/predict")
    public ResponseEntity<Map<String, String>> predict(@RequestBody BusData data) throws Exception {
        String prediction = predictionService.predictAndSave(data);
        Map<String, String> response = new HashMap<>();
        response.put("prediction", prediction);
        return ResponseEntity.ok(response);
    }
}