package com.Fixura.FixuraBackend.Repository.Interface;
import java.util.List;
import com.Fixura.FixuraBackend.Model.Incidente;
public interface IincidenteRepository {
    public List<Incidente> Listar_incidente_usuario(String dni);
    public List<Incidente> Listar_incidente_Municipalidad(String distrito);
	public int save(Incidente incidente);
	public int update_estado(Incidente incidente);
    public int update_categoria(Incidente incidente);
	public int delete(int id);
    public int get_total_votos(int id_incidencia);
}
