package com.project.sgra.service;

import com.project.sgra.model.Ruta;
import com.project.sgra.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BusquedaRutaService {

    @Autowired
    private RutaRepository rutaRepository;

    public List<Ruta> buscarRutasPorUbicacionYDestino(String ubicacion, String destino) {
        // Validación de parámetros
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        if (destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("El destino no puede estar vacío");
        }
        // Realiza la búsqueda si los parámetros son válidos
        return rutaRepository.findRutasByUbicacionAndDestinoParcial(ubicacion, destino);
    }
    public List<Ruta> buscarRutasPorParadaUnica(String parada) {
        if (parada != null && !parada.isEmpty()) {
            return rutaRepository.findRutasByParadaParcial(parada);
        }
        return Collections.emptyList();
    }
    public Ruta obtenerRutaPorId(String id) {
        return rutaRepository.findById(id).orElse(null);
    }
    }
