package com.Fixura.FixuraBackend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Estado;
import com.Fixura.FixuraBackend.Repository.EstadoRepository;
import com.Fixura.FixuraBackend.Service.Interface.IestadoService;
import com.Fixura.FixuraBackend.Service.EstadoService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class EstadoService implements IestadoService{

    @Autowired
  	private JwtUtil jwtUtil;

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public List<Estado> listState(String token) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
            return estadoRepository.listState();
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener lista de estados ", ex);
        }
    }

    @Override
    public Estado getNameState(String token, int id_state) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
            return estadoRepository.getNameState(id_state);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener nombre de estado ", ex);
        }
    }

}