package modelo;

import excepciones.CupoExcedidoException;
import excepciones.CupoMinimoNoAlcanzadoException;
import excepciones.DatosInvalidosException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa una actividad dentro de un
 * EventoUniversitario. Sus subclases concretas son Charla, Taller y Curso.
 * La relación entre EventoUniversitario y Actividad es de COMPOSICIÓN:
 * las actividades no tienen sentido de existir fuera del evento que
 * las contiene.
 */
public abstract class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private int cupo;
    private int cupoMinimo;
    private double costoMateriales;
    private List<Inscripcion> inscripciones;

    protected Actividad(int id, String nombre, int cupo, int cupoMinimo, double costoMateriales)
            throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre de la actividad no puede ser nulo ni vacío.");
        }
        if (cupo <= 0) {
            throw new DatosInvalidosException("El cupo de la actividad debe ser mayor a 0.");
        }
        if (cupoMinimo < 0 || cupoMinimo > cupo) {
            throw new DatosInvalidosException("El cupo mínimo debe estar entre 0 y el cupo máximo.");
        }
        if (costoMateriales < 0) {
            throw new DatosInvalidosException("El costo de materiales no puede ser negativo.");
        }
        this.id = id;
        this.nombre = nombre;
        this.cupo = cupo;
        this.cupoMinimo = cupoMinimo;
        this.costoMateriales = costoMateriales;
        this.inscripciones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCupo() {
        return cupo;
    }

    public int getCupoMinimo() {
        return cupoMinimo;
    }

    public double getCostoMateriales() {
        return costoMateriales;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Devuelve un nombre legible para el tipo concreto de actividad
     * (Charla, Taller, Curso), usado solo para mostrar datos por consola.
     */
    public String getTipo() {
        return getClass().getSimpleName();
    }

    /**
     * Inscribe a un estudiante en la actividad, generando el objeto
     * Inscripcion correspondiente. Lanza CupoExcedidoException (excepción
     * chequeada) si ya se alcanzó el cupo máximo.
     */
    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupo) {
            throw new CupoExcedidoException(
                    "Cupo excedido en \"" + nombre + "\" (cupo máximo: " + cupo + ").");
        }
        Inscripcion inscripcion = new Inscripcion(estudiante, this);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    /**
     * Cierra las inscripciones de la actividad, validando que se haya
     * alcanzado el cupo mínimo requerido. Lanza CupoMinimoNoAlcanzadoException
     * (excepción chequeada) en caso contrario.
     */
    public void cerrarInscripciones() throws CupoMinimoNoAlcanzadoException {
        if (inscripciones.size() < cupoMinimo) {
            throw new CupoMinimoNoAlcanzadoException(
                    "\"" + nombre + "\" no alcanzó el cupo mínimo (" + cupoMinimo
                            + "). Inscriptos actuales: " + inscripciones.size() + ".");
        }
    }

    public void mostrarDatos() {
        System.out.printf("  [%s] %s (ID: %d | Cupo: %d | CupoMín: %d | CostoMat: $%.2f | Inscriptos: %d)%n",
                getTipo(), nombre, id, cupo, cupoMinimo, costoMateriales, inscripciones.size());
    }
}
