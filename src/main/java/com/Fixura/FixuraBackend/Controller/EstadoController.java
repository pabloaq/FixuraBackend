package com.Fixura.FixuraBackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.Estado;
import com.Fixura.FixuraBackend.Service.EstadoService;

@RestController
@RequestMapping("/api/estado")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadoController {

    @Autowired
    private EstadoService estadoService; 

    @GetMapping("/list")
    public ResponseEntity<List<Estado>> listStates(@RequestHeader("Authorization") String token){
        List<Estado> list  = estadoService.listState(token);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

    @GetMapping("/name/{idState}")
    public ResponseEntity<Estado> getNameState(
        @RequestHeader("Authorization") String token,
        @PathVariable int idState
        ){
        Estado result  = estadoService.getNameState(token, idState);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
