package com.Fixura.FixuraBackend.Service;

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
public class UsuarioService implements IusuarioService{

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

    // Se genera el token de verificación de email
    String token_email_verification = jwtUtil.generateEmailValidationToken(usuario);
    usuario.setToken_verification(token_email_verification);
    usuario.setActivo(false);

    // Guardamos el usuario
    try {
        int result = usuarioRepository.register(usuario);
        emailVerification.sendEmailVerification(usuario, "Confirma tu Correo Electronico", "verify-email", "email-verification-template");
        return result;
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
      return usuarioRepository.checkEmailExist(email);
    } catch (Exception e) {
      throw new RuntimeException("Error al verificar el correo");
    }
  }

  @Override
  public ApiDniResponse getNameUserByDNI(String DNI) {
    String url = "https://apiperu.dev/api/dni/"+DNI+"?api_token=e15f4001510845c4c28d081d955fb095981312d118efda2d1658bbbf84349d84";
    return restTemplate.getForObject(url, ApiDniResponse.class);
  }

  private boolean isValidEmail(String email){
    String emailRegex = "^[a-zA-Z0-9._%+-]+@(gmail|outlook|hotmail)\\.(com|es|net)$";
    return email.matches(emailRegex);
  }
}
