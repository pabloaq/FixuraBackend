package com.Fixura.FixuraBackend.Service;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Repository.IncidenteRepository;
import com.Fixura.FixuraBackend.Service.Interface.IincidenteService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class IncidenteService implements IincidenteService{

	@Autowired
  	private JwtUtil jwtUtil;

    @Autowired
	private IncidenteRepository incidenteRepository;

    @Override
	public List<Incidente> Listar_incidente_usuario(String dni) {
		List<Incidente> list;
		try {
			list=incidenteRepository.Listar_incidente_usuario(dni);
		}catch (Exception ex) {
			throw ex;
		}
		return list;
	}

    @Override
	public List<Incidente> Listar_incidente_Municipalidad(String distrito) {
		List<Incidente> list;
		try {
			list=incidenteRepository.Listar_incidente_Municipalidad(distrito);
		}catch (Exception ex) {
			throw ex;
		}
		return list;
	}

    @Override
	public int save(Incidente incidente) {
		int row;
		try {
			row=incidenteRepository.save(incidente);
		}catch (Exception ex) {
			throw ex;
		}
		return row;
	}

    @Override
	public int update_estado(Incidente incidente) {
		int row;
		try {
			row=incidenteRepository.update_estado(incidente);
		}catch (Exception ex) {
			throw ex;
		}
		return row;
	}

    @Override
	public int update_categoria(Incidente incidente) {
		int row;
		try {
			row=incidenteRepository.update_categoria(incidente);
		}catch (Exception ex) {
			throw ex;
		}
		return row;
	}

	@Override
	public int delete(int id) {
		int row;
		try {
			row=incidenteRepository.delete(id);
		}catch (Exception ex) {
			throw ex;
		}
		return row;
	}

	@Override
	public int get_total_votos(String token, int id_incidencia) {
		try {
			if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
			int num_votos = incidenteRepository.get_total_votos(id_incidencia);
			return num_votos;
		} catch (Exception ex) {
			throw new RuntimeException("Error al obtener total de votos de incidencia");
		}
	}
}
