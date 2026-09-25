package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa la inscripción de un Estudiante en una Actividad concreta.
 * Vincula ambos objetos mediante referencias y administra su estado de
 * confirmación y el ticket de acceso asociado.
 */
public class Inscripcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Estudiante estudiante;
    private Actividad actividad;
    private boolean confirmada;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante, Actividad actividad) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.confirmada = false;
        this.ticket = null;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void confirmar() {
        this.confirmada = true;
    }

    public TicketDeAcceso getTicket() {
        return ticket;
    }

    /**
     * Genera el ticket de acceso para esta inscripción. Solo puede
     * emitirse si la inscripción se encuentra confirmada.
     */
    public TicketDeAcceso emitirTicket() {
        if (!confirmada) {
            throw new IllegalStateException(
                    "No se puede emitir un ticket para una inscripción no confirmada.");
        }
        if (ticket == null) {
            ticket = new TicketDeAcceso();
        }
        return ticket;
    }

    public void mostrarDatos() {
        System.out.println("  Inscripción: " + estudiante.getNombre() + " -> " + actividad.getNombre()
                + " | Confirmada: " + (confirmada ? "Sí" : "No")
                + " | Ticket: " + (ticket != null ? ticket.getCodigo() : "sin emitir"));
    }

    /**
     * Clase anidada MIEMBRO (no estática) de Inscripcion: un ticket de
     * acceso solo tiene sentido en el contexto de una inscripción concreta,
     * por eso necesita la referencia implícita a la instancia externa
     * (Inscripcion.this) para poder identificar de qué estudiante y
     * actividad se trata.
     */
    public class TicketDeAcceso implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String codigo;
        private final Date fechaEmision;

        private TicketDeAcceso() {
            // Usa la referencia implícita a la Inscripcion externa
            this.codigo = "TCK-" + Inscripcion.this.actividad.getId() + "-"
                    + Inscripcion.this.estudiante.getLegajo() + "-" + System.nanoTime();
            this.fechaEmision = new Date();
        }

        public String getCodigo() {
            return codigo;
        }

        public Date getFechaEmision() {
            return fechaEmision;
        }

        public void mostrarTicket() {
            System.out.println("  🎫 Ticket " + codigo + " | Estudiante: "
                    + Inscripcion.this.estudiante.getNombre() + " | Actividad: "
                    + Inscripcion.this.actividad.getNombre() + " | Emitido: " + fechaEmision);
        }
    }
}
