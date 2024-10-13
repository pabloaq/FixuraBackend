package com.Fixura.FixuraBackend.Model;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Incidente {
    int id_incidencia;
    Timestamp fecha_publicacion;
    String descripcion;
    String ubicacion;
    String imagen;
    int total_votos;
    int id_estado;
    String DNI;
    int id_categoria;
    double latitud;
    double longitud;
}
