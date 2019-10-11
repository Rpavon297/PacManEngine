package testing.evo.algoritmo;

import pacman.game.util.Stats;

public class FitnessFunction {
    public static double getFitness(Stats[] stats){
        return stats[0].getAverage();
    }
}
