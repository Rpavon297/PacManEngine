package Testing.Evo.Algoritmo;

import Testing.Evo.Genetica.Gen;
import Testing.Evo.Genetica.Individuo;
import Testing.Evo.Genetica.Poblacion;
import es.ucm.fd.ici.c1920.practica0.RobertoPavon.Ghosts;
import pacman.Executor;
import pacman.game.util.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AlgoritmoGenetico {

    public Poblacion ejecutarAlgoritmo(int poblacionSize, int numGeneraciones, double probabilidadCruce, int cantidadSeleccionada,
                                       double probabilidadMutacion, boolean elitismo, double percentElitismo){

        Poblacion poblacion = new Poblacion();
        inicializa(poblacion, poblacionSize);
        List<Individuo> generaciones = new ArrayList<>();

        int salvados = (int) Math.ceil(poblacion.getPoblacion().size() * percentElitismo);
        Poblacion elite = new Poblacion();

        double fitnessTotal = actualizarPoblacion(poblacion);

        for(int i = 0; i < numGeneraciones; i++){
            generaciones.add(poblacion.getPoblacion().get(0));

            //Guardar élite
            if(elitismo)
                elite = new Poblacion(poblacion.getPoblacion().subList(0,salvados+1));

            //Selección
            Poblacion pobsel = new Poblacion(seleccionarPoblacion(poblacion.getPoblacion(), cantidadSeleccionada));
            //Cruce
            poblacion.substitute(cruzarPoblacion(pobsel, probabilidadCruce));
            //Mutación
            poblacion = new Poblacion(mutarPoblacion(poblacion.getPoblacion(), probabilidadMutacion));
            //Evaluación
            fitnessTotal = actualizarPoblacion(poblacion);
            //Introducir elite
            poblacion.substitute(elite);
            //Reevaluación
            fitnessTotal = actualizarPoblacion(poblacion);
        }

        mostrarGrafica(generaciones);
        return poblacion;
    }

    private void inicializa(Poblacion poblacion, int poblacionSize){
        for(int i = 0; i < poblacionSize; i++){
            List<Gen> genes = new ArrayList<>();

            for(int j = 0; j < 20; j++){
                genes.add(new Gen(1));
                genes.get(j).randomize(0,256);
            }

            poblacion.getPoblacion().add(new Individuo(genes));
        }
    }

    private double actualizarPoblacion(Poblacion poblacion) {
        Executor executor = new Executor.Builder().build();
        double fitnessTotal = 0;

        for(Individuo ind : poblacion.getPoblacion()) {
            List<Double> lista = ind.getFenotipo();
            Gramatica gramatica = new Gramatica(lista);
            Stats[] stats = executor.runExperiment(gramatica, new Ghosts(), 10, "Fitness...");
            ind.setFitness(stats[0].getAverage());
            fitnessTotal += stats[0].getAverage();
        }

        ordenarPoblacion(poblacion);
        poblacion.setProbSeleccion(fitnessTotal);

        return fitnessTotal;
    }

    private void ordenarPoblacion(Poblacion poblacion){
        Collections.sort(poblacion.getPoblacion(), (i1, i2) -> new Double(i2.getFitness()).compareTo(new Double(i1.getFitness())));
    }

    private List<Individuo> seleccionarPoblacion(List<Individuo> poblacion, int k){
        int tamTorneo = 2;
        if(poblacion.size() > 10) tamTorneo = 3;

        List<Individuo> pobSeleccionada = new ArrayList<Individuo>();

        for (int i = 0; i < k; i++){
            List<Individuo> seleccionados = new ArrayList();
            for(int j = 1; j < tamTorneo; j++)
                seleccionados.add(poblacion.get(ThreadLocalRandom.current().nextInt(0,poblacion.size())));

            Individuo mejor = seleccionados.get(0);
            for(Individuo ind : seleccionados)
                if(ind.getFitnessAdaptado() > mejor.getFitnessAdaptado())
                    mejor = ind;

            pobSeleccionada.add(mejor);

        }

        return pobSeleccionada;
    }

    private Poblacion cruzarPoblacion(Poblacion poblacion, double probabilidad){

        Poblacion pob = new Poblacion();
        int aCruzar = 1;
        Individuo j = null;

        for(Individuo i : poblacion.getPoblacion()) {
            if(Math.random() < probabilidad) {
                if (aCruzar == 1)
                    j = i;
                if (aCruzar == 2) {
                    aCruzar = 0;
                    pob.getPoblacion().addAll(monopunto(i,j));
                }
                aCruzar++;
            }
        }
        return pob;
    }

    private List<Individuo> monopunto(Individuo padre1, Individuo padre2){
        int min = 0;
        int max = padre1.getTam();

        int random = ThreadLocalRandom.current().nextInt(min, max);

        Individuo h1 = new Individuo(padre1);
        Individuo h2 = new Individuo(padre2);

        List<Gen> genes1 = h1.getGenes();
        List<Gen> genes2 = h2.getGenes();

        List<Object> alelos1 = padre1.getAlelos();
        List<Object> alelos2 = padre2.getAlelos();

        List<Object> hijo1 = alelos1.subList(0,random);
        hijo1.addAll(alelos2.subList(random,max));

        List<Object> hijo2 = alelos2.subList(0,random);
        hijo2.addAll(alelos1.subList(random,max));

        int acum = 0;

        for(Gen g : genes1) {
            g.setAlelos(hijo1.subList(acum, acum+g.getTam()));
            acum += g.getTam();
        }

        acum = 0;

        for(Gen g : genes2) {
            g.setAlelos(hijo2.subList(acum, acum+g.getTam()));
            acum += g.getTam();
        }

        List <Individuo> hijos = new ArrayList<Individuo>();

        hijos.add(new Individuo(genes1));
        hijos.add(new Individuo(genes2));
        return hijos;
    }

    private List<Individuo> mutarPoblacion(List<Individuo> poblacion, double probabilidad){
        double min = 0, max = 256;

            for(Individuo individuo : poblacion){
                for(Gen gen : individuo.getGenes()){
                    double random = ThreadLocalRandom.current().nextDouble(0, 1);
                    if(random <= probabilidad)
                        gen.randomize(min, max);
                }
            }


        return poblacion;

    }

    private void mostrarGrafica(List<Individuo> generaciones){
        int i = 0;
        for(Individuo g : generaciones) {
            i++;
            System.out.println("Generación " + i + ", puntuación del mejor PacMan: " + g.getFitness() +
                    " con fenotipo: " + g.getFenotipo() + " y genotipo: " + g.getAlelos());
        }
    }
}