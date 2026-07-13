package com.apr.Reparacion_Service.service;

import com.apr.Reparacion_Service.dto.ReparacionDTO;
import com.apr.Reparacion_Service.dto.ReparacionResponseDTO;
import com.apr.Reparacion_Service.exception.ResourceNotFoundException;
import com.apr.Reparacion_Service.mapper.ReparacionMapper;
import com.apr.Reparacion_Service.model.EstadoReparacion;
import com.apr.Reparacion_Service.model.Reparacion;
import com.apr.Reparacion_Service.repository.ReparacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReparacionService {

    private final ReparacionRepository repository;
    private final WebClient webClient;

    public ReparacionService(ReparacionRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public List<ReparacionResponseDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(ReparacionMapper::toResponseDTO)
                .toList();
    }

    public ReparacionResponseDTO buscarPorId(Long id) {
        Reparacion reparacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparación no encontrada con id: " + id));
        return ReparacionMapper.toResponseDTO(reparacion);
    }

    public List<ReparacionResponseDTO> buscarPorIncidenciaId(Long incidenciaId) {
        return repository.findByIncidenciaId(incidenciaId)
                .stream()
                .map(ReparacionMapper::toResponseDTO)
                .toList();
    }

    public List<ReparacionResponseDTO> buscarPorEstado(EstadoReparacion estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(ReparacionMapper::toResponseDTO)
                .toList();
    }

    public ReparacionResponseDTO guardar(ReparacionDTO dto) {
        // 1. Validar Incidencia
        Map<String, Object> incidencia = obtenerIncidenciaDeIncidenciaService(dto.getIncidenciaId());

        // 2. Registrar Reparación
        Reparacion reparacion = ReparacionMapper.toEntity(dto);
        Reparacion guardada = repository.save(reparacion);

        // 3. Si se crea como FINALIZADA, actualizar la incidencia
        if (dto.getEstado() == EstadoReparacion.FINALIZADA) {
            actualizarEstadoIncidenciaAResuelta(dto.getIncidenciaId(), incidencia);
        }

        return ReparacionMapper.toResponseDTO(guardada);
    }

    public ReparacionResponseDTO actualizar(Long id, ReparacionDTO dto) {
        Reparacion reparacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparación no encontrada con id: " + id));

        Map<String, Object> incidencia = null;
        if (!reparacion.getIncidenciaId().equals(dto.getIncidenciaId())) {
            incidencia = obtenerIncidenciaDeIncidenciaService(dto.getIncidenciaId());
        }

        reparacion.setIncidenciaId(dto.getIncidenciaId());
        reparacion.setDescripcionTrabajo(dto.getDescripcionTrabajo());
        reparacion.setCostoEstimado(dto.getCostoEstimado());
        if (dto.getFechaInicio() != null) reparacion.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFin() != null) reparacion.setFechaFin(dto.getFechaFin());

        EstadoReparacion anteriorEstado = reparacion.getEstado();
        if (dto.getEstado() != null) {
            reparacion.setEstado(dto.getEstado());
            if (dto.getEstado() == EstadoReparacion.FINALIZADA && anteriorEstado != EstadoReparacion.FINALIZADA) {
                if (reparacion.getFechaFin() == null) {
                    reparacion.setFechaFin(LocalDate.now());
                }
                if (incidencia == null) {
                    incidencia = obtenerIncidenciaDeIncidenciaService(dto.getIncidenciaId());
                }
                actualizarEstadoIncidenciaAResuelta(dto.getIncidenciaId(), incidencia);
            }
        }

        Reparacion actualizada = repository.save(reparacion);
        return ReparacionMapper.toResponseDTO(actualizada);
    }

    public Map<String, Object> notificarReparacion(Long id) {
        Reparacion reparacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparación no encontrada con id: " + id));

        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "NOTIFICACION_REPARACION");
        notificacion.put("reparacionId", reparacion.getId());
        notificacion.put("incidenciaId", reparacion.getIncidenciaId());
        notificacion.put("estado", reparacion.getEstado());
        notificacion.put("mensaje", String.format(
                "Estimado socio: La reparación asociada a la incidencia %d ha finalizado con éxito (estado: %s). Servicio restablecido.",
                reparacion.getIncidenciaId(),
                reparacion.getEstado()));
        notificacion.put("simulado", true);
        return notificacion;
    }

    public void eliminar(Long id) {
        Reparacion reparacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparación no encontrada con id: " + id));
        repository.delete(reparacion);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> obtenerIncidenciaDeIncidenciaService(Long incidenciaId) {
        try {
            Map<String, Object> incidencia = webClient.get()
                    .uri("/incidencias/" + incidenciaId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (incidencia == null) {
                throw new IllegalArgumentException("La Incidencia con ID " + incidenciaId + " no existe.");
            }
            return incidencia;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al obtener la incidencia: " + e.getMessage());
        }
    }

    private void actualizarEstadoIncidenciaAResuelta(Long incidenciaId, Map<String, Object> incidenciaOriginal) {
        try {
            Map<String, Object> requestBody = new HashMap<>(incidenciaOriginal);
            requestBody.put("estado", "RESUELTA");

            webClient.put()
                    .uri("/incidencias/" + incidenciaId)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo actualizar el estado de la incidencia a RESUELTA: " + e.getMessage());
        }
    }
}
