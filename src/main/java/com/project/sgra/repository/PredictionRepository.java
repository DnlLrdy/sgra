package com.project.sgra.repository;
import com.project.sgra.dto.BusData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PredictionRepository extends MongoRepository<BusData, String> {
}
