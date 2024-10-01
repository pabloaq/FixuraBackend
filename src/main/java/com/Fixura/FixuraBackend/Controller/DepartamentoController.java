package com.Fixura.FixuraBackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.Departamento;
import com.Fixura.FixuraBackend.Model.Provincia;
import com.Fixura.FixuraBackend.Model.Distrito;
import com.Fixura.FixuraBackend.Service.Interface.IdepartamentoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/departamento")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartamentoController {
  
  @Autowired
  private IdepartamentoService idepartamentoService;

  @GetMapping("/list")
  public ResponseEntity<List<Departamento>> list_departamento(){
    var result = idepartamentoService.Listar_departamento();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{id_depart}/provincias")
  public ResponseEntity<List<Provincia>> listProvinciasPorDepartamento(@PathVariable Integer id_depart) {
    var result = idepartamentoService.Listar_provinvia(id_depart);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{id_depart}/provincia/{id_prov}/distritos")
  public ResponseEntity<List<Distrito>> listDistritosPorProvinciaYDepartamento(
    @PathVariable Integer id_prov
    ){
    var result = idepartamentoService.Listar_distrito(id_prov);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  
}
