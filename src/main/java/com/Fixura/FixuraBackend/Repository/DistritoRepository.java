package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Coordenada_Distrito;
import com.Fixura.FixuraBackend.Model.Distrito;
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
}
