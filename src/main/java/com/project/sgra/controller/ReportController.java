package com.project.sgra.controller;

import com.project.sgra.model.Report;
import com.project.sgra.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    @Autowired
    private ReportRepository repo;

    @GetMapping
    public List<Report> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Report save(@RequestBody Report report) {
        return repo.save(report);
    }

    @DeleteMapping("/clear")
    public void clearAll() {
        repo.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable String id) {
        repo.deleteById(id);
    }

    // stats opcional
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        List<Report> all = repo.findAll();
        long total = all.size();
        long need = all.stream().filter(r -> "Yes".equalsIgnoreCase(r.getPrediction())).count();
        long ok = total - need;
        return Map.of("total", total, "needMaintenance", need, "ok", ok);
    }
}