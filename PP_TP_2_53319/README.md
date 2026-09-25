# 🎓 TP2 — Sistema de Eventos Universitarios (POO escalado en Java)

| Dato | Detalle                       |
|---|-------------------------------|
| **Materia** | Paradigmas de la Programacion |
| **Alumno** | Ten Andrés                    |
| **Comisión** | 2K7                           |
| **Repositorio** | `PP_TP2_<52142>`              |

---

## 📌 Descripción del proyecto

Este trabajo escala el modelo de eventos universitarios desarrollado
previamente, modularizando el código en **packages** y agregando:

1. **Manejo de excepciones chequeadas** para tolerancia a fallos
   (cupo excedido, cupo mínimo no alcanzado, datos inválidos) y
   **persistencia de objetos mediante serialización** de un
   `EventoUniversitario` a disco.
2. **Interfaces** para modelar qué actividades son certificables
   (`Taller` y `Curso`, pero no `Charla`) y emitir `Certificado`s.
3. **Métodos genéricos acotados y wildcards** en `EventoUniversitario`
   para filtrar actividades por tipo concreto y calcular el costo de
   materiales sobre listas de cualquier subtipo de `Actividad`.
4. **Clases anidadas e hilos**: `TicketDeAcceso` como clase miembro
   (no estática) de `Inscripcion`, y `EnvioTicketsThread` como clase
   independiente del paquete `hilos` que envía los tickets de forma
   concurrente sin bloquear al hilo principal.

---

## 🗂️ Arquitectura del proyecto (packages)

```
src/
├── modelo/
│   ├── Estudiante.java
│   ├── Sala.java                    (agregación con EventoUniversitario)
│   ├── Actividad.java                (abstracta)
│   ├── Charla.java                   (extends Actividad, NO certificable)
│   ├── Taller.java                   (extends Actividad, implements Certificable)
│   ├── Curso.java                    (extends Actividad, implements Certificable)
│   ├── EventoUniversitario.java      (composición con Actividad)
│   ├── Inscripcion.java              (contiene la clase anidada TicketDeAcceso)
│   └── Certificado.java
├── interfaces/
│   └── Certificable.java
├── excepciones/
│   ├── CupoExcedidoException.java
│   ├── CupoMinimoNoAlcanzadoException.java
│   └── DatosInvalidosException.java
├── persistencia/
│   └── GestorPersistencia.java       (serialización/deserialización)
├── hilos/
│   └── EnvioTicketsThread.java       (Runnable, envío concurrente de tickets)
└── app/
    └── App.java                      (main)
```

### Relaciones del modelo

- **Composición**: `EventoUniversitario` ↔ `Actividad` (las actividades no
  existen fuera del evento que las contiene; se crean con
  `evento.crearActividad(...)`).
- **Agregación**: `EventoUniversitario` ↔ `Sala` (la sala existe
  independientemente del evento al que se le asigna).
- **Asociación**: `Actividad` ↔ `Estudiante` a través de `Inscripcion`.
- **Herencia**: `Charla`, `Taller` y `Curso` extienden `Actividad`.
- **Clase anidada miembro**: `Inscripcion.TicketDeAcceso` (no estática,
  requiere la referencia implícita `Inscripcion.this` para saber a qué
  estudiante y actividad pertenece el ticket).

---

## ⚙️ Detalle por ejercicio

### Ejercicio 1 — Excepciones y persistencia
- `Actividad.inscribir(Estudiante)` lanza `CupoExcedidoException`
  (chequeada) cuando ya se alcanzó el cupo máximo.
- `Actividad.cerrarInscripciones()` lanza `CupoMinimoNoAlcanzadoException`
  (chequeada) si no se llegó al cupo mínimo.
- Los constructores del modelo lanzan `DatosInvalidosException`
  (chequeada) ante datos nulos/vacíos/fuera de rango.
- `GestorPersistencia` serializa y deserializa un `EventoUniversitario`
  completo (incluye su sala, actividades e inscripciones).
- En `App` se implementa un flujo `try-catch-finally` que intenta
  inscribir, persistir y leer el evento, capturando de forma granular
  `CupoExcedidoException`, `CupoMinimoNoAlcanzadoException`,
  `FileNotFoundException`, `IOException` y `ClassNotFoundException`,
  cada una con su mensaje propio.

### Ejercicio 2 — Interfaces y certificados
- `Certificable` declara `emitirCertificado(Estudiante)`.
- La implementan `Taller` y `Curso`; `Charla` **no** la implementa.
- En `App` se recorren las actividades del evento y, mediante
  `instanceof Certificable`, se emiten certificados solo a los
  inscriptos de actividades certificables.

### Ejercicio 3 — Genéricos acotados y wildcards
- `public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo)`
  devuelve listas correctamente tipadas (`List<Charla>`, `List<Taller>`,
  `List<Curso>`).
- `public double calcularCostoMateriales(List<? extends Actividad> actividades)`
  opera sobre cualquier lista de `Actividad` o subtipos.

### Ejercicio 4 — Clases anidadas e hilos
- `Inscripcion.TicketDeAcceso` es una clase miembro (no estática): un
  ticket solo tiene sentido dentro del contexto de una inscripción
  confirmada concreta.
- `EnvioTicketsThread` (paquete `hilos`) implementa `Runnable` y envía,
  con una demora simulada, todos los tickets de las inscripciones
  confirmadas.
- En `App`, el hilo principal inicia `EnvioTicketsThread` en un hilo
  separado y continúa mostrando los datos del evento, sus actividades e
  inscriptos mientras el envío ocurre en paralelo — evidenciando en
  consola los nombres de ambos hilos.

---

## ▶️ Cómo ejecutar

**Desde línea de comandos** (parado en la carpeta `src`):
```bash
cd src
javac app/App.java modelo/*.java interfaces/*.java excepciones/*.java persistencia/*.java hilos/*.java
java app.App
```

**Desde IntelliJ IDEA**: abrir el proyecto (`File > Open`), marcar `src`
como *Sources Root* si no lo detecta automáticamente, y ejecutar
`app.App` (clic derecho → `Run 'App.main()'`).

Al ejecutarse, el programa genera un archivo `evento1.dat` en el
directorio de trabajo (evidencia de la persistencia por serialización).

---

## 🖥️ Consola (salida esperada, resumida)

> ⚠️ Los códigos de ticket y el orden exacto de algunas líneas del hilo
> de envío pueden variar levemente entre ejecuciones — es normal en un
> programa concurrente. Reemplazar este bloque por tu captura real.

```
==================================================
   TP2 - SISTEMA DE EVENTOS UNIVERSITARIOS (escalado)
==================================================

----- Validación de datos -----
⚠ No se pudo crear el estudiante: El legajo del estudiante no puede ser nulo ni vacío.

----- EJERCICIO 1: Inscripciones con control de cupo -----
✔ Inscripciones a la Charla realizadas con éxito (cupo 2/2).
⚠ No se pudo inscribir en la Charla: Cupo excedido en "Electrónica básica" (cupo máximo: 2).
Proceso de inscripción a la Charla finalizado.
✔ Inscripciones al Taller realizadas con éxito.
Proceso de inscripción al Taller finalizado.
✔ Inscripciones al Curso realizadas con éxito.
Proceso de inscripción al Curso finalizado.
⚠ No se pudo cerrar inscripciones: "Taller de repaso" no alcanzó el cupo mínimo (3). Inscriptos actuales: 1.
Proceso de cierre del Taller de repaso finalizado.

----- Persistencia del evento -----
✔ Evento persistido correctamente en "evento1.dat".
✔ Evento recuperado desde archivo:
  Comisión: 2K7
  ...
Proceso de persistencia finalizado.

----- EJERCICIO 2: Emisión de certificados -----
Certificados emitidos: 5 (solo Talleres y Cursos; las Charlas no son certificables)
  🏅 Certificado (Taller) - Matemática introductoria | Estudiante: Pablo | ...
  ...

----- EJERCICIO 3: Filtrado por tipo y costo de materiales -----
Cantidad de Charlas: 1
Cantidad de Talleres: 2
Cantidad de Cursos: 1
Costo de materiales - Charlas: $500.00
Costo de materiales - Talleres: $2000.00
Costo de materiales - Cursos: $2000.00

----- EJERCICIO 4: Tickets de acceso concurrentes -----
Inscripciones confirmadas con ticket emitido: 4
  🎫 Ticket TCK-101-... | Estudiante: Pablo | Actividad: Electrónica básica | ...
  ...

[main] Continúo mostrando información del evento mientras el otro hilo envía los tickets:
  ...
  [Hilo-EnvioTickets] Iniciando envío de 4 ticket(s) de acceso...
  [Hilo-EnvioTickets] Enviado ticket TCK-101-... a Pablo
  ...
[main] Envío de tickets finalizado. Se evidenciaron dos hilos de ejecución distintos.

==================================================
Total de eventos creados en el sistema: 1
==================================================
```

> 📷 **[ Acá va tu captura real de la salida completa de consola ]**

---

## 📦 Pautas de entrega

1. Crear un repositorio en **GitHub**, de acceso **público**, llamado
   `PP_TP2_<tu_legajo>` (ej: `https://github.com/tu_usuario/PP_TP2_50268`).
2. El repositorio debe contener:
   - El proyecto de código completo (hasta el Ejercicio 4), generado
     desde **IntelliJ IDEA**, listo para clonar y ejecutar.
   - Este `README.md` con la documentación del proyecto.
   - Una **captura de pantalla** de la salida por consola de una
     ejecución completa del programa.
3. Consignar en la entrega la **URL de clonado vía https** del
   repositorio, en el enlace "Entregar Trabajo Práctico N.º 2".
