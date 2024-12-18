package com.Fixura.FixuraBackend.Repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.Interface.IusuarioRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Repository
public class UsuarioRepository implements IusuarioRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public int register(Usuario usuario) {
    String SQL = "INSERT INTO Usuarios (DNI, nombre, apellido, correo, contrasenia, foto_perfil, tiempo_ban, id_rol, id_distrito, token_verification, activo, banned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(SQL, new Object[] {
        usuario.getDNI(),
        usuario.getNombre(),
        usuario.getApellido(),
        usuario.getCorreo(),
        usuario.getContrasenia(),
        usuario.getFoto_perfil(),
        usuario.getTiempo_ban(),
        usuario.getId_rol(),
        usuario.getId_distrito(),
        usuario.getToken_verification(),
        usuario.isActivo(),
        usuario.isBanned()
    });
  }

  @Override
  public Usuario login(String correo) {
    String SQL = "SELECT * FROM Usuarios WHERE correo = ? AND activo = true";
    List<Usuario> users = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class), correo);
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public Usuario profile(String UserDni) {
    String SQL = "SELECT * FROM Usuarios WHERE DNI = '" + UserDni + "'";
    List<Usuario> users = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class));
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public boolean checkEmailExist(String correo) {
    String SQL = "SELECT COUNT(*) FROM Usuarios WHERE correo = ?";
    int count = jdbcTemplate.queryForObject(SQL, Integer.class, correo);
    return count > 0;
  }

  @Override
  public Usuario findByCorreo(String correo) {
    String SQL = "SELECT * FROM Usuarios WHERE correo = ?";
    List<Usuario> users = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class), correo);
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public int updateUsuario(Usuario usuario) {
    String SQL = "UPDATE Usuarios SET activo = ?, token_verification = ? WHERE correo = ?";
    return jdbcTemplate.update(SQL, usuario.isActivo(), usuario.getToken_verification(), usuario.getCorreo());
  }

  @Override
  public int updatePassword(String new_password, String correo) {
    String SQL = "UPDATE Usuarios SET contrasenia = ? WHERE correo = ?";
    return jdbcTemplate.update(SQL, new_password, correo);
  }

  @Override
  public int banUser(String dni, boolean isPermanent, String durationBan) {
    String SQL;

    if (isPermanent) {
      SQL = "UPDATE Usuarios SET tiempo_ban = NULL, banned = true WHERE dni = ?";
      return jdbcTemplate.update(SQL, dni);
    } else {
      try {
        String formattedString = durationBan.replace("T", " ").replace("Z","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(sdf.parse(formattedString).getTime());

        SQL = "UPDATE Usuarios SET tiempo_ban = ?, banned = true WHERE dni = ?";
        
        return jdbcTemplate.update(SQL, timestamp, dni);
        
      } catch (Exception e) {
        System.err.println("Error al parsear la fecha: " + e.getMessage());
        return 0;
      }
    }
  }

  public boolean updatePerfilUsuario(Usuario usuario) {
    String SQL = "update usuarios set foto_perfil = ? , id_distrito = ? where dni = ?";
    int result = jdbcTemplate.update(SQL, usuario.getFoto_perfil(), usuario.getId_distrito(), usuario.getDNI());
    if (result == 0) {
      throw new RuntimeException("No se pudo actualizar el usuario " );
  }
    return result > 0;
  }

  @Override
  public boolean unbanUser(String dni) {
    String SQL = "UPDATE Usuarios SET tiempo_ban = NULL, banned = false WHERE dni = ?";
    return (jdbcTemplate.update(SQL, dni)) > 0;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean getBanStatus(String dni) {
    String SQL = "SELECT banned FROM Usuarios WHERE dni = ?";
    Boolean bannedStatus = jdbcTemplate.queryForObject(SQL, new Object[]{dni}, Boolean.class);
    return bannedStatus != null && bannedStatus;
  }
}
