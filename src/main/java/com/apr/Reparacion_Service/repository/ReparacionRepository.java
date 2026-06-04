package com.apr.Reparacion_Service.repository;

import com.apr.Reparacion_Service.model.EstadoReparacion;
import com.apr.Reparacion_Service.model.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {
    List<Reparacion> findByIncidenciaId(Long incidenciaId);
    List<Reparacion> findByEstado(EstadoReparacion estado);
}
