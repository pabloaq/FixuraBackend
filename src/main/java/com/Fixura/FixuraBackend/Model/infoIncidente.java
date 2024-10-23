package com.Fixura.FixuraBackend.Model;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class infoIncidente {
    int id_incidencia;
    Timestamp fecha_publicacion;
    String descripcion;
    String ubicacion;
    String imagen;
    int total_votos;
    String estado;
    String usuario;
    String categoria;
    Double latitud;
    Double longitud;
    boolean tiene_like;
}
