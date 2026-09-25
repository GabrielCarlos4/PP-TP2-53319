package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa un certificado de participación emitido a un estudiante
 * por haber participado de una actividad certificable (Taller o Curso).
 */
public class Certificado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Estudiante estudiante;
    private String nombreActividad;
    private String tipoActividad;
    private Date fechaEmision;

    public Certificado(Estudiante estudiante, Actividad actividad) {
        this.estudiante = estudiante;
        this.nombreActividad = actividad.getNombre();
        this.tipoActividad = actividad.getTipo();
        this.fechaEmision = new Date();
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void mostrarDatos() {
        System.out.println("  🏅 Certificado (" + tipoActividad + ") - " + nombreActividad
                + " | Estudiante: " + estudiante.getNombre() + " | Emitido: " + fechaEmision);
    }
}
