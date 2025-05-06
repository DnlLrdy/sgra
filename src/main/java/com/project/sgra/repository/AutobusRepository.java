package com.project.sgra.repository;

import com.project.sgra.model.Autobus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutobusRepository extends MongoRepository<Autobus, String> {
}
