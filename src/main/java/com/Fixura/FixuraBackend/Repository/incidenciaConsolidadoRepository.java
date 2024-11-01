package com.Fixura.FixuraBackend.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.IncidenciaConsolidado;
import com.Fixura.FixuraBackend.Repository.Interface.IincidenciaConsolidadoRepository;

@Repository
public class incidenciaConsolidadoRepository implements IincidenciaConsolidadoRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean insertConsolidado(IncidenciaConsolidado incidenciaConsolidado) {
        String sql = "INSERT INTO incidencia_consolidado (dni, id_incidencia, hour_consolidado) VALUES(?, ?, ?);";
        return jdbcTemplate.update(sql, new Object[] {
            incidenciaConsolidado.getDni(),
            incidenciaConsolidado.getId_incidencia(),
            incidenciaConsolidado.getHour_consolidado()
        }) > 0;
    }

}
