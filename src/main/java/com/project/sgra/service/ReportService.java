package com.project.sgra.service;

import com.project.sgra.model.Report;
import com.project.sgra.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public Report saveReport(Report report) {
        return repository.save(report);
    }

    public void clearReports() {
        repository.deleteAll();
    }
}
