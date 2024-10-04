package com.Fixura.FixuraBackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.AuthResponse;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Service.Interface.IusuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {
  
  @Autowired
  private IusuarioService iusuarioServicey;


  @PostMapping(value="/register")
  public ResponseEntity<?> register(@RequestBody Usuario usuario){

    ServiceResponse response = new ServiceResponse();

    try{
        int result = iusuarioServicey.register(usuario);

        if(result != 0){
          response.setSuccess(true);
          response.setMenssage("Registro exitoso");
          return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
          response.setSuccess(false);
          response.setMenssage("El correo electrónico ya existe");
          System.out.println("El correo ya existe - Controller");
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }catch (Exception e) {
      response.setSuccess(false);
      response.setMenssage("Error al registrarse");
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Usuario user) {
    
    try {
      AuthResponse authResponse = iusuarioServicey.login(user);
      return ResponseEntity.ok(authResponse);
    } catch (Exception e) {
        return new ResponseEntity<>("Error al iniciar sesión", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/profile")
  public ResponseEntity<Usuario> getUserProfile(@RequestHeader("Authorization") String token) {
    try {

      
      Usuario userData = iusuarioServicey.profile(token);
      return new ResponseEntity<>(userData, HttpStatus.OK);
      
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value="/existEmail")
  public ResponseEntity<Boolean> existEmail(@RequestBody String correo) {
      boolean response = iusuarioServicey.checkEmail(correo);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  
}
