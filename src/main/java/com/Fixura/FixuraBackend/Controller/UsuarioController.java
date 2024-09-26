package com.Fixura.FixuraBackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.AuthResponse;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.Interface.IusuarioRepository;
import com.Fixura.FixuraBackend.Service.Interface.IusuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UsuarioController {
  
  @Autowired
  private IusuarioRepository iusuarioRepository;

  @Autowired
  private IusuarioService iusuarioServicey;

  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping(value="/register")
  public ResponseEntity<ServiceResponse> register(
    @RequestBody Usuario usuario
    ){

      ServiceResponse serviceResponse = new ServiceResponse();
      
      usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

      int result = iusuarioRepository.register(usuario);
      if (result == 1) {
        serviceResponse.setMenssage("Usuario registrado correctamente");
      } else {
        serviceResponse.setMenssage("Error al registrar el usuario");
      }

      return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
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

      String jwtToken = token.substring(7); 
      //Extraer el token del encabezado, ignorando "Bearer "
      //Nota: si se quiere probar este endpoint desde postman. Pasar el token directo a iusuarioServicey.profile(token),
      Usuario userData = iusuarioServicey.profile(jwtToken);
      return new ResponseEntity<>(userData, HttpStatus.OK);
      
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}
