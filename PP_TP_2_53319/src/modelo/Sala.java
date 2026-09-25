package modelo;

import excepciones.DatosInvalidosException;

import java.io.Serializable;

/**
 * Representa una sala o aula física donde puede desarrollarse un evento
 * universitario. Su relación con EventoUniversitario es de AGREGACIÓN:
 * la sala existe independientemente del evento al que esté asignada.
 */
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numero;
    private String nombre;

    public Sala(int numero, String nombre) throws DatosInvalidosException {
        if (numero <= 0) {
            throw new DatosInvalidosException("El número de sala debe ser mayor a 0.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre de la sala no puede ser nulo ni vacío.");
        }
        this.numero = numero;
        this.nombre = nombre;
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void mostrarDatos() {
        System.out.printf("  Sala N° %d - %s%n", numero, nombre);
    }

    @Override
    public String toString() {
        return "Sala N° " + numero + " - " + nombre;
    }
}
