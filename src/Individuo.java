import java.util.ArrayList;
import java.util.List;

public class Individuo {
    List<Integer> cromosoma;
    double fitness;

    public Individuo(List<Integer> cromosoma, double fitness) {
        this.cromosoma = cromosoma;
        this.fitness = fitness;
    }

    public Individuo(List<Integer> cromosoma) {
        this.cromosoma = cromosoma;
        this.fitness = -1;
    }

    public Individuo() {
    }

    public List<Integer> getCromosoma() {
        return cromosoma;
    }

    public void setCromosoma(List<Integer> cromosoma) {
        this.cromosoma = cromosoma;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<Individuo> cruzamientoUniforme(Individuo otro) {
        List<Individuo> nuevosIndividuos = new ArrayList<>();

        Individuo indv1;
        Individuo indv2;

        return nuevosIndividuos;
    }

    public List<Individuo> cruzamientoOnePoint(Individuo otro) {
        List<Individuo> nuevosIndividuos = new ArrayList<>();

        List<Integer> cromosoma1 = new ArrayList<>();
        List<Integer> cromosoma2 = new ArrayList<>();

        Individuo indv1 = new Individuo(cromosoma1);
        Individuo indv2 = new Individuo(cromosoma2);

        // int puntoAleatorio = (int) (Math.floor(Math.random() * (this.getCromosoma().size() + 1)));
        int puntoAleatorio = 2;
        for (int i = 0; i < puntoAleatorio; i++) {
            cromosoma1.add(this.cromosoma.get(i));
            cromosoma2.add(otro.cromosoma.get(i));
        }
        for (int i = puntoAleatorio; i < this.getCromosoma().size(); i++) {
            cromosoma2.add(i, this.cromosoma.get(i));
            cromosoma1.add(i, otro.cromosoma.get(i));
        }

        nuevosIndividuos.add(indv1);
        nuevosIndividuos.add(indv2);

        return nuevosIndividuos;
    }

    public void mutarIndividuo() {
        int index = 0;
        for (Integer gen : this.getCromosoma()) {
            if(Math.random() < 0.5){
                if (gen == 0) this.getCromosoma().set(index, 1);
                else this.getCromosoma().set(index, 0);;
            }
            index++;
        }
    }

    @Override
    public String toString() {
        String ind = "";
        for (int i = 0; i < this.cromosoma.size(); i++) {
            ind = ind + this.cromosoma.get(i);
        }
        ind += " " + this.getFitness();
        return ind;
    }
}
