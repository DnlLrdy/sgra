package com.project.sgra.controller;

import com.project.sgra.model.Autobus;
import com.project.sgra.model.Parada;
import com.project.sgra.model.Ruta;
import com.project.sgra.repository.AutobusRepository;
import com.project.sgra.repository.RutaRepository;
import com.project.sgra.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RutaRepository rutaRepository;
    private final AutobusRepository autobusRepository;

    public UsuarioController(UsuarioService usuarioService,
                             RutaRepository rutaRepository,
                             AutobusRepository autobusRepository) {
        this.usuarioService = usuarioService;
        this.rutaRepository = rutaRepository;
        this.autobusRepository = autobusRepository;
    }

    // =====================================================
    // ✅ Página principal de búsqueda de rutas
    // =====================================================
    @GetMapping("/buscar")
    public String buscarRutas(@RequestParam(required = false) String ubicacion,
                              @RequestParam(required = false) String destino,
                              Model model) {

        List<Ruta> rutas = new ArrayList<>();
        String errorMessage = null;

        if (ubicacion != null && destino != null &&
                !ubicacion.isBlank() && !destino.isBlank()) {
            rutas = usuarioService.buscarRutasPorUbicacionYDestino(ubicacion, destino);
        } else if ((ubicacion != null && !ubicacion.isBlank()) ||
                (destino != null && !destino.isBlank())) {
            errorMessage = "Debe ingresar ubicación y destino para buscar rutas.";
        }

        model.addAttribute("autobuses", rutas);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("destino", destino);
        model.addAttribute("errorMessage", errorMessage);

        return "usuario/buscar-rutas";
    }

    // =====================================================
    // ✅ Obtener una ruta por ID
    // =====================================================
    @GetMapping("/ruta/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerRutaPorId(@PathVariable String id) {
        return rutaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // ✅ Obtener paradas de una ruta
    // =====================================================
    @GetMapping("/ruta/{id}/paradas")
    @ResponseBody
    public ResponseEntity<List<Parada>> obtenerParadasPorRuta(@PathVariable String id) {
        return rutaRepository.findById(id)
                .map(ruta -> ResponseEntity.ok(Optional.ofNullable(ruta.getParadas())
                        .orElse(Collections.emptyList())))
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // ✅ Obtener autobuses por ruta
    // =====================================================
    @GetMapping("/ruta/{rutaId}/autobuses")
    @ResponseBody
    public ResponseEntity<List<Autobus>> obtenerAutobusesPorRuta(@PathVariable String rutaId) {
        List<Autobus> autobuses = autobusRepository.findByRutaId(rutaId);
        return ResponseEntity.ok(autobuses);
    }

    // =====================================================
    // ✅ Obtener todas las paradas disponibles
    // =====================================================
    @GetMapping("/paradas/todas")
    @ResponseBody
    public ResponseEntity<List<String>> obtenerTodasLasParadas() {
        List<Ruta> rutas = rutaRepository.findAll();
        Set<String> nombresParadas = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for (Ruta ruta : rutas) {
            if (ruta.getParadas() != null) {
                ruta.getParadas().stream()
                        .map(Parada::getNombre)
                        .filter(Objects::nonNull)
                        .forEach(nombresParadas::add);
            }
        }

        return ResponseEntity.ok(new ArrayList<>(nombresParadas));
    }

    // =====================================================
    // ✅ Obtener perfil de usuario autenticado
    // =====================================================
    @GetMapping("/perfil")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerPerfil(Authentication authentication) {
        Map<String, Object> data = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                data.put("nombreUsuario", userDetails.getUsername());
                data.put("rol", userDetails.getAuthorities());
            } else {
                data.put("nombreUsuario", "Cliente");
                data.put("rol", "INVITADO");
            }
        } else {
            data.put("nombreUsuario", "Cliente");
            data.put("rol", "INVITADO");
        }

        data.put("horaServidor", new Date().toString());
        return ResponseEntity.ok(data);
    }

    // =====================================================
    // ✅ Nuevo: Obtener rutas por nombre o coincidencia parcial
    // =====================================================
    @GetMapping("/rutas/buscar")
    @ResponseBody
    public ResponseEntity<List<Ruta>> buscarRutasPorNombre(@RequestParam String termino) {
        if (termino == null || termino.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<Ruta> rutas = rutaRepository.findAll().stream()
                .filter(r -> r.getNombre() != null &&
                        r.getNombre().toLowerCase().contains(termino.toLowerCase()))
                .toList();

        return ResponseEntity.ok(rutas);
    }

    // =====================================================
    // ✅ Nuevo: Endpoint de prueba de conexión (puede servir para AJAX)
    // =====================================================
    @GetMapping("/ping")
    @ResponseBody
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Controlador Usuario activo y funcionando");
        return ResponseEntity.ok(response);
    }
}
