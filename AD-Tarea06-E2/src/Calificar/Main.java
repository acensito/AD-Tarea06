
package Calificar;

/**
 *
 * @author Felipon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //instanciamos la clase
        AccedeDB gestion = new AccedeDB();
        //Mostramos el listado inicial
        gestion.listado();
        //Añadimos una calificación
        gestion.anade();
        //Modificamos una calificacion
        gestion.modifica();
        //Listamos de nuevo
        gestion.listado();
    }
}