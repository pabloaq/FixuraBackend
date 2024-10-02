package com.Fixura.FixuraBackend.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.IncidenciaLike;
import com.Fixura.FixuraBackend.Repository.Interface.IincidenciaLikeRepository;

@Repository
public class IncidenciaLikeRepository implements IincidenciaLikeRepository{

    @Autowired
  private JdbcTemplate jdbcTemplate;

    @Override
    public boolean insertLike(IncidenciaLike incidenciaLike) {
        String sql = "INSERT INTO IncidenciaLike (dni, id_incidencia, hour_liked) VALUES(?, ?, ?);";
        return jdbcTemplate.update(sql, new Object[] {
            incidenciaLike.getDni(),
            incidenciaLike.getId_incidencia(),
            incidenciaLike.getHour_liked()
        }) > 0; // devuelve true si se insertó más de 1 fila. En lo contrario, False.
    }

    @Override
    public boolean deleteLike(IncidenciaLike incidenciaLike) {
        String sql = "DELETE FROM IncidenciaLike WHERE dni = ? AND id_incidencia = ?";
        return jdbcTemplate.update(sql, new Object [] {
            incidenciaLike.getDni(),
            incidenciaLike.getId_incidencia()
        }) > 0; // devuelve true si se insertó más de 1 fila. En lo contrario, False.
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean thaLiked(IncidenciaLike incidenciaLike) {
        String sql = "SELECT COUNT(*) FROM IncidenciaLike WHERE dni = ? AND id_incidencia = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {
            incidenciaLike.getDni(),
            incidenciaLike.getId_incidencia()
        }, Integer.class) > 0; // devuelve true si se encuentra al menos una coincidencia
    }

}
