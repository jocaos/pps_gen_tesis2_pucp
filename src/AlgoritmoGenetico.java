import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlgoritmoGenetico {

    private List<Individuo> poblacion;
    private int numGeneraciones;
    private int poblacionSize;
    private double tasaMutacion;
    private double tasaCruzamiento;
    private int numeroSobrevivientes;
    private Individuo bestInd;

    private HelperGenetico helper;

    public AlgoritmoGenetico(Proyectos proyectos, int numGeneraciones, int tamPoblacion,
                             double tasaMutacion, double tasaCruzamiento) {
        this.helper = new HelperGenetico(proyectos);
        this.numGeneraciones = numGeneraciones;
        this.tasaMutacion = tasaMutacion;
        this.tasaCruzamiento = tasaCruzamiento;
        this.poblacionSize = tamPoblacion;
        this.numeroSobrevivientes = tamPoblacion;
    }

    public List<Individuo> getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(List<Individuo> poblacion) {
        this.poblacion = poblacion;
    }

    public int getNumGeneraciones() {
        return numGeneraciones;
    }

    public int getPoblacionSize() {
        return poblacionSize;
    }

    public double getTasaMutacion() {
        return tasaMutacion;
    }

    public double getTasaCruzamiento() {
        return tasaCruzamiento;
    }

    public int getNumeroSobrevivientes() {
        return numeroSobrevivientes;
    }

    private void definirPoblacion() {
        this.setPoblacion(helper.generarPoblacionInicial(this.getPoblacionSize()));
    }

    private void evaluarPoblacion() {
        helper.evaluarPoblacion(this.getPoblacion());
    }

    private void evaluarIndividuos(List<Individuo> individuos) {
        helper.evaluarPoblacion(individuos);
    }

    private List<Individuo> cruzarPoblacion(double tasaCruzamiento) {
        /* Seleccionamos lo padres para el cruzamiento */
        List<List<Individuo>> listaPadres = new ArrayList<>();
        for (int j = 0; j < (this.getPoblacion().size() /2 ); j++)
            System.out.println(j);
            listaPadres.add(helper.elegirPadrePorRuleta(this.poblacion));

        /* Crea la población descendencia cruzando las parejas del mating pool con recombinación de 1 punto */
        List<Individuo> pob_desdenciente = new ArrayList<>();
        //System.out.println("Cruzando padres");
        for (List<Individuo> padres : listaPadres) {
            //System.out.println("Padres:");
            //helper.imprimirPoblacion(padres);
            List<Individuo> hijos = padres.get(0).cruzamientoOnePoint(padres.get(1));
            //System.out.println("Hijos");
            //helper.imprimirPoblacion(hijos);
            pob_desdenciente.add(hijos.get(0));
            pob_desdenciente.add(hijos.get(1));
        }
        return pob_desdenciente;
    }

    private void mutarHijos(double tasaMutacion, List<Individuo> pob_desdenciente) {
        /* Aplica el operador de mutación con probabilidad pmut en cada hijo generado */
        //System.out.println("Hijos antes de mutar");
        //helper.imprimirPoblacion(pob_desdenciente);
        for (Individuo hijo : pob_desdenciente) {
            if (Math.random() < this.getTasaMutacion()) {
                hijo.mutarIndividuo();
            }
        }
        //System.out.println("Hijos después de mutar");
        //helper.imprimirPoblacion(pob_desdenciente);
        evaluarIndividuos(pob_desdenciente);
        //System.out.println("Hijos evaluados");
        //helper.imprimirPoblacion(pob_desdenciente);
    }

    private void seleccionarNuevaPoblacion(List<Individuo> pob_desdenciente, int numeroSobrevivientes) {
        /* Selecciona popsize individuos para la sgte. generación de la union de la pob. actual y  pob. descendencia */
        this.setPoblacion(helper.seleccionarNuevaPoblacion(this.getPoblacion(), pob_desdenciente, numeroSobrevivientes));
        //System.out.println("Nueva Población");
        //helper.imprimirPoblacion(this.getPoblacion());
    }

    public void run() {

        List<Double> bestfitness = new ArrayList<>();

        definirPoblacion();
        evaluarPoblacion();

        // Obtiene el mejor individuo de la población y registra el mejor fitness
        this.bestInd = Collections.max(this.getPoblacion(), new CompararIndividuos());
        bestfitness.add(bestInd.getFitness());
        System.out.println("Poblacion Inicial, mejor fitness = " + this.bestInd.getFitness());

        // Inicial el la generaciones
        for (int i = 0; i < this.getNumGeneraciones(); i++) { // Por cada generación
            List<Individuo> pob_desdenciente;
            pob_desdenciente = cruzarPoblacion(this.tasaCruzamiento);
            mutarHijos(this.tasaMutacion, pob_desdenciente);
            seleccionarNuevaPoblacion(pob_desdenciente, this.numeroSobrevivientes);
            /* Almacena la historia del fitness del mejor individuo */
            this.bestInd = Collections.max(this.getPoblacion(), new CompararIndividuos());
            bestfitness.add(this.bestInd.getFitness());

            /* Inicio del algoritmo memético: búsqueda local *
            //this.busquedaLocalFFD();
            //* Almacena la historia del fitness del mejor individuo */
            //this.bestInd = Collections.max(this.getPoblacion(), new CompararIndividuos());
            //bestfitness.add(this.bestInd.getFitness());
            System.out.println("Generación: " + (i + 1) + ", mejor fitness = " + this.bestInd.getFitness());
        }

        System.out.println(this.bestInd.getCromosoma());
        this.printSolucion(helper.getProyectos(), this.bestInd);

    }


    private void printSolucion(Proyectos proyectos, Individuo best) {
        try {
            String filename = "dataset/solucionAleatoria.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Proyectos-Sol");

            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell(0).setCellValue("Numero Proyectos");
            rowhead.createCell(1).setCellValue(proyectos.getNumeroProyectos());

            rowhead = sheet.createRow((short) 1);
            rowhead.createCell(0).setCellValue("Presupuesto");
            rowhead.createCell(1).setCellValue(proyectos.getPresupuesto());

            rowhead = sheet.createRow((short) 2);

            rowhead.createCell(0).setCellValue("Nº Proyecto");
            rowhead.createCell(1).setCellValue("Ganancia");
            rowhead.createCell(2).setCellValue("Costo");
            rowhead.createCell(3).setCellValue("Tiempo");
            rowhead.createCell(4).setCellValue("Riesgo");
            rowhead.createCell(5).setCellValue("Score");
            rowhead.createCell(6).setCellValue("Elección");
            rowhead.createCell(7).setCellValue("PresupuestoE");

            int presupuestoE = 0;
            int GananciaT = 0;
            for (int i = 0; i < proyectos.getNumeroProyectos(); i++) {
                HSSFRow rowData = sheet.createRow((short) i + 3);

                rowData.createCell(0).setCellValue(i + 1);
                rowData.createCell(1).setCellValue(proyectos.getmGanancias().get(i));
                rowData.createCell(2).setCellValue(proyectos.getmCostos().get(i));
                rowData.createCell(3).setCellValue(proyectos.getmTiempos().get(i));
                rowData.createCell(4).setCellValue(proyectos.getmRiesgos().get(i));
                rowData.createCell(5).setCellValue((double) (proyectos.getmGanancias().get(i) * 10) / (proyectos.getmCostos().get(i) * proyectos.getmTiempos().get(i) * proyectos.getmRiesgos().get(i)));
                rowData.createCell(6).setCellValue(best.getCromosoma().get(i));
                presupuestoE += best.getCromosoma().get(i) * proyectos.getmCostos().get(i);
                GananciaT += best.getCromosoma().get(i) * proyectos.getmGanancias().get(i);
                rowData.createCell(7).setCellValue(best.getCromosoma().get(i) * proyectos.getmCostos().get(i));
            }

            rowhead = sheet.createRow((short) proyectos.getNumeroProyectos() + 4);
            rowhead.createCell(0).setCellValue("Total Costo:");
            rowhead.createCell(1).setCellValue(presupuestoE);
            rowhead = sheet.createRow((short) proyectos.getNumeroProyectos() + 5);
            rowhead.createCell(0).setCellValue("Ganancia Total:");
            rowhead.createCell(1).setCellValue(GananciaT);


            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Se generaron los proyectos");

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private class CompararIndividuos implements Comparator<Individuo> {
        @Override
        public int compare(Individuo o1, Individuo o2) {
            if (o1.getFitness() > o2.getFitness()) return 1;
            if (o1.getFitness() < o2.getFitness()) return -1;
            return 0;
        }
    }

    private void busquedaLocalFFD() {
        List<Integer> proyecOrdenadosDes = helper.getProyectos().getProjectosOrdDescendente();

        for (Individuo indv : this.getPoblacion()) {
            //System.out.print(indv + "mejorado a ");
            int costoSol = this.calcularCostoIndividuo(indv.getCromosoma());
            if (costoSol < helper.getProyectos().getPresupuesto()) {
                int costoTemporal = costoSol;
                for (Integer indProyecto : proyecOrdenadosDes) {
                    if (indv.getCromosoma().get(indProyecto) == 0) {
                        costoTemporal += helper.getProyectos().getmCostos().get(indProyecto);
                        if (costoTemporal <= helper.getProyectos().getPresupuesto()) {
                            indv.getCromosoma().set(indProyecto, 1);
                        }
                    }
                }
            }
            //System.out.println(indv);
        }
        helper.evaluarPoblacion(this.getPoblacion());
        //helper.imprimirPoblacion(this.getPoblacion());
    }

    private int calcularCostoIndividuo(List<Integer> cromosoma) {
        int costo = 0;
        int index = 0;
        for (Integer gen : cromosoma) {
            costo += helper.getProyectos().getmCostos().get(index) * gen;
            index++;
        }
        return costo;
    }

}
