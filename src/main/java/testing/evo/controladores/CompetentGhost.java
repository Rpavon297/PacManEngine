package testing.evo.controladores;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

@SuppressWarnings("restriction")
public final class CompetentGhost extends GhostController {

    private Random rnd = new Random();
    private EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

    /*
     * Comprueba si Ms PacMan está cerca de una power pill.
     * Game: El juego
     * Return: Un par que contiene el índice de la powerpill y la distancia de ella a ms pacman.
     */
    private Pair<Integer, Integer> pacManCloseToPill(Game game) {

        int limit = 20;

        boolean pacManNearPowerPill = false;
        Pair<Integer,Integer> ubicationPill = new Pair<Integer, Integer>(-1,-1);

        if(game.getNumberOfActivePowerPills() > 0) {

            for(int powerPill = 0 ; powerPill < game.getActivePowerPillsIndices().length && !pacManNearPowerPill ; ++powerPill) {

                int distance = game.getManhattanDistance(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices()[powerPill]);

                if(distance <= limit) {
                    pacManNearPowerPill = true;
                    ubicationPill.setFirst(powerPill);
                    ubicationPill.setSecond(distance);
                }

            }
        }


        return ubicationPill;

    }

    /*
     * Devuelve el siguiente movimiento a hacer.
     * Game: El juego
     * timeDue
     * Return: El siguiente movimiento
     */
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {

        moves.clear();

        for (GHOST ghostType : GHOST.values())

            if (game.doesGhostRequireAction(ghostType)) {

                Pair<Integer,Integer> ubicationMsPacman = pacManCloseToPill(game);
                //Si Ms Pacman me puede comer, me alejo.
                if(game.isGhostEdible(ghostType)) {
                    int secondsEdible = game.getGhostEdibleTime(ghostType);

                    if(secondsEdible >= 30) { //tengo que huir
                        int distanceToPacMan = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex());
                        if(distanceToPacMan <= 40) {

                            moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, false));
                        }
                        else {

                            moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, true));
                        }
                    }
                    else {

                        moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, true));
                    }

                }
                //si Ms Pacman está cerca de una ppill
                else if (ubicationMsPacman.getSecond() != -1){
                    //comprobamos la distancia de nuestro fantasma con la pill más cercana a Ms Pacman
                    int distance = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostType), game.getActivePowerPillsIndices()[ubicationMsPacman.getFirst()]);

                    if(distance > ubicationMsPacman.getSecond()) { //huyo si est� cerca de la ppill

                        moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, false));

                    }
                    else { //si estoy m�s cerca que pacman de la ppill voy a por pacman

                        moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, true));

                    }
                }
                else { //en caso contrario a todo, con un 90% voy a por ella, con un 10% hago un move aleatorio
                    if(rnd.nextFloat() < 0.9) {
                        moves.put(ghostType, this.getBestNextMove(game, game.getPacmanCurrentNodeIndex(), ghostType, true));

                    } else {
                        moves.put(ghostType,MOVE.values()[rnd.nextInt(MOVE.values().length)]);

                    }
                }

            }

        return moves;

    }

    /*
     * Devuelve el mejor movimiento posible a realizar
     * game: El juego
     * ghost: el fantasma actual
     * comer: si es true, irá a por ella.
     */
    private MOVE getBestNextMove(Game game, int pacman, GHOST ghost, boolean comer) {
        ArrayList<Pair<Integer, DM>> array = new ArrayList<>();
        int currentGhost = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        array.add(new Pair<>((int) game.getDistance(currentGhost, pacman, lastMove, DM.MANHATTAN), DM.MANHATTAN));
        array.add(new Pair<>((int) game.getDistance(currentGhost, pacman, lastMove, DM.EUCLID), DM.EUCLID));
        array.add(new Pair<>(game.getShortestPathDistance(currentGhost, pacman, lastMove), DM.PATH));
        array.sort((o1, o2) -> {
            // TODO Auto-generated method stub
            if(o1.getFirst() == o2.getFirst()) {
                if(o1.getSecond() == DM.EUCLID)
                    return 1;
                else if(o2.getSecond() == DM.EUCLID)
                    return -1;
                else return 0;
            }
            return o1.getFirst().compareTo(o2.getFirst());
        });

        if(comer)
            return game.getApproximateNextMoveTowardsTarget(currentGhost, pacman, lastMove, array.remove(0).getSecond());
        else return game.getApproximateNextMoveAwayFromTarget(currentGhost, pacman, lastMove, array.remove(0).getSecond());


    }
}