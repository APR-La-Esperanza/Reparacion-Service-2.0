package com.apr.Reparacion_Service.controller;

import com.apr.Reparacion_Service.dto.ReparacionDTO;
import com.apr.Reparacion_Service.dto.ReparacionResponseDTO;
import com.apr.Reparacion_Service.model.EstadoReparacion;
import com.apr.Reparacion_Service.service.ReparacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reparaciones")
public class ReparacionController {

    private final ReparacionService service;

    public ReparacionController(ReparacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReparacionResponseDTO>> listarTodas(
            @RequestParam(required = false) Long incidenciaId,
            @RequestParam(required = false) EstadoReparacion estado) {
        if (incidenciaId != null) {
            return ResponseEntity.ok(service.buscarPorIncidenciaId(incidenciaId));
        }
        if (estado != null) {
            return ResponseEntity.ok(service.buscarPorEstado(estado));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReparacionResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReparacionResponseDTO> guardar(@Valid @RequestBody ReparacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReparacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReparacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
