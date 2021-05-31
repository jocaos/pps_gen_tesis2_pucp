import java.util.*;
import java.io.*;

public class SeleccionPortafolioProyectos {

    public static void main(String[] args) throws IOException {

        Proyectos proyectos = new Proyectos();

        if (args.length != 1) {
            System.out.println("Usage:");
            System.out.println("java SeleccionPortafolioProyectos <nombreArchivo>");
            System.out.println("Example: java SeleccionPortafolioProyectos dataset05");
            System.exit(0);
        }

        if (args[0].equals("aleatorio")) {
            proyectos.generarAleatorios("dataset10-a");

        } else if (!args[0].isEmpty()) {
            proyectos.leerDatos(args[0]);
        }
        /* Configuración de algoritmo genético*/
        int numGeneraciones = 3;
        int sizePoblacion = 4;
        double tasaMutacion = 0.5;
        double tasaCruzamiento = 0.5;

        AlgoritmoGenetico ag = new AlgoritmoGenetico(proyectos,
                numGeneraciones, sizePoblacion, tasaMutacion, tasaCruzamiento);
        ag.run();
    }
}
