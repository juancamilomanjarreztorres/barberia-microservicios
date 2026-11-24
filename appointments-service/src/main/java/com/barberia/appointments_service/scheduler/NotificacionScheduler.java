package com.barberia.appointments_service.scheduler;

import com.barberia.appointments_service.entity.Cita;
import com.barberia.appointments_service.repository.CitaRepository;
import com.barberia.appointments_service.service.DispositivoNotificacionService;
import com.barberia.appointments_service.service.FirebaseNotificationService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificacionScheduler {

    private final CitaRepository citaRepository;
    private final DispositivoNotificacionService dispositivoService;
    private final FirebaseNotificationService firebaseService;

    public NotificacionScheduler(
            CitaRepository citaRepository,
            DispositivoNotificacionService dispositivoService,
            FirebaseNotificationService firebaseService
    ) {
        this.citaRepository = citaRepository;
        this.dispositivoService = dispositivoService;
        this.firebaseService = firebaseService;
    }

    @Scheduled(fixedDelay = 30000)
    public void revisarCitasPendientes() {

        LocalDateTime ahora = LocalDateTime.now();

        List<Cita> citas = citaRepository
                .findByEstadoInAndNotificacionProgramadaFalseAndHoraSalidaRecomendadaLessThanEqual(
                        List.of("Pendiente", "Confirmada"),
                        ahora
                );

        for (Cita cita : citas) {

            List<String> tokens = dispositivoService
                    .obtenerTokensCliente(cita.getIdCliente());

            if (tokens.isEmpty()) {
                System.out.println(
                        "El cliente " + cita.getIdCliente()
                                + " no tiene dispositivos registrados."
                );
                continue;
            }

            boolean enviada = false;

            for (String token : tokens) {
                try {
                    firebaseService.enviarNotificacion(
                            token,
                            "¡Es hora de salir!",
                            "Tienes una cita mañana o próximamente. "
                                    + "Tu hora recomendada de salida es "
                                    + cita.getHoraSalidaRecomendada().toLocalTime()
                                    + ". ¡Te esperamos!"
                    );

                    enviada = true;

                } catch (Exception e) {
                    System.err.println(
                            "Error enviando notificación al dispositivo: "
                                    + e.getMessage()
                    );
                }
            }

            if (enviada) {
                cita.setNotificacionProgramada(true);
                citaRepository.save(cita);

                System.out.println(
                        "Notificación procesada para la cita: "
                                + cita.getIdCita()
                );
            }
        }
    }
}