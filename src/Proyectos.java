import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class Proyectos {
    int numeroProyectos;
    int presupuesto;
    List<Integer> mGanancias;
    List<Integer> mCostos;
    List<Integer> mRiesgos;
    List<Integer> mTiempos;
    List<Integer> mSolucion;
    List<Integer> projectosOrdDescendente;

    public Proyectos() {
    }

    public int getNumeroProyectos() {
        return numeroProyectos;
    }

    public void setNumeroProyectos(int numeroProyectos) {
        this.numeroProyectos = numeroProyectos;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public List<Integer> getmGanancias() {
        return mGanancias;
    }

    public void setmGanancias(List<Integer> mGanancias) {
        this.mGanancias = mGanancias;
    }

    public List<Integer> getmCostos() {
        return mCostos;
    }

    public void setmCostos(List<Integer> mCostos) {
        this.mCostos = mCostos;
    }

    public List<Integer> getmRiesgos() {
        return mRiesgos;
    }

    public void setmRiesgos(List<Integer> mRiesgos) {
        this.mRiesgos = mRiesgos;
    }

    public List<Integer> getmTiempos() {
        return mTiempos;
    }

    public void setmTiempos(List<Integer> mTiempos) {
        this.mTiempos = mTiempos;
    }

    public List<Integer> getmSolucion() {
        return mSolucion;
    }

    public void setmSolucion(List<Integer> mSolucion) {
        this.mSolucion = mSolucion;
    }

    public List<Integer> getProjectosOrdDescendente() {
        return projectosOrdDescendente;
    }

    public void setProjectosOrdDescendente(List<Integer> projectosOrdDescendente) {
        this.projectosOrdDescendente = projectosOrdDescendente;
    }

    public void leerDatos(String nombreArchivo) throws IOException {

        this.mGanancias = new ArrayList<>();
        this.mCostos = new ArrayList<>();
        this.mRiesgos = new ArrayList<>();
        this.mTiempos = new ArrayList<>();

        String pathName = "dataset/" + nombreArchivo + ".txt";
        File file = new File(pathName);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        StringTokenizer token = new StringTokenizer(line, " ");

        int numProyectos = Integer.parseInt(token.nextToken());
        int presupuesto = Integer.parseInt(token.nextToken());

        String ganancias = reader.readLine();
        String costos = reader.readLine();
        String riesgos = reader.readLine();
        String tiempos = reader.readLine();

        this.numeroProyectos = numProyectos;
        this.presupuesto = presupuesto;

        this.leerToken(ganancias, this.mGanancias);
        this.leerToken(costos, this.mCostos);
        this.leerToken(riesgos, this.mRiesgos);
        this.leerToken(tiempos, this.mTiempos);

        this.projectosOrdDescendente = this.ordenarPorScore();
    }

    private void leerToken(String linea, List<Integer> matriz) {
        StringTokenizer token = new StringTokenizer(linea, " ");
        while (token.hasMoreElements()) {
            matriz.add(Integer.parseInt(token.nextToken()));
        }
    }

    public void generarAleatorios(String nombreArhcivo) {
        try {
            String filename = "dataset/" + nombreArhcivo + ".xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Proyectos");

            /// Hoja de txt
            PrintWriter writer = new PrintWriter("dataset/" + nombreArhcivo + ".txt", "UTF-8");
            ///

            int presupuesto = this.aleatorioEntre(400, 600);
            int numProyectos = this.aleatorioEntre(10, 20);

            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell(0).setCellValue("Numero Proyectos");
            rowhead.createCell(1).setCellValue(numProyectos);

            rowhead = sheet.createRow((short) 1);
            rowhead.createCell(0).setCellValue("Presupuesto");
            rowhead.createCell(1).setCellValue(presupuesto);

            writer.println(numProyectos);
            writer.println(presupuesto);
            rowhead = sheet.createRow((short) 2);

            rowhead.createCell(0).setCellValue("Nº Proyecto");
            rowhead.createCell(1).setCellValue("Ganancia");
            rowhead.createCell(2).setCellValue("Costo");
            rowhead.createCell(3).setCellValue("Tiempo");
            rowhead.createCell(4).setCellValue("Riesgo");
            rowhead.createCell(5).setCellValue("Score");

            this.mGanancias = new ArrayList<>();
            this.mCostos = new ArrayList<>();
            this.mRiesgos = new ArrayList<>();
            this.mTiempos = new ArrayList<>();

            this.numeroProyectos = numProyectos;
            this.presupuesto = presupuesto;

            for (int i = 0; i < numProyectos; i++) {
                HSSFRow rowData = sheet.createRow((short) i + 3);
                int ganancia = this.aleatorioEntre(100, 200);
                int costo = this.aleatorioEntre(50, 100);
                int tiempo = this.aleatorioEntre(1, 10);
                int riesgo = this.aleatorioEntre(1, 10);

                mGanancias.add(ganancia);
                mCostos.add(costo);
                mRiesgos.add(tiempo);
                mTiempos.add(riesgo);

                rowData.createCell(0).setCellValue(i+1);
                rowData.createCell(1).setCellValue(ganancia);
                rowData.createCell(2).setCellValue(costo);
                rowData.createCell(3).setCellValue(tiempo);
                rowData.createCell(4).setCellValue(riesgo);
                rowData.createCell(5).setCellValue((double) (ganancia * 10) / (costo * tiempo * riesgo));
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            writer.close();
            System.out.println("Se generaron los proyectos");

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private int aleatorioEntre(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    private List<Integer> ordenarPorScore() {
        List<Double> scores = new ArrayList<>();
        List<Integer> proyectos = new ArrayList<>();

        for (int i = 0; i < this.getNumeroProyectos(); i++) {
            double denominador = this.getmCostos().get(i) * this.getmRiesgos().get(i) * this.getmTiempos().get(i);
            double score = (this.getmGanancias().get(i) * 10) / denominador;
            scores.add(i, score);
            proyectos.add(i, i);
        }

        Collections.sort(proyectos, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Double.compare(scores.get(proyectos.get(o1)).doubleValue(),
                        scores.get(proyectos.get(o2)).doubleValue());
            }
        }.reversed());

        return proyectos;
    }
}
