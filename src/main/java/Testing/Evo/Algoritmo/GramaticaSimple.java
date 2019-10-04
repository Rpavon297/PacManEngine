package Testing.Evo.Algoritmo;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.List;

import static pacman.game.Constants.MOVE.*;

//  Bot basado en estados simple

public class GramaticaSimple extends PacmanController {
    private final int NINST = 8;

    private int wraps;
    private int maxWraps;
    private int status;
    private List<Double> codones;

    public GramaticaSimple(List<Double> lista) {
        wraps = 0;
        status = -1;
        maxWraps = 2;

        this.codones = lista;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        return decode();
    }

    private Constants.MOVE decode(){
        status++;
        if(status == codones.size())
            status = 0;
        int ninst = codones.get(status).intValue() % NINST;

        switch(ninst){
            case 0:
                return UP;
            case 1:
                return DOWN;
            case 2:
                return LEFT;
            case 3:
                return RIGHT;
            default:
                return null;

        }
    }
}
