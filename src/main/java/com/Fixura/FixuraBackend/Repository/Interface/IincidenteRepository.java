package com.Fixura.FixuraBackend.Repository.Interface;
import java.util.List;
import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Model.IncidentesCoordenada;
public interface IincidenteRepository {
    public List<Incidente> Listar_incidente_usuario(String dni);
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
