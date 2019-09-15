package Testing;

import es.ucm.fd.ici.c1920.practica0.grupoYY.Ghosts;
import es.ucm.fd.ici.c1920.practica0.grupoYY.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.EnumMap;
import java.util.Random;

public class Testing {
    public static void main(String[] args){
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

        GhostController ghost = new Ghosts();
        PacmanController pacman = new MsPacMan();
        try {
            System.out.println(executor.runGame(pacman, ghost, 50));
        }catch(Exception e){
            System.out.println("");
        }
    }
}