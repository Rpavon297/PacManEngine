package testing.evo.algoritmo;

import org.jfree.ui.RefineryUtilities;
import testing.evo.controladores.CompetentGhost;
import testing.evo.controladores.GhostAggresive;
import testing.evo.controladores.RandomGhosts;
import testing.evo.data.Plot;
import testing.evo.genetica.Gen;
import testing.evo.genetica.Individuo;
import testing.evo.genetica.Poblacion;
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
        List<Double> generaciones = new ArrayList<>();

        int salvados = (int) Math.ceil(poblacion.getPoblacion().size() * percentElitismo);
        Poblacion elite = new Poblacion();

        double fitnessTotal = actualizarPoblacion(poblacion, 0);

        for(int i = 0; i < numGeneraciones; i++){
            generaciones.add(fitnessTotal/poblacionSize);

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
            fitnessTotal = actualizarPoblacion(poblacion, i);
            //Introducir elite
            poblacion.substitute(elite);
            //Reevaluación
            fitnessTotal = actualizarPoblacion(poblacion, i);
        }

        mostrarGrafica(generaciones);
        return poblacion;
    }

    private void inicializa(Poblacion poblacion, int poblacionSize){
        for(int i = 0; i < poblacionSize; i++){
            List<Gen> genes = new ArrayList<>();

            for(int j = 0; j < 50; j++){
                genes.add(new Gen(1));
                genes.get(j).randomize(0,256);
            }

            poblacion.getPoblacion().add(new Individuo(genes));
        }
    }

    private double actualizarPoblacion(Poblacion poblacion, int gen) {
        Executor executor = new Executor.Builder().build();
        double fitnessTotal = 0;

        for(Individuo ind : poblacion.getPoblacion()) {
            List<Double> lista = ind.getFenotipo();

            Gramatica gramatica = new Gramatica(lista);
            Stats[] stats;

            //Primeras 20 generaciones, lo entrenamos en "modo fácil"
            if(gen <= 20)
                stats = executor.runExperiment(gramatica, new RandomGhosts(), 300, "Generación: " + gen);
            else
                stats = executor.runExperiment(gramatica, new GhostAggresive(), 300, "Generación: " + gen);

            double fitness = FitnessFunction.getFitness(stats);

            ind.setFitness(fitness);
            fitnessTotal += fitness;
        }

        ordenarPoblacion(poblacion);
        poblacion.setProbSeleccion(fitnessTotal);

        return fitnessTotal;
    }

    private void ordenarPoblacion(Poblacion poblacion){
        Collections.sort(poblacion.getPoblacion(), (i1, i2) -> Double.compare(i2.getFitness(), i1.getFitness()));
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

    private void mostrarGrafica(List<Double> fitness){

        System.out.println("Mejor individuo con puntuacion: " + fitness.get(0));

        Plot plot = new Plot("PacMan evolutivo");
        plot.showPlot(fitness);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }
}
