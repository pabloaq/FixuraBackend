package com.Fixura.FixuraBackend.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Usuario;
import com.Fixura.FixuraBackend.Repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.login(correo);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }

        // Convierte tu entidad Usuario a un objeto UserDetails
        return new org.springframework.security.core.userdetails.User(user.getCorreo(), user.getContrasenia(), new ArrayList<>());
    }
}

