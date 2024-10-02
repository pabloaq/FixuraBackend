package com.Fixura.FixuraBackend.Model;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class IncidenciaLike {
    private String dni;
    private int id_incidencia;
    private Timestamp hour_liked;
}
