package com.barberia.users_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String correoRemitente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoRecuperacion(String destinatario, String enlace) {

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setFrom(correoRemitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Restablecer contraseña - Barbería El Elegante");

        mensaje.setText(
                "Hola.\n\n" +
                        "Has solicitado restablecer tu contraseña.\n\n" +
                        "Ingresa al siguiente enlace para continuar:\n" +
                        enlace + "\n\n" +
                        "Este enlace expirará en 15 minutos.\n\n" +
                        "Si no solicitaste este cambio, ignora este mensaje."
        );

        mailSender.send(mensaje);
    }
}