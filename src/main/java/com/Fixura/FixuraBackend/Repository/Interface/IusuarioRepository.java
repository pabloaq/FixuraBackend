package com.Fixura.FixuraBackend.Repository.Interface;

import com.Fixura.FixuraBackend.Model.Usuario;

public interface IusuarioRepository {
  public int register(Usuario usuario);
  public Usuario login(String correo);
  public Usuario profile(String UserDni);
  public boolean checkEmailExist(String correo);
  public Usuario findByCorreo(String correo);
  public int updateUsuario(Usuario usuario);
  public int updatePassword(String new_password, String correo);
  public int banUser(String dni, boolean isPermanent, String durationBan);
  public boolean updatePerfilUsuario(Usuario usuario);
  public boolean unbanUser(String dni);
  public boolean getBanStatus(String dni);
}
