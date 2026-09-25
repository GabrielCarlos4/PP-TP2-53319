package app;

import excepciones.CupoExcedidoException;
import excepciones.CupoMinimoNoAlcanzadoException;
import excepciones.DatosInvalidosException;
import hilos.EnvioTicketsThread;
import interfaces.Certificable;
import modelo.Actividad;
import modelo.Certificado;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.Charla;
import modelo.Curso;
import modelo.Taller;
import persistencia.GestorPersistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("   TP2 - SISTEMA DE EVENTOS UNIVERSITARIOS (escalado)");
        System.out.println("==================================================");

        // ---- Demostración: datos inválidos controlados (fuera del flujo principal) ----
        System.out.println("\n----- Validación de datos -----");
        try {
            Estudiante invalido = new Estudiante("", null);
        } catch (DatosInvalidosException ex) {
            System.out.println("⚠ No se pudo crear el estudiante: " + ex.getMessage());
        }

        try {
            // ---------- Creación de estudiantes ----------
            Estudiante e1 = new Estudiante("60318", "Lucía");
            Estudiante e2 = new Estudiante("59904", "Martina");
            Estudiante e3 = new Estudiante("61027", "Gonzalo");

            // ---------- Creación de evento, sala y actividades ----------
            EventoUniversitario evento1 = new EventoUniversitario("2K7", "Bases de Datos II", 2200, false);
            Sala sala1 = new Sala(5, "LIB");
            evento1.asignarSala(sala1);

            Actividad charla = evento1.crearActividad(201, "Introducción a Redes", 2, 1, 500, "Charla");
            Actividad taller = evento1.crearActividad(202, "Programación en Python", 2, 1, 1200, "Taller");
            Actividad curso = evento1.crearActividad(103, "Curso de POO avanzada", 3, 1, 2000, "Curso");
            Actividad tallerExtra = evento1.crearActividad(104, "Taller de repaso", 5, 3, 800, "Taller");

            List<Inscripcion> todasLasInscripciones = new ArrayList<>();

            // ================= EJERCICIO 1: excepciones + persistencia =================
            System.out.println("\n----- EJERCICIO 1: Inscripciones con control de cupo -----");

            try {
                todasLasInscripciones.add(charla.inscribir(e1));
                todasLasInscripciones.add(charla.inscribir(e2));
                System.out.println("✔ Inscripciones a la Charla realizadas con éxito (cupo 2/2).");
                todasLasInscripciones.add(charla.inscribir(e3)); // excede el cupo -> excepción
            } catch (CupoExcedidoException ex) {
                System.out.println("⚠ No se pudo inscribir en la Charla: " + ex.getMessage());
            } finally {
                System.out.println("Proceso de inscripción a la Charla finalizado.");
            }

            try {
                todasLasInscripciones.add(taller.inscribir(e1));
                todasLasInscripciones.add(taller.inscribir(e3));
                System.out.println("✔ Inscripciones al Taller realizadas con éxito.");
            } catch (CupoExcedidoException ex) {
                System.out.println("⚠ No se pudo inscribir en el Taller: " + ex.getMessage());
            } finally {
                System.out.println("Proceso de inscripción al Taller finalizado.");
            }

            try {
                todasLasInscripciones.add(curso.inscribir(e2));
                todasLasInscripciones.add(curso.inscribir(e3));
                System.out.println("✔ Inscripciones al Curso realizadas con éxito.");
            } catch (CupoExcedidoException ex) {
                System.out.println("⚠ No se pudo inscribir en el Curso: " + ex.getMessage());
            } finally {
                System.out.println("Proceso de inscripción al Curso finalizado.");
            }

            // Caso deliberado de cupo mínimo NO alcanzado (Taller de repaso: cupoMin 3, solo 1 inscripto)
            try {
                todasLasInscripciones.add(tallerExtra.inscribir(e1));
                tallerExtra.cerrarInscripciones();
                System.out.println("✔ Taller de repaso alcanzó el cupo mínimo.");
            } catch (CupoExcedidoException ex) {
                System.out.println("⚠ No se pudo inscribir en el Taller de repaso: " + ex.getMessage());
            } catch (CupoMinimoNoAlcanzadoException ex) {
                System.out.println("⚠ No se pudo cerrar inscripciones: " + ex.getMessage());
            } finally {
                System.out.println("Proceso de cierre del Taller de repaso finalizado.");
            }

            // ---------- Persistencia mediante serialización de objetos ----------
            System.out.println("\n----- Persistencia del evento -----");
            String archivo = "evento1.dat";
            try {
                GestorPersistencia.guardarEvento(evento1, archivo);
                System.out.println("✔ Evento persistido correctamente en \"" + archivo + "\".");

                EventoUniversitario eventoRecuperado = GestorPersistencia.leerEvento(archivo);
                System.out.println("✔ Evento recuperado desde archivo:");
                eventoRecuperado.mostrarDatos();
            } catch (FileNotFoundException ex) {
                System.out.println("⚠ Archivo de persistencia no encontrado: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("⚠ Error de entrada/salida al persistir el evento: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("⚠ Error al reconstruir el objeto persistido: " + ex.getMessage());
            } finally {
                System.out.println("Proceso de persistencia finalizado.");
            }

            // ================= EJERCICIO 2: certificados mediante interfaces =================
            System.out.println("\n----- EJERCICIO 2: Emisión de certificados -----");
            List<Certificado> certificados = new ArrayList<>();
            for (Actividad actividad : evento1.getActividades()) {
                if (actividad instanceof Certificable certificable) {
                    for (Inscripcion inscripcion : actividad.getInscripciones()) {
                        certificados.add(certificable.emitirCertificado(inscripcion.getEstudiante()));
                    }
                }
            }
            System.out.println("Certificados emitidos: " + certificados.size()
                    + " (solo Talleres y Cursos; las Charlas no son certificables)");
            for (Certificado certificado : certificados) {
                certificado.mostrarDatos();
            }

            // ================= EJERCICIO 3: genéricos acotados y wildcards =================
            System.out.println("\n----- EJERCICIO 3: Filtrado por tipo y costo de materiales -----");
            List<Charla> charlas = evento1.filtrarActividadesPorTipo(Charla.class);
            List<Taller> talleres = evento1.filtrarActividadesPorTipo(Taller.class);
            List<Curso> cursos = evento1.filtrarActividadesPorTipo(Curso.class);

            System.out.println("Cantidad de Charlas: " + charlas.size());
            System.out.println("Cantidad de Talleres: " + talleres.size());
            System.out.println("Cantidad de Cursos: " + cursos.size());

            double costoCharlas = evento1.calcularCostoMateriales(charlas);
            double costoTalleres = evento1.calcularCostoMateriales(talleres);
            double costoCursos = evento1.calcularCostoMateriales(cursos);
            System.out.printf("Costo de materiales - Charlas: $%.2f%n", costoCharlas);
            System.out.printf("Costo de materiales - Talleres: $%.2f%n", costoTalleres);
            System.out.printf("Costo de materiales - Cursos: $%.2f%n", costoCursos);

            // ================= EJERCICIO 4: hilos y clase anidada TicketDeAcceso =================
            System.out.println("\n----- EJERCICIO 4: Tickets de acceso concurrentes -----");

            // Se confirman algunas inscripciones
            for (int i = 0; i < todasLasInscripciones.size(); i++) {
                if (i % 2 == 0) { // confirma alternadamente, a modo de ejemplo
                    todasLasInscripciones.get(i).confirmar();
                }
            }

            List<Inscripcion> confirmadas = new ArrayList<>();
            for (Inscripcion inscripcion : todasLasInscripciones) {
                if (inscripcion.isConfirmada()) {
                    inscripcion.emitirTicket();
                    confirmadas.add(inscripcion);
                }
            }
            System.out.println("Inscripciones confirmadas con ticket emitido: " + confirmadas.size());
            for (Inscripcion inscripcion : confirmadas) {
                inscripcion.getTicket().mostrarTicket();
            }

            Thread hiloEnvio = new Thread(new EnvioTicketsThread(confirmadas), "Hilo-EnvioTickets");
            hiloEnvio.start();

            System.out.println("\n[" + Thread.currentThread().getName()
                    + "] Continúo mostrando información del evento mientras el otro hilo envía los tickets:");
            evento1.mostrarDatos();
            for (Actividad actividad : evento1.getActividades()) {
                actividad.mostrarDatos();
                for (Inscripcion inscripcion : actividad.getInscripciones()) {
                    System.out.println("    -> Inscripto: " + inscripcion.getEstudiante());
                }
            }

            try {
                hiloEnvio.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.out.println("El hilo principal fue interrumpido esperando el envío de tickets.");
            }

            System.out.println("[" + Thread.currentThread().getName()
                    + "] Envío de tickets finalizado. Se evidenciaron dos hilos de ejecución distintos.");

        } catch (DatosInvalidosException ex) {
            System.out.println("⚠ Error de datos inválidos: " + ex.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println("Total de eventos creados en el sistema: " + EventoUniversitario.getCantidadEventos());
        System.out.println("==================================================");
    }
}
