package Testing;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;

public class Testing {
    public static void main(String[] args){
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

        GhostController ghost = new GhostController() {
            private EnumMap<Constants.GHOST, Constants.MOVE> moves = new EnumMap<>(Constants.GHOST.class);
            private Constants.MOVE[] allMoves = Constants.MOVE.values();
            private Random rnd = new Random();

            @Override
            public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
                moves.clear();
                for(Constants.GHOST ghostType : Constants.GHOST.values()){
                    if(game.doesGhostRequireAction(ghostType)){
                        moves.put(ghostType,allMoves[rnd.nextInt(allMoves.length)]);
                    }
                }

                return moves;
            }
        };
        /*
        PacmanController pacman = new PacmanController() {
            private Random rnd = new Random();
            private Constants.MOVE[] allMoves = Constants.MOVE.values();

            @Override
            public Constants.MOVE getMove(Game game, long timeDue) {
                return allMoves[rnd.nextInt(allMoves.length)];
            }
        };*/
        PacmanController pacman = new MsPacManRandom();
        System.out.println(executor.runGame(pacman,ghost,50));
    }
}
