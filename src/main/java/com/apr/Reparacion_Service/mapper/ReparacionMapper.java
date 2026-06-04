package com.apr.Reparacion_Service.mapper;

import com.apr.Reparacion_Service.dto.ReparacionDTO;
import com.apr.Reparacion_Service.dto.ReparacionResponseDTO;
import com.apr.Reparacion_Service.model.Reparacion;

public class ReparacionMapper {

    public static Reparacion toEntity(ReparacionDTO dto) {
        if (dto == null) return null;
        Reparacion reparacion = new Reparacion();
        reparacion.setIncidenciaId(dto.getIncidenciaId());
        reparacion.setDescripcionTrabajo(dto.getDescripcionTrabajo());
        reparacion.setCostoEstimado(dto.getCostoEstimado());
        if (dto.getFechaInicio() != null) reparacion.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFin() != null) reparacion.setFechaFin(dto.getFechaFin());
        if (dto.getEstado() != null) reparacion.setEstado(dto.getEstado());
        return reparacion;
    }

    public static ReparacionResponseDTO toResponseDTO(Reparacion reparacion) {
        if (reparacion == null) return null;
        ReparacionResponseDTO dto = new ReparacionResponseDTO();
        dto.setId(reparacion.getId());
        dto.setIncidenciaId(reparacion.getIncidenciaId());
        dto.setDescripcionTrabajo(reparacion.getDescripcionTrabajo());
        dto.setCostoEstimado(reparacion.getCostoEstimado());
        dto.setFechaInicio(reparacion.getFechaInicio());
        dto.setFechaFin(reparacion.getFechaFin());
        dto.setEstado(reparacion.getEstado());
        return dto;
    }
}
