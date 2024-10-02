package com.Fixura.FixuraBackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.IncidenciaLike;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Service.IncidenciaLikeService;

@RestController
@RequestMapping("/api/incidenteLike")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidenciaLikeController {

    @Autowired
    private IncidenciaLikeService incidenciaLikeService;

    @PostMapping(value="/insertLike")
    public ResponseEntity<ServiceResponse> registerIncidenciaLike(
        @RequestHeader("Authorization") String token,
        @RequestBody IncidenciaLike incidenciaLike
        ){
        ServiceResponse serviceResponse = new ServiceResponse();

        boolean result = incidenciaLikeService.insertLike(token, incidenciaLike);

        if (result) {
            serviceResponse.setMenssage("Like registrado correctamente");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } else {
            serviceResponse.setMenssage("Error al registrar el Like de incidencia");
            return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/deleteLike")
	public ResponseEntity<ServiceResponse> deleteIncidenciaLike(
        @RequestHeader("Authorization") String token,
        @RequestBody IncidenciaLike incidenciaLike
        ){
        ServiceResponse serviceResponse = new ServiceResponse();

        boolean result = incidenciaLikeService.deleteLike(token, incidenciaLike);

        if (result) {
            serviceResponse.setMenssage("Like eliminado correctamente");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } else {
            serviceResponse.setMenssage("Error al eliminar el Like de incidencia");
            return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/thaLike")
    public ResponseEntity<Boolean> thaIncidenciaLike(
        @RequestHeader("Authorization") String token,
        @RequestBody IncidenciaLike incidenciaLike
        ){
        boolean result = incidenciaLikeService.thaLiked(token, incidenciaLike);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
