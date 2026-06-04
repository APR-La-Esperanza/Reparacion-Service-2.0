package com.apr.Reparacion_Service.dto;

import com.apr.Reparacion_Service.model.EstadoReparacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReparacionDTO {

    @NotNull(message = "El ID de incidencia es obligatorio")
    private Long incidenciaId;

    @NotBlank(message = "La descripción del trabajo es obligatoria")
    private String descripcionTrabajo;

    private BigDecimal costoEstimado;
    private LocalDate javaInicio; // Wait, let's call it fechaInicio matching model
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoReparacion estado;

    public ReparacionDTO() {
    }

    public Long getIncidenciaId() { return incidenciaId; }
    public void setIncidenciaId(Long incidenciaId) { this.incidenciaId = incidenciaId; }
    public String getDescripcionTrabajo() { return descripcionTrabajo; }
    public void setDescripcionTrabajo(String descripcionTrabajo) { this.descripcionTrabajo = descripcionTrabajo; }
    public BigDecimal getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public EstadoReparacion getEstado() { return estado; }
    public void setEstado(EstadoReparacion estado) { this.estado = estado; }
}
