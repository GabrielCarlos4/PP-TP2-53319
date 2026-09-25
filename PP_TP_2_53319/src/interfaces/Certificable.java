package interfaces;

import modelo.Certificado;
import modelo.Estudiante;

/**
 * Interfaz que deben implementar las actividades que pueden emitir
 * certificados de participación. En el sistema, Taller y Curso la
 * implementan; Charla no, ya que las charlas no son certificables.
 */
public interface Certificable {

    Certificado emitirCertificado(Estudiante estudiante);
}
