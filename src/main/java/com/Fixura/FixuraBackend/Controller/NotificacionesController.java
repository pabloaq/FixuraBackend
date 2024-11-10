package com.Fixura.FixuraBackend.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.Fixura.FixuraBackend.Model.Incidente;
import com.Fixura.FixuraBackend.Service.WebSocketService;

@Controller
public class NotificacionesController {
  /* private final WebSocketService webSocketService;

  public NotificacionesController(WebSocketService webSocketService){
    this.webSocketService = webSocketService;
  } */

  @MessageMapping("/incidente")
  @SendTo("/topic/incidentes")
  public Incidente sendNotificacion(Incidente incidente){
    //webSocketService.enviarNotificacion(message);
    return incidente;
  }
}
