package com.barberia.appointments_service.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import org.springframework.stereotype.Service;

@Service
public class FirebaseNotificationService {

    public void enviarNotificacion(
            String token,
            String titulo,
            String mensaje
    ) throws Exception {

        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(titulo)
                                .setBody(mensaje)
                                .build()
                )
                .build();

        String respuesta = FirebaseMessaging.getInstance()
                .send(message);

        System.out.println("Notificación enviada: " + respuesta);
    }
}