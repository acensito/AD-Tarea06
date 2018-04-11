
package Calificar;

import calificaciones.CalificacionesBean;
import calificaciones.CalificacionesBean.BDModificadaEvent;
import calificaciones.CalificacionesBean.BDModificadaListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase AccedeDB. Implementa la interfaz BDModificadaListener
 * 
 * @author Felipon
 */
public class AccedeDB implements BDModificadaListener {

    //instanciamos el componente
    CalificacionesBean calificaciones;

    AccedeDB() {
        //indicamos que se añada un oyente de eventos
        calificaciones = new CalificacionesBean();
        calificaciones.addBDModificadaListener((BDModificadaListener) this);
    }

    /**
     * Método listado(), muestra el listado de calificaciones
     */
    public void listado() {
        //Bucle que comienza a obtener valores dependiendo del tamaño filas
        for (int i = 0; i < calificaciones.getTamanio(); i++) {
            calificaciones.seleccionarFila(i);
            System.out.println("DNI: " + calificaciones.getDNI());
            System.out.println("\tAsignatura: " + calificaciones.getNombreCurso());
            System.out.println("\tCurso: " + calificaciones.getCurso());
            System.out.println("\tNota: " + calificaciones.getNota());
        }
    }

    /**
     * Método anade(), que establece en el objeto estos datos y procede a
     * insertarlos en la BD.
     */
    public void anade() {
        calificaciones.setDNI("98765432A");
        calificaciones.setNombreCurso("Cria del mamut");
        calificaciones.setCurso("15-16");
        calificaciones.setNota(5.6);
        try {
            calificaciones.agregarNotaModulo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccedeDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método modifica(), que seleccionamos un DNI de existente en la BD y procede
     * a modificar su Nota.
     */
    public void modifica() {
        //Seleccionamos el alumno que queremos modificar
        calificaciones.seleccionarDNI("98765432A");
        //Modificamos la nota
        calificaciones.setNota(8.2);
        try {
            //Guardamos los cambios
            calificaciones.modificarNotaModulo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccedeDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    /**
     * Metodo sobreescrito que lanza mensaje al recibir un evento
     */
    public void capturarBDModificada(BDModificadaEvent ev) {
        System.out.println("Se ha añadido una nota nueva");
    }

    @Override
    /**
     * Metodo sobreescrito que lanza mensaje al recibir un evento
     */
    public void capturarNotaBDmodificada(BDModificadaEvent ev) {
        System.out.println("NOTA MODIFICADA");
    }

}
