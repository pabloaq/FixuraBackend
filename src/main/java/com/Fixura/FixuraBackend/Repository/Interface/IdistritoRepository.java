package com.Fixura.FixuraBackend.Repository.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Coordenada_Distrito;
import com.Fixura.FixuraBackend.Model.Distrito;
import com.Fixura.FixuraBackend.Model.infoMunicipalidad;

public interface IdistritoRepository {
  public List<Distrito> Listar_distrito(Integer id_depart);

  public List<Coordenada_Distrito> Listar_coordenadas_distrito(Integer id_distrito);

  public infoMunicipalidad getNameDistrito(int id_distrito);

} 
