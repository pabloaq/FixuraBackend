package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.Interface.IusuarioRepository;

@Repository
public class UsuarioRepository implements IusuarioRepository{
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public int save(Usuario usuario) {
    String SQL = "INSERT INTO Usuarios (DNI, nombre, correo, contrasenia, foto_perfil, tiempo_ban, id_rol, id_distrito) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(SQL, new Object[]{
      usuario.getDNI(),
      usuario.getNombre(), 
      usuario.getCorreo(), 
      usuario.getContrasenia(), 
      usuario.getFoto_perfil(), 
      usuario.getTiempo_ban(), 
      usuario.getId_rol(), 
      usuario.getId_distrito()
    });
  }

  @Override
  public Usuario login(Usuario usuario) {
    String SQL = "SELECT * FROM Usuarios WHERE correo = '" + usuario.getCorreo() + "' AND contrasenia = '" + usuario.getContrasenia() + "'";
    List<Usuario> users = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class));
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public Usuario profile(String UserDni) {
    String SQL = "SELECT * FROM Usuarios WHERE DNI = '" + UserDni + "'";
    List<Usuario> users = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class));
    return users.isEmpty() ? null : users.get(0);
  }
}
