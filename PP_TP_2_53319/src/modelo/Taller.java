package modelo;

import excepciones.DatosInvalidosException;
import interfaces.Certificable;

/**
 * Actividad de tipo Taller. Es certificable: implementa Certificable
 * y puede emitir certificados de participación a sus inscriptos.
 */
public class Taller extends Actividad implements Certificable {

    private static final long serialVersionUID = 1L;

    public Taller(int id, String nombre, int cupo, int cupoMinimo, double costoMateriales)
            throws DatosInvalidosException {
        super(id, nombre, cupo, cupoMinimo, costoMateriales);
    }

    @Override
    public Certificado emitirCertificado(Estudiante estudiante) {
        return new Certificado(estudiante, this);
    }
}
