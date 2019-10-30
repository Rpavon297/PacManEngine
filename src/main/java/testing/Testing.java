package testing;

import org.jfree.ui.RefineryUtilities;
import pacman.game.util.Stats;
import testing.evo.algoritmo.AlgoritmoGenetico;
import testing.evo.algoritmo.DecodGramatica;
import testing.evo.algoritmo.Gramatica;
import testing.evo.controladores.CompetentGhost;
import testing.evo.controladores.GhostAggresive;
import testing.evo.controladores.RandomGhosts;
import testing.evo.data.Plot;
import testing.evo.genetica.Individuo;
import testing.evo.genetica.Poblacion;

import pacman.Executor;
import pacman.controllers.GhostController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Testing {
    private static List<Double> suicidalPacman = new ArrayList<>(Arrays.asList(208.0,252.0,129.0,150.0,243.0,95.0,212.0,50.0,72.0,106.0,125.0,83.0,125.0,174.0,130.0,137.0,160.0,55.0,182.0,46.0));
    private static List<Double> simplePointsPacman = new ArrayList<>(Arrays.asList(156.0, 126.0, 58.0, 248.0, 140.0, 43.0, 80.0, 108.0, 113.0, 118.0, 156.0, 215.0, 44.0, 248.0, 205.0, 104.0, 174.0, 64.0, 32.0, 136.0));
    private static List<Double> maxTimePacman = new ArrayList<>(Arrays.asList(228.0, 180.0, 73.0, 162.0, 89.0, 129.0, 41.0, 65.0, 4.0, 107.0, 148.0, 7.0, 192.0, 193.0, 171.0, 122.0, 93.0, 35.0, 24.0, 188.0));
    private static List<Double> squaredPoints = new ArrayList<>(Arrays.asList(110.0, 20.0, 119.0, 220.0, 89.0, 254.0, 52.0, 93.0, 60.0, 254.0, 15.0, 87.0, 10.0, 178.0, 124.0, 134.0, 197.0, 213.0, 210.0, 175.0));
    private static List<Double> ratio = new ArrayList<>(Arrays.asList(150.0, 144.0, 21.0, 121.0, 70.0, 41.0, 78.0, 133.0, 167.0, 185.0, 96.0, 164.0, 191.0, 190.0, 255.0, 58.0, 53.0, 25.0, 24.0, 179.0));

    public static void main(String[] args){

        List<Double> lista = train();


        try {
            write("puntos.txt", lista);
        }catch (Exception e){
            System.out.println(e);
        }

        DecodGramatica decodGramatica = new DecodGramatica(lista);

        decodGramatica.showTree();
        play(lista);
    }

    private static void play(List<Double> codones){
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

        GhostController ghost = new GhostAggresive();
        System.out.println(executor.runGame(new Gramatica(codones), ghost, 30));
    }

    private static List<Double> train() {
        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico();
        Poblacion poblacion = algoritmoGenetico.ejecutarAlgoritmo(100,  50, 60, 28,
                2, true, 0.1);

        System.out.println("Entrenamiento terminado. Puse cualquier tecla y enter para ejecutar.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

         return poblacion.getPoblacion().get(0).getFenotipo();
    }


    public static void write (String filename, List<Double> lista) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));

        for (int i = 0; i < lista.size(); i++)
            outputWriter.write( lista.get(i)+", ");

        outputWriter.flush();
        outputWriter.close();
    }

    public static void plotResult(List<Double> codones){
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();;


        Gramatica gramatica = new Gramatica(codones);

        Stats[] stats = executor.runExperiment(gramatica, new GhostAggresive(),1000,"");
        List<Double> results = stats[0].getNormalizedData();
        ArrayList<Double> observed = new ArrayList<>();

        for(int i = 0; i < (int) stats[0].getMax() + 1; i++)
            observed.add(0.0);

        for(double i :results)
            observed.set((int) i, observed.get((int) i) + 1);

        Plot plot = new Plot("Resultados");
        plot.showPlot(observed);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }
}
