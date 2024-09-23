package com.Fixura.FixuraBackend.Repository.Interface;

import com.Fixura.FixuraBackend.Model.Usuario;

public interface IusuarioRepository {
  public int save(Usuario usuario);
  public Usuario login(Usuario usuario);
}
