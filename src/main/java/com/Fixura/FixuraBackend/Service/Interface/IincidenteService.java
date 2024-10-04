package com.Fixura.FixuraBackend.Service.Interface;
import java.util.List;

import com.Fixura.FixuraBackend.Model.Incidente;

public interface IincidenteService {
    public List<Incidente> Listar_incidente_usuario(String dni);
    public List<Incidente> Listar_incidente_Municipalidad(String distrito);
	public int save(Incidente incidente);
	public int update_estado(Incidente incidente);
    public int update_categoria(Incidente incidente);
	public boolean delete(String token, int id_incidencia);
    public int get_total_votos(String token, int id_incidencia);
    public String get_name_user(String token, int id_incidencia);
    public boolean update_incidente(String token, Incidente incidente);
}
