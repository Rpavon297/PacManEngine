package testing.evo.genetica;

import java.util.ArrayList;
import java.util.List;

public class Individuo {
    private List<Gen> genes;
    private double acumulado;
    private double fitness;
    private double probSeleccion;
    private double fitnessAdaptado;

    public Individuo(Individuo nCrom){
        this.acumulado = nCrom.getAcumulado();
        this.fitness = nCrom.getFitness();
        this.fitnessAdaptado = nCrom.getFitnessAdaptado();
        this.probSeleccion = nCrom.getProbSeleccion();
        this.genes = new ArrayList<>();

        for(Gen g : nCrom.getGenes()){
                this.genes.add(new Gen(g));
        }
    }

    public Individuo(List<Gen> genes){ this.genes = genes; }


    public List<Double> getFenotipo(){
        List<Double> fenot = new ArrayList<>();

        for(Gen g : genes)
            fenot.add(g.getFenotipo());
        return fenot;
    }

    public List<Object> getAlelos(){
        List<Object> alelos = new ArrayList<>();

        for(Gen g : this.genes)
            alelos.addAll(g.getAlelos());

        return alelos;
    }

    public int getTam(){
        int ret = 0;
        if(!genes.isEmpty()){
            for(Gen g : this.genes){
                ret += g.getTam();
            }
        }

        return ret;
    }


    public List<Gen> getGenes() {return genes;}

    public double getFitness() {return fitness;}
    public double getAcumulado() {return acumulado;}


    public void setAcumulado(double acumulado) {this.acumulado = acumulado;}
    public void setFitness(double fitness) {this.fitness = fitness;}

    private double getProbSeleccion() {
        return probSeleccion;
    }

    public void setProbSeleccion(double probSeleccion) {
        this.probSeleccion = probSeleccion;
    }

    public double getFitnessAdaptado() {
        return fitnessAdaptado;
    }

    public void setFitnessAdaptado(double fitnessAdaptado) {
        this.fitnessAdaptado = fitnessAdaptado;
    }

    public void setGenes(List<Gen> genes) {
        this.genes = genes;
    }

    public String toString(){
        StringBuilder cad = new StringBuilder("[");
        for(Gen g : this.genes)
            cad.append(g).append(", ");
        cad.append("]");

        return cad.toString();
    }

    public boolean equals(Object o){
        Individuo ind = (Individuo) o;
        return ind.getGenes().equals(this.genes);
    }
}
