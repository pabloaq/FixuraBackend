package com.Fixura.FixuraBackend.Repository.Interface;

import com.Fixura.FixuraBackend.Model.Usuario;

public interface IusuarioRepository {
  public int register(Usuario usuario);
  public Usuario login(String correo);
  public Usuario profile(String UserDni);
  public boolean checkEmailExist(String correo);
}
