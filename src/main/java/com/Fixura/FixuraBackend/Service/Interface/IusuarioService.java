package com.Fixura.FixuraBackend.Service.Interface;

import com.Fixura.FixuraBackend.Model.AuthResponse;
import com.Fixura.FixuraBackend.Model.Usuario;

public interface IusuarioService {
  public int register(Usuario usuario);
  public AuthResponse login(Usuario user);
  public Usuario profile(String token);
}
