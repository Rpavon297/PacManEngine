package es.ucm.fd.ici.c1920.practica0.grupoYY;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;


public class Ghosts extends GhostController {

    private EnumMap<Constants.GHOST, Constants.MOVE> moves = new EnumMap<>(Constants.GHOST.class);

    private final int FLEE_DIST = 4;


    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        if(moves.isEmpty()) {
            for (Constants.GHOST ghostType : Constants.GHOST.values()) {
                moves.put(ghostType, Constants.MOVE.NEUTRAL);
            }
        }

        int to = game.getPacmanCurrentNodeIndex();
        int pp[] = game.getActivePowerPillsIndices();


        for (Constants.GHOST ghostType : Constants.GHOST.values()) {
            try {
                if (game.doesGhostRequireAction(ghostType)) {
                    boolean flee = game.isGhostEdible(ghostType);
                    Random rnd = new Random();
                    int from = game.getGhostCurrentNodeIndex(ghostType);

                    Constants.MOVE move = Constants.MOVE.NEUTRAL;

                    for (int pill : pp) {
                        if (game.getDistance(to, pill, Constants.DM.MANHATTAN) < FLEE_DIST)
                            flee = true;
                    }
                    if (flee)
                        move = game.getApproximateNextMoveAwayFromTarget(from, to, moves.get(ghostType),
                                Constants.DM.MANHATTAN);
                    else {
                        if (rnd.nextFloat() < 0.9) {

                            move = game.getApproximateNextMoveTowardsTarget(from, to, moves.get(ghostType),
                                    Constants.DM.MANHATTAN);


                        }
                        else
                            move = Constants.MOVE.values()[rnd.nextInt(Constants.MOVE.values().length)];
                    }
                    moves.put(ghostType, move);
                }
            }catch(Exception e){
                System.out.println("bug: " + ghostType + " " + moves.get(ghostType) + " desde: "
                        + game.getGhostCurrentNodeIndex(ghostType) + " a: " + to + " Movimientos: " + moves) ;
            }
        }



        return moves;
    }
}
