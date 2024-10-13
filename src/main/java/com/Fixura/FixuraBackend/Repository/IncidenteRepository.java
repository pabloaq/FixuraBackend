package com.Fixura.FixuraBackend.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Model.IncidentesCoordenada;
import com.Fixura.FixuraBackend.Repository.Interface.IincidenteRepository;

@Repository
public class IncidenteRepository implements IincidenteRepository{

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
	public List<Incidente> Listar_incidente_usuario(String dni) {
		String sql = "select id_incidencia,fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,id_categoria from Incidencia where DNI='"+dni+"' AND Incidencia.id_estado <> 4 ORDER BY fecha_publicacion DESC";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Incidente.class));
	}
	
    @Override
	public List<Incidente> Listar_incidente_Municipalidad(int distrito) {
		String sql = "select id_incidencia,fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,id_categoria from Incidencia inner join Usuarios on Incidencia.DNI=Usuarios.DNI where Usuarios.id_distrito="+distrito+" AND Incidencia.id_estado <> 4 ORDER BY fecha_publicacion DESC";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Incidente.class));
	}
	@Override
	public List<IncidentesCoordenada> Listar_coordenadas_incidentes_Municipalidad(int distrito) {
		String sql = "select id_incidencia,latitud,longitud from Incidencia inner join Usuarios on Incidencia.DNI=Usuarios.DNI where Usuarios.id_distrito="+distrito+" AND Incidencia.id_estado <> 4 AND latitud is not null AND longitud is not null";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(IncidentesCoordenada.class));
	}
	@Override
	public IncidentesCoordenada Listar_Coordenada_Incidente(int id_incidencia) {
		String sql = "select id_incidencia, latitud, longitud from incidencia where id_incidencia = ?";
		return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(IncidentesCoordenada.class), id_incidencia);
	}
	@Override
	public int save(Incidente incidente) {
		String sql = "INSERT INTO Incidencia(fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,DNI,id_categoria,latitud,longitud) VALUES(?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,new Object[] {
			incidente.getFecha_publicacion(),
			incidente.getDescripcion(),
			incidente.getUbicacion(),
			incidente.getImagen(),
			incidente.getTotal_votos(),
            incidente.getId_estado(),
			incidente.getDNI(),
			incidente.getId_categoria(),
			incidente.getLatitud(),
			incidente.getLongitud()
		});
	}

	@Override
	public int update_estado(Incidente incidente) {
		String sql = "UPDATE Incidencia SET id_estado=? WHERE id_incidencia=?";
		return jdbcTemplate.update(sql,new Object[] {
			incidente.getId_estado(),
			incidente.getId_incidencia()
		});
	}

    @Override
	public int update_categoria(Incidente incidente) {
		String sql = "UPDATE Incidencia SET id_categoria=? WHERE id_incidencia=?";
		return jdbcTemplate.update(sql,new Object[] {
			incidente.getId_categoria(),
			incidente.getId_incidencia()
		});
	}

	@Override
	public boolean delete(int id_incidencia) {
		String sql = "UPDATE Incidencia SET id_estado = 4 WHERE id_incidencia = ?;";
		return jdbcTemplate.update(sql, new Object[] {id_incidencia}) > 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int get_total_votos(int id_incidencia) {
		String sql = "SELECT total_votos FROM Incidencia WHERE id_incidencia = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {id_incidencia}, Integer.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String get_name_user(int id_incidencia) {
		String sql = """
		SELECT us.nombre FROM Incidencia AS inc
		INNER JOIN usuarios AS us ON us.dni = inc.dni
		WHERE inc.id_incidencia = ?;
		""";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{id_incidencia}, String.class);
	}

	@Override
	public boolean update_incidente(Incidente incidente) {
		String sql = """
		UPDATE Incidencia
		SET id_estado = ?, id_categoria = ?, ubicacion = ?, latitud=?, longitud=?
		WHERE id_incidencia = ?;
		""";

		int result = jdbcTemplate.update(sql,new Object[] {
			incidente.getId_estado(),
			incidente.getId_categoria(),
			incidente.getUbicacion(),
			incidente.getLatitud(),
			incidente.getLongitud(),
			incidente.getId_incidencia()
		}) ;
		
		if (result == 0) {
			throw new RuntimeException("No se pudo actualizar el incidente. ID no encontrado: " + incidente.getId_incidencia());
		}
		return result > 0;
	}

}
