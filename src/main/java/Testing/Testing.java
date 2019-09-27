package Testing;

import Testing.Evo.Algoritmo.AlgoritmoGenetico;
import Testing.Evo.Algoritmo.DecodGramatica;
import Testing.Evo.Algoritmo.Gramatica;
import Testing.Evo.Genetica.Individuo;
import Testing.Evo.Genetica.Poblacion;

import es.ucm.fd.ici.c1920.practica0.RobertoPavon.Ghosts;
import es.ucm.fd.ici.c1920.practica0.RobertoPavon.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Testing {
    public static void main(String[] args){

        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();;

        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico();
        Poblacion poblacion = algoritmoGenetico.ejecutarAlgoritmo(200, 800, 60, 100,
         20, true, 0.1);


        Individuo mejor = poblacion.getPoblacion().get(0);
        List<Double> lista = mejor.getFenotipo();
        DecodGramatica dgramatica = new DecodGramatica(lista);
        Gramatica gramatica = new Gramatica(lista);

        System.out.println("Entrenamiento terminado. Puse cualquier tecla y enter para ejecutar.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GhostController ghost = new Ghosts();
        System.out.println(executor.runGame(gramatica, ghost, 50));


        /*
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

*/
    }
}
