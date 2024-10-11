package com.Fixura.FixuraBackend.Service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.Fixura.FixuraBackend.Model.Usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailVerification {
  
  @Autowired
  private JavaMailSender sender;

  @Autowired
  private TemplateEngine templateEngine;

  public void sendEmailVerification(Usuario user, String subject, String path, String template) throws MessagingException, UnsupportedEncodingException{

    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
    helper.setFrom("noreply@fixura.com", "Fixura");
    helper.setTo(user.getCorreo());
    helper.setSubject(subject);

    String emailContent = loadEmailTemplate(user.getNombre(), generateConfirmationLink(path, user.getToken_verification()), template);
    helper.setText(emailContent, true);

    sender.send(message);
  }

  private String generateConfirmationLink(String path, String token) {
    return "http://localhost:4200/"+path+"?token=" + token;
  }

  private String loadEmailTemplate(String nombre, String link, String template){
    Context context = new Context();
    context.setVariable("nombre", nombre);
    context.setVariable("link", link);

    return templateEngine.process(template, context);
  }
}
