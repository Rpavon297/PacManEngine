package testing.evo.genetica;

import java.util.ArrayList;
import java.util.List;

public class Poblacion {
    private List<Individuo> poblacion;

    public Poblacion() {
        poblacion = new ArrayList<>();
    }

    public Poblacion(List<Individuo> pob){
        this.poblacion = new ArrayList();
        for(Individuo ind : pob)
            this.poblacion.add(new Individuo(ind));
    }

    public List<Individuo> getPoblacion() {
        return poblacion;
    }

    public void revisarAdaptacion(boolean maximiza){

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for(Individuo c : poblacion){
            if(c.getFitness() > max)
                max = c.getFitness();
            if(c.getFitness() < min)
                min = c.getFitness();
        }

        max = max * 1.05;
        min = min * 0.95;

        if(maximiza)
            for(Individuo c : poblacion)
                c.setFitnessAdaptado(c.getFitness() + min);
        else
            for(Individuo c : poblacion)
                c.setFitnessAdaptado(max - c.getFitness());


    }

    public void setProbSeleccion(double fitnessTotal) {

        double probSel;
        double probSelAcu = 0;

        for(Individuo i : poblacion) {
            probSel = i.getFitnessAdaptado()/fitnessTotal;
            probSelAcu += probSel;
            i.setProbSeleccion(probSel);
            i.setAcumulado(probSelAcu);
        }
    }

    public void substitute(Poblacion nPoblacion) {
        // TODO Auto-generated method stub
        int i = this.poblacion.size() - 1;
        for(Individuo ind : nPoblacion.getPoblacion()){
            this.poblacion.set(i, ind);
            i--;
        }
    }

    //Para debugear

    public String toString(){
        String cadena = "";
        for (Individuo in : this.poblacion)
            cadena += in.getFenotipo() + "\n";

        return cadena;
    }


}
