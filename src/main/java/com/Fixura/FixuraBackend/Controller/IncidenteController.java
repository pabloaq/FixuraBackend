package com.Fixura.FixuraBackend.Controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Model.IncidentesCoordenada;
import com.Fixura.FixuraBackend.Model.ServiceResponse;
import com.Fixura.FixuraBackend.Model.UsuarioBlock;
import com.Fixura.FixuraBackend.Model.infoIncidente;
import com.Fixura.FixuraBackend.Service.WebSocketService;
import com.Fixura.FixuraBackend.Service.Interface.IincidenteService;

import java.sql.Timestamp;
import java.text.ParseException;

@RestController
@RequestMapping("/api/incidente")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidenteController {

    @Autowired
	private IincidenteService iincidenteService;

	@Autowired
	private WebSocketService webSocketService;

	@GetMapping("/list/usuario/{id}")
	public ResponseEntity<List<Incidente>> list(@PathVariable String id) {
		var result = iincidenteService.Listar_incidente_usuario(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/list/incidente_id/{id}")
	public ResponseEntity<infoIncidente> list(@PathVariable int id) {
		var result = iincidenteService.Listar_incidente_porID(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/list/paginated/usuario")
	public ResponseEntity<Page<infoIncidente>> getIncidentesPorUsuario(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam String dni) {
		Page<infoIncidente> incidentes = iincidenteService.page_incidente_usuario(size, page, dni);
		if (incidentes.getTotalElements() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no hay registros
		}
		return new ResponseEntity<>(incidentes, HttpStatus.OK);
	}

	@GetMapping("/list/paginated/usuario_distrito")
	public ResponseEntity<Page<infoIncidente>> getIncidentesPorUsuarioDistrito(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam String dni,
			@RequestParam int id_distrito) {
		Page<infoIncidente> incidentes = iincidenteService.page_incidente_usuario_distrito(size, page, dni, id_distrito);
		if (incidentes.getTotalElements() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no hay registros
		}
		return new ResponseEntity<>(incidentes, HttpStatus.OK);
	}

	@GetMapping("/list/paginated/distrito")
	public ResponseEntity<Page<infoIncidente>> getIncidentesPorDistrito(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam int id_distrito) {
		Page<infoIncidente> incidentes = iincidenteService.page_incidente_distrito(size, page, id_distrito);
		if (incidentes.getTotalElements() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no hay registros
		}
		return new ResponseEntity<>(incidentes, HttpStatus.OK);
	}

	@GetMapping("/list/paginated/consolidado")
	public ResponseEntity<Page<infoIncidente>> getConsolidadoPorDistrito(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam int id_distrito) {
		Page<infoIncidente> incidentes = iincidenteService.page_consolidado_distrito(size, page, id_distrito);
		if (incidentes.getTotalElements() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no hay registros
		}
		return new ResponseEntity<>(incidentes, HttpStatus.OK);
	}

	@GetMapping("/list/municipalidad/{id_distrito}")
	public ResponseEntity<List<Incidente>> list2(@PathVariable int id_distrito) {
		var result = iincidenteService.Listar_incidente_Municipalidad(id_distrito);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/list/municipalidad/usuarios/{id_distrito}")
	public ResponseEntity<List<UsuarioBlock>> getMethodName(@PathVariable int id_distrito) {
		var result = iincidenteService.Listar_usuarios_municipalidad(id_distrito);

		if (result.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/list/coordenadas/{id_distrito}")
	public ResponseEntity<List<IncidentesCoordenada>> list3(@PathVariable int id_distrito) {
		var result = iincidenteService.Listar_coordenadas_incidentes_Municipalidad(id_distrito);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/list/coordenada/{id_incidente}")
	public ResponseEntity<IncidentesCoordenada> list4(@PathVariable int id_incidente) {
		var result = iincidenteService.Listar_Coordenada_Incidente(id_incidente);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/save")
	public ResponseEntity<ServiceResponse> save(@RequestParam("fecha_publicacion") String fecha_publicacion,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("ubicacion") String ubicacion,
			@RequestParam("imagen") String imagen,
			@RequestParam("total_votos") int total_votos,
			@RequestParam("id_estado") int id_estado,
			@RequestParam("DNI") String DNI,
			@RequestParam("id_categoria") int id_categoria,
			@RequestParam("latitud") double latitud,
			@RequestParam("longitud") double longitud) throws IOException, ParseException {
		Incidente incidente = new Incidente();
		ServiceResponse serviceResponse = new ServiceResponse();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp timestamp = new Timestamp(dateFormat.parse(fecha_publicacion).getTime());
		incidente.setFecha_publicacion(timestamp);
		incidente.setDescripcion(descripcion);
		incidente.setUbicacion(ubicacion);
		incidente.setImagen(imagen);
		incidente.setTotal_votos(total_votos);
		incidente.setId_estado(id_estado);
		incidente.setDNI(DNI);
		incidente.setId_categoria(id_categoria);
		incidente.setLatitud(latitud);
		incidente.setLongitud(longitud);
		int result = iincidenteService.save(incidente);
		if (result == 1) {
			serviceResponse.setMenssage("El producto se registro correctamente.");

			webSocketService.enviarNotificacion("Nuevo incidente registrado por el usuario con numero DNI " + incidente.getDNI());
			webSocketService.refreshIncidencias(incidente);
		}

		return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
	}

    @PostMapping(value="/update_estado",consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse> update(
			@RequestParam("id_incidencia") int id_incidencia,
			@RequestParam("id_estado") int id_estado)throws IOException{
            Incidente incidente= new Incidente();
		ServiceResponse serviceResponse = new ServiceResponse();
		incidente.setId_incidencia(id_incidencia);
        incidente.setId_estado(id_estado);

		int result= iincidenteService.update_estado(incidente);
		if(result==1) {
			serviceResponse.setMenssage("El incidente se modifico correctamente.");
		}

		return new ResponseEntity<>(serviceResponse,HttpStatus.OK);
	}

    @PostMapping(value="/update_categoria",consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse> update2(
			@RequestParam("id_incidencia") int id_incidencia,
			@RequestParam("id_categoria") int id_categoria)throws IOException{
            Incidente incidente= new Incidente();
		ServiceResponse serviceResponse = new ServiceResponse();
		incidente.setId_incidencia(id_incidencia);
        incidente.setId_categoria(id_categoria);

		int result= iincidenteService.update_categoria(incidente);
		if(result==1) {
			serviceResponse.setMenssage("El incidente se modifico correctamente.");
		}

		return new ResponseEntity<>(serviceResponse,HttpStatus.OK);
	}

	@PutMapping("/delete/{idIncidencia}")
	public ResponseEntity<ServiceResponse> deleteIncidencia(
			@RequestHeader("Authorization") String token,
			@PathVariable int idIncidencia) {

		Incidente incidente = new Incidente();
		ServiceResponse response = new ServiceResponse();

		boolean result = iincidenteService.delete(token, idIncidencia);
		incidente.setId_incidencia(idIncidencia);
		webSocketService.refreshIncidencias(incidente);

		if (result) {
			response.setSuccess(true);
			response.setMenssage("Incidente eliminado correctamente");
		} else {
			response.setSuccess(false);
			response.setMenssage("Incidente no encontrado o no eliminado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/totalVotos/{idIncidencia}")
	public ResponseEntity<Integer> get_total_votos(
		@RequestHeader("Authorization") String token,
		@PathVariable int idIncidencia
		) {

		int num_votos = iincidenteService.get_total_votos(token, idIncidencia);

		if (num_votos > -1) {
			return new ResponseEntity<>(num_votos, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(-1, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/name/usuario/{idIncidencia}")
	public ResponseEntity<String> get_name_usuario(
		@RequestHeader("Authorization") String token,
		@PathVariable int idIncidencia
		) {

		String name_user = iincidenteService.get_name_user(token, idIncidencia);

		if (name_user != null) {
			return new ResponseEntity<>(name_user, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un usuario asociado con la incidencia ID: " + idIncidencia);
		}
	}

	@PutMapping("/updateIncidencia")
    public ResponseEntity<ServiceResponse> updateIncidente(
		@RequestHeader("Authorization") String token,
		@RequestBody Incidente incidente
		) {
		ServiceResponse response = new ServiceResponse();

		boolean updated = iincidenteService.update_incidente(token, incidente);

		if (updated) {
			response.setSuccess(true);
        	response.setMenssage("Incidente actualizado correctamente");
		} else {
			response.setSuccess(false);
        	response.setMenssage("Incidente no encontrado o no actualizado");
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.ok(response);
    }
	@GetMapping("/list/paginated/masVotados")
	public ResponseEntity<Page<infoIncidente>> getIncidentesMasVotados(
        @RequestParam int page,
        @RequestParam int size,
		@RequestParam int id_distrito) {
        try {
            Page<infoIncidente> incidentes = iincidenteService.Listar_incidente_masVotados(size, page, id_distrito);
            if (incidentes.hasContent()) {
                return new ResponseEntity<>(incidentes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
