/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calificaciones;

import java.beans.*;
import java.io.Serializable;
import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipon
 */
public class CalificacionesBean implements Serializable {

    protected String DNI;
    protected String NombreCurso;
    protected String Curso;
    protected Double Nota;

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombreCurso() {
        return NombreCurso;
    }

    public void setNombreCurso(String NombreCurso) {
        this.NombreCurso = NombreCurso;
    }

    public String getCurso() {
        return Curso;
    }

    public void setCurso(String Curso) {
        this.Curso = Curso;
    }

    public Double getNota() {
        return Nota;
    }

    public void setNota(Double Nota) {
        this.Nota = Nota;
    }

    private PropertyChangeSupport propertySupport;

    public CalificacionesBean() {
        propertySupport = new PropertyChangeSupport(this);

        try {
            recargarFilas();
        } catch (ClassNotFoundException ex) {
            this.DNI = "";
            this.NombreCurso = "";
            this.Curso = "";
            this.Nota = 0.0;
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *****************************************************
     * Definimos los métodos y atributos privados del componente que usaremos
     * para darle funcionalidad.
     *
     */
    /**
     * ***************************************************
     * Clase auxiliar que usaremos para crear un vector privado de alumnos.
     */
    private class Calificacion {

        public String DNI;
        public String NombreCurso;
        public String Curso;
        public Double Nota;

        public Calificacion() {  };

        public Calificacion(String DNI, String NombreCurso, String Curso, Double Nota) {
            this.DNI = DNI;
            this.NombreCurso = NombreCurso;
            this.Curso = Curso;
            this.Nota = Nota;
        }
    }

    /**
     * ****************************************************
     * Usaremos un vector auxiliar para cargar la información de la tabla de
     * forma que tengamos acceso a los datos sin necesidad de estar conectados
     * constantemente
     */
    private Vector Calificaciones = new Vector();

    /**
     * *****************************************************
     * Actualiza el contenido de la tabla en el vector de alumnos Las
     * propiedades contienen el valor del primer elementos de la tabla
     */
    private void recargarFilas() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.250/alumnos", "alumnos", "123456");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from calificaciones");
            while (rs.next()) {
                Calificacion c = new Calificacion(rs.getString("DNI"), 
                                                                rs.getString("NombreCurso"), 
                                                                rs.getString("Curso"), 
                                                                rs.getDouble("Nota"));
                Calificaciones.add(c);
            }

            Calificacion c = new Calificacion();

            c = (Calificacion) Calificaciones.elementAt(1);
            this.DNI = c.DNI;
            this.NombreCurso = c.NombreCurso;
            this.Curso = c.Curso;
            this.Nota = c.Nota;
            rs.close();
            con.close();
        } catch (SQLException ex) {
            this.DNI = "";
            this.NombreCurso = "";
            this.Curso = "";
            this.Nota = 0.0;
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ******************************************************
     *
     * @param i numero de la fila a cargar en las propiedades del componente
     */
    public void seleccionarFila(int i) {
        if (i <= Calificaciones.size()) {
            Calificacion c = new Calificacion();
            c = (Calificacion) Calificaciones.elementAt(i);
            this.DNI = c.DNI;
            this.NombreCurso = c.NombreCurso;
            this.Curso = c.Curso;
            this.Nota = c.Nota;
        } else {
            this.DNI = "";
            this.NombreCurso = "";
            this.Curso = "";
            this.Nota = 0.0;
        }
    }

    /**
     * ******************************************************
     *
     * @param DNI DNI A buscar, se carga en las propiedades del componente
     */
    public void seleccionarDNI(String DNI) {
        Calificacion c = new Calificacion();
        int i = 0;

        this.DNI = "";
        this.NombreCurso = "";
        this.Curso = "";
        this.Nota = 0.0;
        
        while (this.DNI.equals("") && i <= Calificaciones.size()) {
            c = (Calificacion) Calificaciones.elementAt(i);
            if (c.DNI.equals(DNI)) {
                this.DNI = c.DNI;
                this.NombreCurso = c.NombreCurso;
                this.Curso = c.Curso;
                this.Nota = c.Nota;
            }
        }
    }

    /**
     * *******************************************************************
     * Código para añadir un nuevo alumno a la base de datos. cada vez que se
     * modifca el estado de la BD se genera un evento para que se recargue el
     * componente.
     */
    private BDModificadaListener receptor;

    public class BDModificadaEvent extends java.util.EventObject {
        // constructor
        public BDModificadaEvent(Object source) {
            super(source);
        }
    }

    //Define la interfaz para el nuevo tipo de evento
    public interface BDModificadaListener extends EventListener {
        public void capturarBDModificada(BDModificadaEvent ev);
    }

    public void addBDModificadaListener(BDModificadaListener receptor) {
        this.receptor = receptor;
    }

    public void removeBDModificadaListener(BDModificadaListener receptor) {
        this.receptor = null;
    }

    /**
     * *****************************************************
     * Método que añade un alumno a la base de datos añade un registro a la base
     * de datos formado a partir de los valores de las propiedades del
     * componente.
     *
     * Se presupone que se han usado los métodos set para configurar
     * adecuadamente las propiedades con los datos del nuevo registro.
     * @throws java.lang.ClassNotFoundException
     */
    public void addCalificacion() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.250/alumnos", "alumnos", "123456");
            PreparedStatement s = con.prepareStatement("insert into calificaciones values (?,?,?,?)");

            s.setString(1, DNI);
            s.setString(2, NombreCurso);
            s.setString(3, Curso);
            s.setDouble(4, Nota);

            s.executeUpdate();
            recargarFilas();
            receptor.capturarBDModificada(new BDModificadaEvent(this));
        } catch (SQLException ex) {
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * *****************************************************
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

}


