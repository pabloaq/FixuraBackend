package com.Fixura.FixuraBackend.Model;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Usuario {
  private String DNI;
  private String nombre;
  private String correo;
  private String contrasenia;
  private String foto_perfil;
  private Timestamp tiempo_ban;
  private int id_rol;
  private int id_distrito;
  private String token_verification;
  private boolean activo;
}
