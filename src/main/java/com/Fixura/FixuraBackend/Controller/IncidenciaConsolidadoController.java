package com.Fixura.FixuraBackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.IncidenciaConsolidado;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Service.IncidenciaConsolidadoService;

@RestController
@RequestMapping("/api/incidenteConsolidado")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidenciaConsolidadoController {
    @Autowired
    private IncidenciaConsolidadoService incConsolidadoService;

    @PostMapping(value="/insertConsolidado")
    public ResponseEntity<ServiceResponse> registerIncidenciaConsolidado(
        @RequestBody IncidenciaConsolidado incidenciaConsolidado
        ){
        ServiceResponse serviceResponse = new ServiceResponse();

        boolean result = incConsolidadoService.insertConsolidado(incidenciaConsolidado);

        if (result) {
            serviceResponse.setMenssage("Consolidado de incidencia registrado correctamente");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } else {
            serviceResponse.setMenssage("Error al registrar el Consolidado de incidencia");
            return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
