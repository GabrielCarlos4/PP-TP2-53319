package modelo;

import excepciones.DatosInvalidosException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un evento universitario (ej: jornada, ciclo de charlas)
 * dictado para una comisión, con una temática, un costo asociado, una
 * sala asignada (AGREGACIÓN) y una o más actividades (COMPOSICIÓN).
 */
public class EventoUniversitario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String comision;
    private String tematica;
    private double monto;
    private boolean gratuito;
    private Sala sala;
    private List<Actividad> actividades;

    // Atributo de clase: cuenta la cantidad de eventos creados en el sistema.
    private static int cantidadEventos = 0;

    public EventoUniversitario(String comision, String tematica, double monto, boolean gratuito)
            throws DatosInvalidosException {
        if (comision == null || comision.isBlank()) {
            throw new DatosInvalidosException("La comisión del evento no puede ser nula ni vacía.");
        }
        if (tematica == null || tematica.isBlank()) {
            throw new DatosInvalidosException("La temática del evento no puede ser nula ni vacía.");
        }
        if (monto < 0) {
            throw new DatosInvalidosException("El monto del evento no puede ser negativo.");
        }
        this.comision = comision;
        this.tematica = tematica;
        this.monto = monto;
        this.gratuito = gratuito;
        this.actividades = new ArrayList<>();
        cantidadEventos++;
    }

    public String getComision() {
        return comision;
    }

    public String getTematica() {
        return tematica;
    }

    public double getMonto() {
        return monto;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public Sala getSala() {
        return sala;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    /**
     * Asigna la sala física donde se desarrollará el evento (agregación).
     */
    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    /**
     * Crea una nueva actividad del tipo indicado ("Charla", "Taller" o
     * "Curso") y la agrega a la lista de actividades del evento
     * (composición). Actúa como fábrica simple según el parámetro tipo.
     */
    public Actividad crearActividad(int id, String nombre, int cupo, int cupoMinimo,
                                     double costoMateriales, String tipo) throws DatosInvalidosException {
        Actividad actividad;
        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de actividad no puede ser nulo.");
        }
        switch (tipo) {
            case "Charla":
                actividad = new Charla(id, nombre, cupo, cupoMinimo, costoMateriales);
                break;
            case "Taller":
                actividad = new Taller(id, nombre, cupo, cupoMinimo, costoMateriales);
                break;
            case "Curso":
                actividad = new Curso(id, nombre, cupo, cupoMinimo, costoMateriales);
                break;
            default:
                throw new DatosInvalidosException("Tipo de actividad inválido: " + tipo);
        }
        actividades.add(actividad);
        return actividad;
    }

    /**
     * Filtra las actividades del evento por un tipo concreto, utilizando
     * un método parametrizado acotado: T extends Actividad. Devuelve una
     * lista correctamente tipada (por ejemplo, List&lt;Taller&gt;).
     */
    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }

    /**
     * Calcula el costo total de materiales de una lista de actividades.
     * Utiliza un wildcard acotado (? extends Actividad) para poder operar
     * tanto sobre List&lt;Actividad&gt; como sobre List&lt;Charla&gt;,
     * List&lt;Taller&gt; o List&lt;Curso&gt; indistintamente.
     */
    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        double total = 0;
        for (Actividad actividad : actividades) {
            total += actividad.getCostoMateriales();
        }
        return total;
    }

    public void mostrarDatos() {
        System.out.println("  Comisión: " + comision);
        System.out.println("  Temática: " + tematica);
        System.out.println("  Costo: " + (gratuito ? "Gratuito" : "$" + monto));
        if (sala != null) {
            System.out.println("  Sala asignada: " + sala.getNombre() + " (N° " + sala.getNumero() + ")");
        } else {
            System.out.println("  Sala asignada: (sin asignar)");
        }
        System.out.println("  Cantidad de actividades: " + actividades.size());
    }
}
