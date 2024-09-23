package com.Fixura.FixuraBackend.Repository.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Provincia;

public interface IprovinciaRepository {
  public List<Provincia> Listar_provincia(Integer id_depart);

}
