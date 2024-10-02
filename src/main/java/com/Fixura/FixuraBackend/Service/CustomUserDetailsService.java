package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.UsuarioRepository;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        //Buscamos al usuario en la base de datos mediante el correo
        Usuario user = usuarioRepository.login(correo);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }

        // Asignamos el rol del usuario como autoridad (GrantedAuthority)
        GrantedAuthority authority = new SimpleGrantedAuthority(getRole(user.getId_rol()));

        // Convierte tu entidad Usuario a un objeto UserDetails, que incluye el correo, la contraseña y los roles
        return new User(user.getCorreo(), user.getContrasenia(), Collections.singletonList(authority));
    }

    // Metodo que obtiene el rol basado en el idRol del usuario
    public String getRole(int idRol){
        switch (idRol) {
            case 1:
                return "ADMIN";
            
            case 2:
                return "MODERATOR";
        
            case 3:
                return "USER";
            
        
            default:
                throw new IllegalArgumentException("Rol desconocido: " + idRol);
        }
    }
}

