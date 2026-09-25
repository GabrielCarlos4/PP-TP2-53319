package excepciones;

/**
 * Excepción chequeada que se lanza al cerrar las inscripciones de una
 * actividad cuya cantidad de inscriptos no alcanzó el cupo mínimo
 * requerido para poder dictarse.
 */
public class CupoMinimoNoAlcanzadoException extends Exception {

    public CupoMinimoNoAlcanzadoException(String mensaje) {
        super(mensaje);
    }
}
