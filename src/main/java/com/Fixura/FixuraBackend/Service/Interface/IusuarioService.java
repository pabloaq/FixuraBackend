package com.Fixura.FixuraBackend.Service.Interface;

import com.Fixura.FixuraBackend.Model.Usuario;

public interface IusuarioService {
  public int save(Usuario usuario);
  public Usuario login(Usuario usuario);
}
