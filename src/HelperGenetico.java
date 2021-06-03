import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HelperGenetico {

    private Proyectos proyectos;

    public HelperGenetico(Proyectos proyectos) {
        this.setProyectos(proyectos);
    }

    public Proyectos getProyectos() {
        return proyectos;
    }

    public void setProyectos(Proyectos proyectos) {
        this.proyectos = proyectos;
    }

    public List<Individuo> generarPoblacionInicial(int poblacionSize) {
        //System.out.println("Generando Población Inicial");
        List<Individuo> poblacionInicial = new ArrayList<>();

        for (int i = 0; i < poblacionSize; i++) {
            List<Integer> cromosoma = new ArrayList<>();
            for (int j = 0; j < proyectos.getNumeroProyectos(); j++) {
                cromosoma.add(Math.random() > 0.5 ? 1 : 0);
            }
            Individuo individuo = new Individuo(cromosoma);
            poblacionInicial.add(individuo);
        }
        Collections.shuffle(poblacionInicial);

        return poblacionInicial;
    }

    public List<Individuo> elegirPadrePorRuleta(List<Individuo> poblacion) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        int indexPadre1 = 0;
        int indexPadre2 = 0;

        double fitnessTotal = 0;
        for (Individuo p : poblacion) {
            fitnessTotal += p.getFitness();
        }

        double agujaRuleta = Math.random() * fitnessTotal;
        System.out.println("TotalFitness: " + df.format(fitnessTotal) + " Ruleta 1: " + df.format(agujaRuleta));
        
        /* Escogemos al primer padre */
        double fitnessAcumulado = 0;
        int index = 0;
        for (Individuo p : poblacion) {
            fitnessAcumulado += p.getFitness();
            if (fitnessAcumulado >= agujaRuleta) {
                indexPadre1 = index;
                fitnessTotal -= poblacion.get(indexPadre1).getFitness();
                break;
            }
            index++;
        }

        /* Escogemos al segundo padre */
        agujaRuleta = Math.random() * fitnessTotal;
        System.out.println("TotalFitness: " + df.format(fitnessTotal) + " Ruleta 1: " + df.format(agujaRuleta));
        fitnessAcumulado = 0;
        index = 0;
        for (Individuo p : poblacion) {
            if (index == indexPadre1) {
                index++;
                continue; // Not the same parent
            }
            fitnessAcumulado += p.getFitness();
            if (fitnessAcumulado >= agujaRuleta) {
                indexPadre2 = index;
                break;
            }
            index++;
        }

        List<Individuo> padres = new ArrayList<>();
        padres.add(poblacion.get(indexPadre1));
        padres.add(poblacion.get(indexPadre2));
        return padres;
    }

    public double getAleatorioEnteroEntre(int max, int min) {
        return Math.floor(Math.random() * (max - min + 1) + min);
    }

    public void evaluarPoblacion(List<Individuo> poblacion) {
        for (Individuo indv : poblacion) {
            indv.setFitness(this.evaluarFitness(indv.getCromosoma()));
        }
        imprimirPoblacion(poblacion);
    }

    public double evaluarFitness(List<Integer> cromosoma) {
        int sumaCostos = 0;
        double sumaScores = 0;

        double fitness;
        double denominador;

        int index = 0;
        for (Integer gen : cromosoma) {
            if (gen == 1) {
                denominador = this.proyectos.getmCostos().get(index) *
                        this.proyectos.getmRiesgos().get(index) *
                        this.proyectos.getmTiempos().get(index);
                sumaScores += this.getProyectos().getmGanancias().get(index) / denominador;
                sumaCostos += this.proyectos.getmCostos().get(index);
            }
            index++;
        }
        if (sumaCostos > this.proyectos.getPresupuesto()) return 0;
        fitness = sumaScores * 10;
        return fitness;
    }

    public List<Individuo> seleccionarNuevaPoblacion(List<Individuo> poblacion,
                                                     List<Individuo> descendientes,
                                                     int numeroSobrevivientes) {
        List<Individuo> nuevaPoblacion = new ArrayList<>();
        // une las dos poblaciones
        poblacion.addAll(descendientes);
        // Ordenamos de mayor a menor para añadir los padres e hijos mas fuertes
        poblacion.sort(Comparator.comparing(Individuo::getFitness).reversed());
        for (int i = 0; i < numeroSobrevivientes; i++) {
            nuevaPoblacion.add(poblacion.get(i));
        }
        System.out.println("Población Anterior");
        this.imprimirPoblacion(poblacion);
        System.out.println("Nueva POblación");
        this.imprimirPoblacion(nuevaPoblacion);
        return nuevaPoblacion;
    }

    public List<Individuo> generarPoblacionInicialFija(int poblacionSize) {
        System.out.println("Generando Población Inicial");
        List<Individuo> poblacionInicial = new ArrayList<>();

        // Individuo 1
        List<Integer> cromosoma1 = new ArrayList<>();
        cromosoma1.add(0);
        cromosoma1.add(1);
        cromosoma1.add(0);
        cromosoma1.add(1);
        cromosoma1.add(0);
        Individuo a = new Individuo(cromosoma1);
        poblacionInicial.add(a);

        // Individuo 2
        List<Integer> cromosoma2 = new ArrayList<>();
        cromosoma2.add(1);
        cromosoma2.add(0);
        cromosoma2.add(1);
        cromosoma2.add(0);
        cromosoma2.add(1);
        Individuo b = new Individuo(cromosoma2);
        poblacionInicial.add(b);

        // Individuo 3
        List<Integer> cromosoma3 = new ArrayList<>();
        cromosoma3.add(0);
        cromosoma3.add(0);
        cromosoma3.add(1);
        cromosoma3.add(1);
        cromosoma3.add(1);
        Individuo c = new Individuo(cromosoma3);
        poblacionInicial.add(c);

        // Individuo 4
        List<Integer> cromosoma4 = new ArrayList<>();
        cromosoma4.add(1);
        cromosoma4.add(1);
        cromosoma4.add(0);
        cromosoma4.add(0);
        cromosoma4.add(0);
        Individuo d = new Individuo(cromosoma4);
        poblacionInicial.add(d);

        // Individuo 5
        List<Integer> cromosoma5 = new ArrayList<>();
        cromosoma5.add(1);
        cromosoma5.add(0);
        cromosoma5.add(1);
        cromosoma5.add(1);
        cromosoma5.add(1);
        Individuo e = new Individuo(cromosoma5);
        poblacionInicial.add(e);

        return poblacionInicial;
    }

    public void imprimirPoblacion(List<Individuo> poblacion) {
        for (Individuo i : poblacion) {
            System.out.println(i);
        }
    }
}
