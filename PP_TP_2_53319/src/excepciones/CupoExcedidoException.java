package excepciones;

/**
 * Excepción chequeada que se lanza cuando se intenta inscribir a un
 * estudiante en una actividad que ya alcanzó su cupo máximo de inscriptos.
 */
public class CupoExcedidoException extends Exception {

    public CupoExcedidoException(String mensaje) {
        super(mensaje);
    }
}
