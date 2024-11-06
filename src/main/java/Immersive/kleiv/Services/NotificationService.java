package Immersive.kleiv.Services;

import Immersive.kleiv.Entity.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class NotificationService {

    private JavaMailSender mailSender;
     public void sendNotification(Validation validation) {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom("Klei@Contact.cm");
         message.setTo(validation.getUser().getEmail());
         message.setSubject("Votre code d'activation");
         String content = String.format("Bonjour %s, bienvenue sur notre plateforme votre code est %s", validation.getUser().getName(), validation.getCode());
         message.setText(content);
         mailSender.send(message);
     }
}
