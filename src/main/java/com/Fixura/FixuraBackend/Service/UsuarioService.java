package com.Fixura.FixuraBackend.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Fixura.FixuraBackend.Model.ApiDniResponse;
import com.Fixura.FixuraBackend.Model.AuthResponse;
import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.UsuarioRepository;
import com.Fixura.FixuraBackend.Service.Interface.IusuarioService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class UsuarioService implements IusuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailVerification emailVerification;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public int register(Usuario usuario) {

    // Validar email
    if (!isValidEmail(usuario.getCorreo())) {
      throw new RuntimeException("Correo no valido");
    }

    // Verificar si el correo ya existe
    if (checkEmail(usuario.getCorreo())) {
      throw new RuntimeException("El correo electrónico ya existe");
    }

    // Verificar el DNI
    ApiDniResponse dniResponse = getNameUserByDNI(usuario.getDNI());
    if (!dniResponse.isSuccess()) {
      throw new RuntimeException("El DNI ingresado no es válido");
    }

    // Establecer valores del usuario y codificar la contraseña
    usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
    usuario.setNombre(dniResponse.getData().getNombres());
    usuario.setApellido(dniResponse.getData().getApellidoPaterno() + " " + dniResponse.getData().getApellidoMaterno());

    // Se genera el token de verificación de email
    String token_email_verification = jwtUtil.generateEmailValidationToken(usuario);
    usuario.setToken_verification(token_email_verification);
    usuario.setActivo(false);
    usuario.setBanned(false);

    // Guardamos el usuario
    try {
      int result = usuarioRepository.register(usuario);
      emailVerification.sendEmailVerification(usuario, "Confirma tu Correo Electronico", "verify-email",
          "email-verification-template");
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Error al registrar el usuario", e);
    }
  }

  @Override
  public AuthResponse login(Usuario user) {
    Usuario userData = usuarioRepository.login(user.getCorreo());

    if (userData == null) {
      throw new RuntimeException("Credenciales inválidas");
    }

    if (!passwordEncoder.matches(user.getContrasenia(), userData.getContrasenia())) {
      throw new RuntimeException("Contraseña incorrecta");
    }

    if (userData.isBanned()) {
      if (userData.getTiempo_ban() != null) {
        if (userData.getTiempo_ban().after(new Date())) {
          throw new RuntimeException("Usuario baneado hasta: " + userData.getTiempo_ban());
        } else {
          usuarioRepository.unbanUser(userData.getDNI());
        }
      } else {
        throw new RuntimeException("Cuenta bloqueada. Por favor, pónganse en contacto con el soporte técnico.");
      }
    }

    String token = jwtUtil.generateToken(userData);
    return new AuthResponse(token);
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
      return usuarioRepository.checkEmailExist(email);
    } catch (Exception e) {
      throw new RuntimeException("Error al verificar el correo");
    }
  }

  @Override
  public ApiDniResponse getNameUserByDNI(String DNI) {
    String url = "https://apiperu.dev/api/dni/" + DNI
        + "?api_token=e15f4001510845c4c28d081d955fb095981312d118efda2d1658bbbf84349d84";
    return restTemplate.getForObject(url, ApiDniResponse.class);
  }

  @Override
  public int banUser(String dni, boolean isPermanent, String durationBan) {
    return usuarioRepository.banUser(dni, isPermanent, durationBan);
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9._%+-]+@(gmail|outlook|hotmail)\\.(com|es|net)$";
    return email.matches(emailRegex);
  }

  @Override
  public boolean updatePerfilUsuario(Usuario user) {
    try {
      return usuarioRepository.updatePerfilUsuario(user);
    } catch (Exception ex) {
      throw new RuntimeException("Error al Actualizar usuario: " + ex);
    }
  }

  @Override
  public boolean getBanStatus(String dni) {
    try {
      System.out.println(dni);
      return usuarioRepository.getBanStatus(dni);
    } catch (Exception ex) {
      throw new RuntimeException("Error al obtener el estado de baneo del usuario");
    }
  }

}
