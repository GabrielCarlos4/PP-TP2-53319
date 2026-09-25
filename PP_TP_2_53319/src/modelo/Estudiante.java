package modelo;

import excepciones.DatosInvalidosException;

import java.io.Serializable;

/**
 * Representa a un estudiante universitario que puede inscribirse
 * a distintas actividades dentro de un evento universitario.
 */
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private String legajo;
    private String nombre;

    public Estudiante(String legajo, String nombre) throws DatosInvalidosException {
        if (legajo == null || legajo.isBlank()) {
            throw new DatosInvalidosException("El legajo del estudiante no puede ser nulo ni vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del estudiante no puede ser nulo ni vacío.");
        }
        this.legajo = legajo;
        this.nombre = nombre;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void mostrarDatos() {
        System.out.printf("  Legajo: %-8s | Nombre: %s%n", legajo, nombre);
    }

    @Override
    public String toString() {
        return nombre + " (Legajo: " + legajo + ")";
    }
}
