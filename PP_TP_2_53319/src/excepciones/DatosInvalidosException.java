package excepciones;

/**
 * Excepción chequeada que se lanza cuando se intenta construir un objeto
 * del modelo (Estudiante, Sala, EventoUniversitario, Actividad, etc.)
 * con datos inválidos (nulos, vacíos o fuera de rango).
 */
public class DatosInvalidosException extends Exception {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
