package com.Fixura.FixuraBackend.Model;

import lombok.Data;

@Data
public class ApiDniResponse {
  private boolean success;
  private Datos data;

  @Data
  public static class Datos{
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
  }
}
