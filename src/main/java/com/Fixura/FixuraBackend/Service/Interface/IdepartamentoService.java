package com.Fixura.FixuraBackend.Service.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Coordenada_Distrito;
import com.Fixura.FixuraBackend.Model.Departamento;
import com.Fixura.FixuraBackend.Model.Provincia;
import com.Fixura.FixuraBackend.Model.infoMunicipalidad;
import com.Fixura.FixuraBackend.Model.Distrito;

public interface IdepartamentoService {
  public List<Departamento> Listar_departamento();
  public List<Provincia> Listar_provinvia(Integer id_depart);
  public List<Distrito> Listar_distrito(Integer id_prov);
  public List<Coordenada_Distrito> Listar_coordenadas_distrito(Integer id_distrito);
  public infoMunicipalidad getNameDistrito(int id_distrito);

} 
