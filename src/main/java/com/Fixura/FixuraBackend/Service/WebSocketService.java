package com.Fixura.FixuraBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Incidente;

@Service
public class WebSocketService {

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  public void enviarNotificacion(String mensaje){
    messagingTemplate.convertAndSend("/topic/notifications", mensaje);
  }

  public void refreshIncidencias(Incidente incidente){
    messagingTemplate.convertAndSend("/topic/incidents", incidente);
  }

}
