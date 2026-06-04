package com.apr.Reparacion_Service.dto;

import com.apr.Reparacion_Service.model.EstadoReparacion;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReparacionResponseDTO {

    private Long id;
    private Long incidenciaId;
    private String descripcionTrabajo;
    private BigDecimal costoEstimado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoReparacion estado;

    public ReparacionResponseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
