package Testing.Evo.Plotting;

import Testing.Evo.Algoritmo.Gramatica;

import javax.swing.*;
import java.util.List;


public class Diagnostico extends JFrame {

    public static void mostrarGrafica( List<Double> mejores) {
        double[] y = new double[mejores.size()];
        double[] x =  new double[mejores.size()];

        for(int i = 0; i < mejores.size(); i++){
            y[i] = i;
            x[i] = mejores.get(i);
        }

        MatlabChart fig = new MatlabChart();
        fig.plot(x, y, "-r", 2.0f, "AAPL");
        fig.RenderPlot();
        fig.title("Mejor de cada generación:");
        fig.xlim(10, 100);
        fig.ylim(200, 300);
        fig.xlabel("Generaciones");
        fig.ylabel("Puntuación");

    }
}
