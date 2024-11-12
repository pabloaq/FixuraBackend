package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Coordenada_Distrito;
import com.Fixura.FixuraBackend.Model.Distrito;
import com.Fixura.FixuraBackend.Model.infoMunicipalidad;
import com.Fixura.FixuraBackend.Repository.Interface.IdistritoRepository;

@Repository
public class DistritoRepository implements IdistritoRepository{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Distrito> Listar_distrito(Integer id_prov) {
    String SQL = "SELECT * FROM Distrito WHERE id_provincia = '"+ id_prov +"' ";
    return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Distrito.class));
  }
  
  @Override
  public List<Coordenada_Distrito> Listar_coordenadas_distrito(Integer id_distrito) {
    String SQL = "SELECT * FROM Coordenada_Distrito where id_distrito= '"+ id_distrito +"' ";
    return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Coordenada_Distrito.class));
  }

  @SuppressWarnings("deprecation")
  @Override
  public infoMunicipalidad getNameDistrito(int id_distrito) {
      String SQL = """
      SELECT dis.nombre, us.foto_perfil FROM distrito as dis
      INNER JOIN usuarios as us on us.id_distrito = dis.id_distrito
      WHERE us.id_rol = 1 AND dis.id_distrito = ?""";
      try {
          return jdbcTemplate.queryForObject(SQL, new Object[]{id_distrito}, BeanPropertyRowMapper.newInstance(infoMunicipalidad.class));
      } catch (EmptyResultDataAccessException e) {
          // Devuelve null si no encuentra el distrito con el id especificado
          return null;
      }
  }

}
