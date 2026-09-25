package modelo;

import excepciones.DatosInvalidosException;

/**
 * Actividad de tipo Charla. No es certificable: no implementa
 * la interfaz Certificable.
 */
public class Charla extends Actividad {

    private static final long serialVersionUID = 1L;

    public Charla(int id, String nombre, int cupo, int cupoMinimo, double costoMateriales)
            throws DatosInvalidosException {
        super(id, nombre, cupo, cupoMinimo, costoMateriales);
    }
}
