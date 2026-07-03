package com.apr.Reparacion_Service.controller;

import com.apr.Reparacion_Service.dto.ReparacionDTO;
import com.apr.Reparacion_Service.dto.ReparacionResponseDTO;
import com.apr.Reparacion_Service.model.EstadoReparacion;
import com.apr.Reparacion_Service.service.ReparacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reparaciones")
@Tag(name = "Reparaciones", description = "Endpoints para la gestión, registro y costos de reparaciones de la red hídrica.")
@SecurityRequirement(name = "bearerAuth")
public class ReparacionController {

    private final ReparacionService service;

    public ReparacionController(ReparacionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar reparaciones", description = "Retorna una lista de reparaciones. Permite filtrar por incidenciaId o estado (PENDIENTE, EN_PROCESO, COMPLETADA).")
    @ApiResponse(responseCode = "200", description = "Lista de reparaciones obtenida exitosamente.")
    public ResponseEntity<List<ReparacionResponseDTO>> listarTodas(
            @Parameter(description = "ID de la incidencia para filtrar") @RequestParam(required = false) Long incidenciaId,
            @Parameter(description = "Estado de la reparación para filtrar") @RequestParam(required = false) EstadoReparacion estado) {
        if (incidenciaId != null) {
            return ResponseEntity.ok(service.buscarPorIncidenciaId(incidenciaId));
        }
        if (estado != null) {
            return ResponseEntity.ok(service.buscarPorEstado(estado));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reparación por ID", description = "Obtiene los detalles de un registro de reparación específico.")
    @ApiResponse(responseCode = "200", description = "Reparación encontrada y devuelta.")
    @ApiResponse(responseCode = "404", description = "La reparación indicada no existe.")
    public ResponseEntity<ReparacionResponseDTO> buscarPorId(
            @Parameter(description = "ID único de la reparación", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar reparación", description = "Guarda una nueva reparación técnica. Valida que incidenciaId exista en Incidencia-Service, y actualiza el estado de la incidencia si es oportuno.")
    @ApiResponse(responseCode = "201", description = "Reparación registrada con éxito.")
    @ApiResponse(responseCode = "400", description = "Datos provistos inválidos o incidencia no encontrada.")
    public ResponseEntity<ReparacionResponseDTO> guardar(
            @RequestBody(description = "Datos para la nueva reparación", required = true,
                         content = @Content(schema = @Schema(implementation = ReparacionDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"incidenciaId\": 1,\n  \"descripcionTrabajo\": \"Reemplazo de tramo de tubería de PVC de 2 pulgadas.\",\n  \"costoEstimado\": 45000.0,\n  \"estado\": \"PENDIENTE\",\n  \"fechaInicio\": \"2025-06-21\",\n  \"operadorId\": 2\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody ReparacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reparación", description = "Modifica los datos de una reparación existente (por ejemplo, cambiar su estado a COMPLETADA, lo que marcará la incidencia asociada como RESUELTA).")
    @ApiResponse(responseCode = "200", description = "Reparación actualizada exitosamente.")
    @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos.")
    @ApiResponse(responseCode = "404", description = "El registro de reparación no existe.")
    public ResponseEntity<ReparacionResponseDTO> actualizar(
            @Parameter(description = "ID de la reparación a actualizar", required = true) @PathVariable Long id,
            @RequestBody(description = "Nuevos datos de la reparación", required = true,
                         content = @Content(schema = @Schema(implementation = ReparacionDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"incidenciaId\": 1,\n  \"descripcionTrabajo\": \"Reemplazo completado y presurizado.\",\n  \"costoEstimado\": 45000.0,\n  \"estado\": \"COMPLETADA\",\n  \"fechaInicio\": \"2025-06-21\",\n  \"fechaFin\": \"2025-06-22\",\n  \"operadorId\": 2\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody ReparacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reparación", description = "Elimina permanentemente el registro de reparación técnica.")
    @ApiResponse(responseCode = "204", description = "Reparación eliminada con éxito.")
    @ApiResponse(responseCode = "404", description = "La reparación no existe.")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reparación a eliminar", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
