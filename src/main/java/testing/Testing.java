package testing;

import testing.evo.algoritmo.AlgoritmoGenetico;
import testing.evo.algoritmo.DecodGramatica;
import testing.evo.algoritmo.Gramatica;
import testing.evo.controladores.RandomGhosts;
import testing.evo.genetica.Individuo;
import testing.evo.genetica.Poblacion;

import pacman.Executor;
import pacman.controllers.GhostController;

import java.io.IOException;
import java.util.List;

public class Testing {
    public static void main(String[] args){

        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();;

        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico();
        Poblacion poblacion = algoritmoGenetico.ejecutarAlgoritmo(50,  50, 60, 28,
         10, true, 0.05);


        Individuo mejor = poblacion.getPoblacion().get(0);

        List<Double> lista = mejor.getFenotipo();

        DecodGramatica decodGramatica = new DecodGramatica(lista);

        Gramatica gramatica = new Gramatica(lista);

        decodGramatica.showTree();

        System.out.println("Entrenamiento terminado. Puse cualquier tecla y enter para ejecutar.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GhostController ghost = new RandomGhosts();
        System.out.println(executor.runGame(gramatica, ghost, 30));


        /*
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

*/
    }
}