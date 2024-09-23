package com.Fixura.FixuraBackend.Repository.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Distrito;

public interface IdistritoRepository {
  public List<Distrito> Listar_distrito(Integer id_depart);
} 
