package Testing;

import es.ucm.fd.ici.c1920.practica0.RobertoPavon.Ghosts;
import es.ucm.fd.ici.c1920.practica0.RobertoPavon.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

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
