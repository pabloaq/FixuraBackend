package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.AuthResponse;
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
  public int register(Usuario usuario) {

      if(!isValidEmail(usuario.getCorreo())){
        throw new RuntimeException("Correo no valido");
      }

      if(checkEmail(usuario.getCorreo())){
        return 0;
      }
      
      usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
      
      try {
        return usuarioRepository.register(usuario);
      } catch (Exception e) {
        throw new RuntimeException("Error al registrar el usuario", e);
      }
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

  @Override
  public boolean checkEmail(String email) {
    try {
      boolean valor = usuarioRepository.checkEmailExist(email);
      return valor;
    } catch (Exception e) {
      throw new RuntimeException("Error al iniciar sesión");
    }
  }

  private boolean isValidEmail(String email){
    String emailRegex = "^[a-zA-Z0-9._%+-]+@(gmail|outlook|hotmail)\\.(com|es|net)$";
    return email.matches(emailRegex);
  }
  
}
