package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Departamento;
import com.Fixura.FixuraBackend.Repository.Interface.IdepartamentoRepository;

@Repository
public class DepartamentoRepository implements IdepartamentoRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Departamento> Listar_departamento() {
    String SQL = "SELECT * FROM Departamento";
    return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Departamento.class));
  }
}
