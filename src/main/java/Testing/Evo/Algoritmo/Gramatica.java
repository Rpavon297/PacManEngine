package Testing.Evo.Algoritmo;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.List;

import static pacman.game.Constants.MOVE.*;

/**
 * condition-statement ::= if( condition ) { statement } else { statement } | if( condition ) { statement }
 *
 * statement ::= condition-statement | terminal
 *
 * condition ::= isGhostClose | isPowerPillClose | isEdibleGhostClose
 *
 * terminal ::= fleeClosestGhost | eatCloserPowerPill | chaseCloserGhost | goToClosestPill
 */

public class Gramatica extends PacmanController {
    //  CONSTANTES GENETICAS
    private final int NCSTATEMENT = 2;
    private final int NSTATEMENT = 2;
    private final int NCONDITION = 3;
    private final int NTERMINAL = 4;
    //  CONSTANTES DEL JUEGO
    private final int FLEE = 30;
    private final int CHASE = 20;
    private final int PPILL = 29;
    private final Constants.DM DISTANCE = Constants.DM.PATH;

    private int wraps;
    private int maxWraps;
    private List<Double> codones;
    private Constants.MOVE move;
    private Game game;
    private int last_position;

    public Gramatica(List<Double> lista) {
        wraps = 0;
        maxWraps = 2;

        this.codones = lista;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.game = game;

        conditionStatement(0, true);

        return move;
    }

    private int conditionStatement(int i, boolean operative){
        int instruction = codones.get(i).intValue() % NCSTATEMENT;

        i = checkWraps(i);

        if(wraps < maxWraps){
            switch (instruction){
                case 0:
                    if(condition(i)){
                        i = statement(i, operative);
                        i = checkWraps(i);
                        i = statement(i, false);
                    }else{
                        statement(i, false);
                        i = checkWraps(i);
                        statement(i, operative);
                    }

                    break;
                case 1:
                    if(condition(i))
                        i = statement(i, operative);
                    else
                        i = statement(i,false);

                    break;
            }
        }

        return i;
    }

    private int statement(int i, boolean operative){
        int instruction = codones.get(i).intValue() % NSTATEMENT;

        i = checkWraps(i);

        if(wraps < maxWraps){

            switch (instruction){
                case 0:
                    i = conditionStatement(i,operative);
                    break;
                case 1:
                    i = terminal(i,operative);
                    break;
            }
        }

        return i;
    }

    private int terminal(int i, boolean operative){
        int terminal = codones.get(i).intValue() % NTERMINAL;

        i = checkWraps(i);

        if(wraps < maxWraps) {

            switch (terminal) {
                case 0:
                    if (operative)
                        chaseClosestGhost();
                    break;
                case 1:
                    if (operative)
                        fleeClosestGhost();
                    break;
                case 2:
                    if (operative)
                        eatCloserPPill();
                    break;
                case 3:
                    if (operative)
                        eatCloserPill();
            }
        }

        return i;
    }

    private boolean condition(int i){
        int condition = codones.get(i).intValue() % NCONDITION;

        switch (condition){
            case 0:
                return isGhostClose();
            case 1:
                return isPowerPillClose();
            case 2:
                return isEdibleGhostClose();
            default:
                return false;
        }
    }


    //CONDITIONS
    private boolean isGhostClose(){
        for (Constants.GHOST ghost : Constants.GHOST.values()){
            if(game.getGhostLairTime(ghost) == 0 && !game.isGhostEdible(ghost) &&
                    game.getDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DISTANCE) < FLEE)
                return true;
        }

        return false;
    }

    private boolean isPowerPillClose(){
        for(int index : game.getPowerPillIndices()){
            if(game.getDistance(game.getPacmanCurrentNodeIndex(), index, DISTANCE) < PPILL)
                return true;
        }

        return false;
    }

    private boolean isEdibleGhostClose(){
        for (Constants.GHOST ghost : Constants.GHOST.values()){
            if(game.getGhostLairTime(ghost) == 0 && game.isGhostEdible(ghost) &&
                    game.getDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DISTANCE) < CHASE)
                return true;
        }

        return false;
    }

    //TERMINALS
    private void fleeClosestGhost(){
        double closestDistance = Double.MAX_VALUE;
        Constants.GHOST closestGhost = null;

        for (Constants.GHOST ghost : Constants.GHOST.values()){
            double distance =  game.getDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DISTANCE);

            if(game.getGhostLairTime(ghost) == 0 && game.isGhostEdible(ghost) && distance < closestDistance){
                closestDistance = distance;
                closestGhost = ghost;
            }
        }

        if(closestGhost != null)
            this.move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), DISTANCE);
    }

    private void chaseClosestGhost(){
        double closestDistance = Double.MAX_VALUE;
        Constants.GHOST closestGhost = null;

        for (Constants.GHOST ghost : Constants.GHOST.values()){
            double distance =  game.getDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DISTANCE);

            if(game.getGhostLairTime(ghost) == 0 && !game.isGhostEdible(ghost) && distance < closestDistance){
                closestDistance = distance;
                closestGhost = ghost;
            }
        }

        if(closestGhost != null)
            this.move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), DISTANCE);
    }

    private void eatCloserPPill(){
        double closestDistance = Double.MAX_VALUE;
        int closestIndex = -1;

        for(int index : game.getPowerPillIndices()){
            double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), index, DISTANCE);

            if(distance < closestDistance){
                closestDistance = distance;
                closestIndex = index;
            }
        }

        if(closestIndex != -1)
            this.move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestIndex,DISTANCE);

    }

    private void eatCloserPill(){
        double closestDistance = Double.MAX_VALUE;
        int closestIndex = -1;

        for(int index : game.getActivePillsIndices()){
            double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), index, DISTANCE);

            if(distance < closestDistance){
                closestDistance = distance;
                closestIndex = index;
            }
        }

        if(closestIndex != -1)
            this.move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestIndex,DISTANCE);
    }
    //  CHECKS APPROPIATE CODON IN USE
    private int checkWraps(int i){
        i++;

        if(i == codones.size()){
            i = 0;
            wraps++;
        }
        return i;
    }
}
