package com.Fixura.FixuraBackend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Service.Interface.IdepartamentoService;

import com.Fixura.FixuraBackend.Model.Departamento;
import com.Fixura.FixuraBackend.Model.Provincia;
import com.Fixura.FixuraBackend.Model.Distrito;
import com.Fixura.FixuraBackend.Repository.Interface.IdepartamentoRepository;
import com.Fixura.FixuraBackend.Repository.Interface.IprovinciaRepository;
import com.Fixura.FixuraBackend.Repository.Interface.IdistritoRepository;

@Service
public class DepartamentoService implements IdepartamentoService {

  @Autowired
  private IdepartamentoRepository idepartamentoRepository;

  @Autowired
  private IprovinciaRepository iprovinciaRepository;
  
  @Autowired
  private IdistritoRepository idistritoRepository;

  @Override
  public List<Departamento> Listar_departamento() {
    List<Departamento> list;
    try {
      list = idepartamentoRepository.Listar_departamento();
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  @Override
  public List<Provincia> Listar_provinvia(Integer id_depart) {
    List<Provincia> list;
    try {
      list = iprovinciaRepository.Listar_provincia(id_depart);
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  @Override
  public List<Distrito> Listar_distrito(Integer id_prov) {
    List<Distrito> list;
    try{
      list = idistritoRepository.Listar_distrito(id_prov);
    }catch (Exception e) {
      throw e;
    }
    return list;
  }
  
}
