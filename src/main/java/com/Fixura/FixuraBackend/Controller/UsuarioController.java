package com.Fixura.FixuraBackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.Interface.IusuarioRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UsuarioController {
  
  @Autowired
  private IusuarioRepository iusuarioRepository;

  @PostMapping(value="/save")
  public ResponseEntity<ServiceResponse> save(
    @RequestBody Usuario usuario
    ){

      ServiceResponse serviceResponse = new ServiceResponse();
      
      int result = iusuarioRepository.save(usuario);
      if (result == 1) {
        serviceResponse.setMenssage("Usuario registrado correctamente");
      } else {
        serviceResponse.setMenssage("Error al registrar el usuario");
      }

      return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<Usuario> login(@RequestBody Usuario usuario) {
      var user = iusuarioRepository.login(usuario);

      if (user != null && user.getContrasenia().equals(usuario.getContrasenia())) {
        return new ResponseEntity<>(user, HttpStatus.OK);
      } else {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
  }
  
}
