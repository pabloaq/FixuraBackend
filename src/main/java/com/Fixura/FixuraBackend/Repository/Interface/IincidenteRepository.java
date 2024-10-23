package com.Fixura.FixuraBackend.Repository.Interface;
import java.util.List;

import org.springframework.data.domain.Page;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Model.IncidentesCoordenada;
import com.Fixura.FixuraBackend.Model.infoIncidente;

public interface IincidenteRepository {
    public List<Incidente> Listar_incidente_usuario(String dni);
    public Page<infoIncidente> page_incidente_usuario(int pageSize, int pageNumber, String dni);
    public Page<infoIncidente> page_incidente_usuario_distrito(int pageSize, int pageNumber, String dni, int id_distrito);
    public List<Incidente> Listar_incidente_Municipalidad(int distrito);
    public List<IncidentesCoordenada> Listar_coordenadas_incidentes_Municipalidad(int distrito);
    public IncidentesCoordenada Listar_Coordenada_Incidente(int id_incidencia);
	public int save(Incidente incidente);
	public int update_estado(Incidente incidente);
    public int update_categoria(Incidente incidente);
	public boolean delete(int id_incidencia);
    public int get_total_votos(int id_incidencia);
    public String get_name_user(int id_incidencia);
    public boolean update_incidente(Incidente incidente);
}
