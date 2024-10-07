package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Categoria;
import com.Fixura.FixuraBackend.Repository.Interface.IcategoriaRepository;

@Repository
public class CategoriaRepository implements IcategoriaRepository{

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
    public List<Categoria> listCategory() {
        String sql = "SELECT * FROM categoria";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Categoria.class));
    }

    @SuppressWarnings("deprecation")
    @Override
    public Categoria getNameCategory(int id_categoria) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?;";
        return jdbcTemplate.queryForObject(sql, new Object[]{id_categoria}, BeanPropertyRowMapper.newInstance(Categoria.class));
    }

}
