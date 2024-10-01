package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.AuthResponse;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.UsuarioRepository;
import com.Fixura.FixuraBackend.Service.Interface.IusuarioService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class UsuarioService implements IusuarioService{

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public ServiceResponse register(Usuario usuario) {
    
    ServiceResponse serviceResponse = new ServiceResponse();

    if(usuarioRepository.checkEmailExist(usuario.getCorreo())){
      serviceResponse.setMenssage("Correo ya registrado, pruebe uno diferente");
      return serviceResponse;
    }

    usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
    
    try {
      int result = usuarioRepository.register(usuario);

      if(result == 1){
        serviceResponse.setMenssage("Usuario registrado correctamente");
      }else{
        serviceResponse.setMenssage("Error al registrar el usuario");
      }

    } catch (Exception e) {
      throw e;
    }

    return serviceResponse;
  }

  @Override
  public AuthResponse login(Usuario user) {
    try {

      Usuario userData = usuarioRepository.login(user.getCorreo());
      
      if (userData != null && passwordEncoder.matches(user.getContrasenia(), userData.getContrasenia())) {
        String token = jwtUtil.generateToken(userData);
        return new AuthResponse(token);
      } else throw new RuntimeException("Credenciales inválidas");
      
    } catch (Exception e) {
      throw new RuntimeException("Error al iniciar sesión");
    }
  }

  @Override
  public Usuario profile(String token) {

    try {

      if (jwtUtil.isTokenExpired(token)) {
        throw new RuntimeException("Token Expirado...");
      }

      String userDni = jwtUtil.extractUserDni(token);
      Usuario userData = usuarioRepository.profile(userDni);
      return userData;
      
    } catch (Exception e) {
      throw new RuntimeException("Error al obtener el perfil: " + e.getMessage());
    }
  }
  
}
