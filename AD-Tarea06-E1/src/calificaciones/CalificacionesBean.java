
package calificaciones;

import java.beans.*;
import java.io.Serializable;
import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase CalificacionesBean
 * 
 * @author Felipon
 */
public class CalificacionesBean implements Serializable {
    //ATRIBUTOS
    protected String DNI;
    protected String NombreCurso;
    protected String Curso;
    protected Double Nota;
    
    private PropertyChangeSupport propertySupport;
    //CONSTANTES 
    protected static final String URL = "jdbc:mysql://192.168.0.250/alumnos";
    protected static final String USR = "alumnos";
    protected static final String PSW = "123456";

    //CONSTRUCTOR DE LA CLASE    
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
    
    /* GETTERS Y SETTERS */
    /**
     * Getter que devuelve DNI
     * 
     * @return String DNI
     */
    public String getDNI() {
        return DNI;
    }
    /**
     * Setter que establece DNI
     * 
     * @param DNI 
     */
    public void setDNI(String DNI) {
        this.DNI = DNI;
    }
    /**
     * Getter que devuelve NombreCurso
     * 
     * @return String NombreCurso
     */
    public String getNombreCurso() {
        return NombreCurso;
    }
    /**
     * Setter que establece NombreCurso
     * 
     * @param NombreCurso
     */
    public void setNombreCurso(String NombreCurso) {
        this.NombreCurso = NombreCurso;
    }
    /**
     * Getter que devuelve Curso
     * 
     * @return String Curso
     */
    public String getCurso() {
        return Curso;
    }
    /**
     * Setter que establece Curso
     * 
     * @param Curso
     */
    public void setCurso(String Curso) {
        this.Curso = Curso;
    }
    /**
     * Getter que devuelve Nota
     * 
     * @return Double Nota
     */
    public Double getNota() {
        return Nota;
    }
    /**
     * Setter que establece Nota
     * 
     * @param Nota Double
     */
    public void setNota(Double Nota) {
        this.Nota = Nota;
    }
    
    /**
     * Clase auxiliar que usaremos para crear un vector privado de calificaciones
     */
    private class Calificacion {
        //Atributos
        public String DNI;
        public String NombreCurso;
        public String Curso;
        public Double Nota;

        //Constructor vacio
        public Calificacion() {  };

        //Constructor con parametros
        public Calificacion(String DNI, String NombreCurso, String Curso, Double Nota) {
            this.DNI = DNI;
            this.NombreCurso = NombreCurso;
            this.Curso = Curso;
            this.Nota = Nota;
        }
    }

    /**
     * Usaremos un atributo con un vector auxiliar para cargar la información 
     * de la tabla de forma que tengamos acceso a los datos sin necesidad de 
     * estar conectados constantemente
     */
    private Vector Calificaciones = new Vector();
    
    //Almacenamos el tamaño del vector
    private int tamanio;
    
    /**
     * Método getTamanio(), que devuelve un entero con el tamaño en dicho momento
     * del vector Calificaciones. Útil para los bucles y listar datos.
     * 
     * @return tamanio int
     */
    public int getTamanio() {
        //actualiza el tamaño actual del vector
        this.tamanio = Calificaciones.size();
        return this.tamanio;
    }

    /**
     * Actualiza el contenido de la tabla en el vector de alumnos. Las propiedades 
     * contendrán el valor del primer elemento de la tabla
     */
    private void recargarFilas() throws ClassNotFoundException {
        //Limpiamos previamente el arraylist de elementos (asi no añadimos 
        //cada vez que hacemos una modificacion duplicados)
        Calificaciones.clear();
        //Conectamos
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USR, PSW);
            Statement s = con.createStatement();
            //realizamos la consulta
            ResultSet rs = s.executeQuery("select * from calificaciones");
            //rellenamos el vector con objetos calificacion con cada fila
            while (rs.next()) {
                Calificacion c = new Calificacion(rs.getString("DNI"),
                                                  rs.getString("NombreCurso"), 
                                                  rs.getString("Curso"), 
                                                  rs.getDouble("Nota"));
                Calificaciones.add(c);
            }

            //creamos un objeto tipo calificacion con los datos en el elemento1
            Calificacion c = (Calificacion) Calificaciones.elementAt(1); 
            //establecemos los atributos por defecto a los del primer elemento
            this.DNI = c.DNI;
            this.NombreCurso = c.NombreCurso;
            this.Curso = c.Curso;
            this.Nota = c.Nota;
            //cerramos conexiones a bd.
            rs.close();
            con.close();
        //en el caso de existir excepciones/errores establecemos los valores como
        //vacios
        } catch (SQLException ex) {
            this.DNI = "";
            this.NombreCurso = "";
            this.Curso = "";
            this.Nota = 0.0;
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo seleccionarFila al que se le pasa un valor de fila entero por 
     * parametro y establece los atributos existentes. En caso de no existir
     * dichos datos o pasar un valor de fila incorrecto, se cargaran valores 
     * por defecto.
     * 
     * @param i numero de la fila a cargar en las propiedades del componente
     */
    public void seleccionarFila(int i) {
        if (i <= Calificaciones.size()) {
            Calificacion c = (Calificacion) Calificaciones.elementAt(i);
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
     * Método seleccionarDNI, que establece los atributos del componente por los
     * del objeto que coincida en DNI en el vector. En el caso de no existir, se
     * establecerán datos por defecto.
     * 
     * Se ha corregido del ejemplo del temario al entrar en bucle constante usando
     * while. El funcionamiento es identico.
     * 
     * Asimismo, no es un metodo completo dado que devolverá el único o el último
     * registro existente con dicho DNI.
     * 
     * @param nDNI String con el DNI completo
     */
    public void seleccionarDNI(String nDNI) {
        //se establecen datos por defecto
        this.DNI = "";
        this.NombreCurso = "";
        this.Curso = "";
        this.Nota = 0.0;
        //Se recorre el vector en búsqueda de la ultima coincidencia
        for (int i=0; i < Calificaciones.size(); i++) {
            //Se obtiene elemento a elemento del vector
            Calificacion c = (Calificacion) Calificaciones.elementAt(i);
            //Se compara con el DNI pasado por parametro, si es válido
            if (c.DNI.equals(nDNI)) {
                //Establecemos sus atributos como los del componente
                this.DNI = c.DNI;
                this.NombreCurso = c.NombreCurso;
                this.Curso = c.Curso;
                this.Nota = c.Nota;
            }
        }
    }

    /**
     * Código para añadir un nuevo alumno a la base de datos. cada vez que se 
     * modifca el estado de la BD se genera un evento para que se recargue el 
     * componente.
     */
    private BDModificadaListener receptor;

    //Clase que implementa los eventos
    public class BDModificadaEvent extends java.util.EventObject {
        // constructor
        public BDModificadaEvent(Object source) {
            super(source);
        }
    }

    //Definiendo la interfaz de los eventos
    public interface BDModificadaListener extends EventListener {
        //metodo para modificaciones generales de la BD
        public void capturarBDModificada(BDModificadaEvent ev);
        //metodo para modificaciones de la nota
        public void capturarNotaBDmodificada(BDModificadaEvent ev);
    }
    
    //añadir oyente
    public void addBDModificadaListener(BDModificadaListener receptor) {
        this.receptor = receptor;
    }
    //eliminar oyente
    public void removeBDModificadaListener(BDModificadaListener receptor) {
        this.receptor = null;
    }

    /**
     * Método que añade una calificacion a la base de datos añade un registro a 
     * la base de datos formado a partir de los valores de las propiedades del
     * componente.
     *
     * Se presupone que se han usado los métodos set para configurar
     * adecuadamente las propiedades con los datos del nuevo registro.
     * 
     * @throws java.lang.ClassNotFoundException
     */
    public void agregarNotaModulo() throws ClassNotFoundException {
        try {
            //conectamos
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USR, PSW);
            //preparamos la consulta
            PreparedStatement s = con.prepareStatement("INSERT INTO calificaciones"
                    + "(DNI, NombreCurso, Curso, Nota) "
                    + "VALUES (?,?,?,?)");
            //pasamos los datos por parametro
            s.setString(1, DNI);
            s.setString(2, NombreCurso);
            s.setString(3, Curso);
            s.setDouble(4, Nota);
            //ejecutamos la consulta
            s.executeUpdate();
            //recargamos el vector al existir cambios
            recargarFilas();
            //lanzamos un evento
            receptor.capturarBDModificada(new BDModificadaEvent(this));
        } catch (SQLException ex) {
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método que modifica una calificacion a la base de datos añade un registro 
     * a la base de datos formado a partir de los valores de las propiedades del
     * componente.
     *
     * Se presupone que se han usado los métodos set para configurar
     * adecuadamente las propiedades con los datos del nuevo registro.
     * 
     * @throws java.lang.ClassNotFoundException
     */
    public void modificarNotaModulo() throws ClassNotFoundException {
        try {
            //conectamos
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USR, PSW);
            //preparamos la consulta
            PreparedStatement s = con.prepareStatement(
                    "UPDATE calificaciones SET nota = ? WHERE DNI = ?");
            //pasamos los datos por parametro
            s.setDouble(1, Nota);
            s.setString(2, DNI);
            //ejecutamos la consulta
            s.executeUpdate();
            //recargamos el vector al existir cambios
            recargarFilas();
            //lanzamos un evento
            receptor.capturarNotaBDmodificada(new BDModificadaEvent(this));
        } catch (SQLException ex) {
            Logger.getLogger(CalificacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Operaciones con oyentes. Añadir oyente
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    /**
     * Operaciones con oyentes. Eliminar oyente
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

}


