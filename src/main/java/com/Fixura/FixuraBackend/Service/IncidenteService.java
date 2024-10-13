package com.Fixura.FixuraBackend.Service;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Model.IncidentesCoordenada;
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
	public List<Incidente> Listar_incidente_Municipalidad(int distrito) {
		List<Incidente> list;
		try {
			list=incidenteRepository.Listar_incidente_Municipalidad(distrito);
		}catch (Exception ex) {
			throw ex;
		}
		return list;
	}
	@Override
	public List<IncidentesCoordenada> Listar_coordenadas_incidentes_Municipalidad(int distrito) {
		List<IncidentesCoordenada> list;
		try {
			list=incidenteRepository.Listar_coordenadas_incidentes_Municipalidad(distrito);
		}catch (Exception ex) {
			throw ex;
		}
		return list;
	}
	@Override
	public IncidentesCoordenada Listar_Coordenada_Incidente(int id_incidencia) {
		IncidentesCoordenada coordenada_incidencia;
		try {
			coordenada_incidencia=incidenteRepository.Listar_Coordenada_Incidente(id_incidencia);
		}catch (Exception ex) {
			throw ex;
		}
		return coordenada_incidencia;
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
	public boolean delete(String token, int id_incidencia) {
		try {
			if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
			return incidenteRepository.delete(id_incidencia);
		}catch (Exception ex) {
			throw new RuntimeException("Error al Elimianr incidencia con ID: " + id_incidencia);
		}
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

	@Override
	public String get_name_user(String token, int id_incidencia) {
		try {
			if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
			String name_user = incidenteRepository.get_name_user(id_incidencia);
			return name_user;
		} catch (Exception ex) {
			throw new RuntimeException("Error al obtener el NOMBRE DE USUARIO de la incidencia");
		}
	}

	@Override
	public boolean update_incidente(String token, Incidente incidente) {
		try {
			if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
			return incidenteRepository.update_incidente(incidente);
		} catch (Exception ex) {
			throw new RuntimeException("Error al Actualizar incidencia con ID: "+ incidente.getId_incidencia());
		}
	}
}
