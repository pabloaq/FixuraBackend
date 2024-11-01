package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.IncidenciaConsolidado;
import com.Fixura.FixuraBackend.Repository.Interface.IincidenciaConsolidadoRepository;
import com.Fixura.FixuraBackend.Service.Interface.IincidenciaConsolidadoService;
// import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class IncidenciaConsolidadoService implements IincidenciaConsolidadoService {

    // @Autowired
    // private JwtUtil jwtUtil;

    @Autowired
    private IincidenciaConsolidadoRepository incConsolidadoRepository;

    @Override
    public boolean insertConsolidado(IncidenciaConsolidado incidenciaConsolidado) {
        try {
            return incConsolidadoRepository.insertConsolidado(incidenciaConsolidado);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar Consolidado", e);
        }
    }

}
