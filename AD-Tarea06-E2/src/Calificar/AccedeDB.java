/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Calificar;

import calificaciones.CalificacionesBean;
import calificaciones.CalificacionesBean.BDModificadaEvent;
import calificaciones.CalificacionesBean.BDModificadaListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipon
 */
public class AccedeDB implements BDModificadaListener {

    CalificacionesBean calificaciones;

    AccedeDB() {
        calificaciones = new CalificacionesBean();

        calificaciones.addBDModificadaListener((BDModificadaListener) this);
    }

    public void listado() {
        for (int i = 0; i < 3; i++) {
            calificaciones.seleccionarFila(i);
            System.out.println("DNI: " + calificaciones.getDNI());
            System.out.println("\tAsignatura: " + calificaciones.getNombreCurso());
            System.out.println("\tCurso: " + calificaciones.getCurso());
            System.out.println("\tNota: " + calificaciones.getNota());
        }
    }

    void anade() {
        calificaciones.setDNI("98765432A");
        calificaciones.setNombreCurso("Cria del mamut");
        calificaciones.setCurso("3AC");
        calificaciones.setNota(5.6);
        try {
            calificaciones.addCalificacion();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccedeDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void capturarBDModificada(BDModificadaEvent ev) {
        System.out.println("Se ha añadido un elemento a la base de datos");
    }

}
