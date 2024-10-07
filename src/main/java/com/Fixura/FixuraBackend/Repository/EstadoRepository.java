package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Estado;
import com.Fixura.FixuraBackend.Repository.Interface.IestadoRepository;

@Repository
public class EstadoRepository implements IestadoRepository{

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
    public List<Estado> listState() {
        String sql = "SELECT * FROM Estado";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Estado.class));
    }

    @SuppressWarnings("deprecation")
    @Override
    public Estado getNameState(int id_state) {
        String sql = "SELECT * FROM Estado WHERE id_estado = ?;";
        return jdbcTemplate.queryForObject(sql, new Object[]{id_state}, BeanPropertyRowMapper.newInstance(Estado.class));
    }

}
