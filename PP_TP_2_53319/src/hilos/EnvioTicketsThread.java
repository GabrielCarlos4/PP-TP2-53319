package hilos;

import modelo.Inscripcion;

import java.util.List;

/**
 * Clase pública e independiente (no anidada) ubicada en el paquete hilos,
 * encargada de ejecutar en un hilo separado el proceso de envío de todos
 * los tickets de acceso generados para las inscripciones confirmadas de
 * un evento, sin interrumpir la ejecución del hilo principal.
 */
public class EnvioTicketsThread implements Runnable {

    private List<Inscripcion> inscripcionesConfirmadas;

    public EnvioTicketsThread(List<Inscripcion> inscripcionesConfirmadas) {
        this.inscripcionesConfirmadas = inscripcionesConfirmadas;
    }

    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();
        System.out.println("  [" + nombreHilo + "] Iniciando envío de " + inscripcionesConfirmadas.size()
                + " ticket(s) de acceso...");
        for (Inscripcion inscripcion : inscripcionesConfirmadas) {
            try {
                Thread.sleep(400); // simula latencia de envío (ej: correo, notificación push, etc.)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("  [" + nombreHilo + "] Envío interrumpido.");
                return;
            }
            if (inscripcion.getTicket() != null) {
                System.out.println("  [" + nombreHilo + "] Enviado ticket " + inscripcion.getTicket().getCodigo()
                        + " a " + inscripcion.getEstudiante().getNombre());
            }
        }
        System.out.println("  [" + nombreHilo + "] Envío de tickets finalizado.");
    }
}
