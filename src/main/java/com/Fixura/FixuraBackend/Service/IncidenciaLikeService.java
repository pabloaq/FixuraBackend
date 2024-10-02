package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.IncidenciaLike;
import com.Fixura.FixuraBackend.Repository.IncidenciaLikeRepository;
import com.Fixura.FixuraBackend.Service.Interface.IincidenciaLikeService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class IncidenciaLikeService implements IincidenciaLikeService{

    @Autowired
    private IncidenciaLikeRepository incidenciaLikeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean insertLike(String token, IncidenciaLike incidenciaLike) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token invalido o expirado...");
            }

            return incidenciaLikeRepository.insertLike(incidenciaLike);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar Like", e);
        }
    }

    @Override
    public boolean deleteLike(String token, IncidenciaLike incidenciaLike) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token invalido o expirado...");
            }

            return incidenciaLikeRepository.deleteLike(incidenciaLike);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar Like", e);
        }
    }

    @Override
    public boolean thaLiked(String token, IncidenciaLike incidenciaLike) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token invalido o expirado...");
            }

            return incidenciaLikeRepository.thaLiked(incidenciaLike);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar estado del Like", e);
        }
    }

}
