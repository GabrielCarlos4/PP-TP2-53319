package persistencia;

import modelo.EventoUniversitario;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Clase encargada de la persistencia de objetos EventoUniversitario
 * mediante serialización/deserialización de objetos Java. No maneja las
 * excepciones internamente: las declara para que quien la invoque
 * (la clase App) las capture de forma granular y decida cómo informarlas.
 */
public class GestorPersistencia {

    private GestorPersistencia() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Persiste un EventoUniversitario en el archivo indicado.
     *
     * @throws IOException si ocurre un problema de entrada/salida al escribir el archivo
     */
    public static void guardarEvento(EventoUniversitario evento, String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(evento);
        }
    }

    /**
     * Recupera un EventoUniversitario previamente persistido en el archivo indicado.
     *
     * @throws IOException            si ocurre un problema de entrada/salida al leer el archivo
     * @throws ClassNotFoundException si no se encuentra la clase del objeto serializado
     */
    public static EventoUniversitario leerEvento(String rutaArchivo)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (EventoUniversitario) ois.readObject();
        }
    }
}
