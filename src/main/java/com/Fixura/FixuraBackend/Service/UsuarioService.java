package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Override
  public int save(Usuario usuario) {
    int row;
    try {
      row = usuarioRepository.save(usuario);
    } catch (Exception e) {
      throw e;
    }
    return row;
  }

  @Override
  public AuthResponse login(Usuario user) {
    Usuario userData;

    try {

      userData = usuarioRepository.login(user);
      
      if (userData != null && userData.getContrasenia().equals(user.getContrasenia())) {
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
