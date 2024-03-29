package es.ucm.fd.ici.c1920.practica0.RobertoPavon;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

    private Constants.MOVE last_move;

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        if(last_move == null)
            last_move = Constants.MOVE.NEUTRAL;
        double limit = 20;
        int from = game.getPacmanCurrentNodeIndex();

        Constants.GHOST nearestGhost = null, nearestEdibleGhost = null;
        double dist = limit, edist = limit;
        int to = -1, eto =  -1;

        //Buscar el fantasma mas cercano y el fantasma comestible mas cercano
        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            int nto = game.getGhostCurrentNodeIndex(ghostType);
            double ndist = game.getDistance(from, nto, Constants.DM.PATH);

                if(!game.isGhostEdible(ghostType) && ndist < dist) {
                    dist = ndist;
                    nearestGhost = ghostType;
                    to = nto;
                }else if(game.isGhostEdible(ghostType) && ndist < edist){
                    edist = ndist;
                    nearestEdibleGhost = ghostType;
                    eto = nto;
                }
        }
        //Buscar las pildoras mas cercanas

        int pills[] = game.getActivePillsIndices();
        int powerpills[] = game.getActivePowerPillsIndices();

        double distp = -1;
        int top = -1;

        for(int p : pills){
            double ndistp = game.getDistance(from,p,last_move, Constants.DM.PATH);
            if(ndistp < distp || distp == -1) {
                distp = ndistp;
                top = p;
            }
        }
        for(int pp : powerpills){
            double ndistp = game.getDistance(from,pp,last_move, Constants.DM.PATH);
            if(ndistp < distp || distp == -1) {
                distp = ndistp;
                top = pp;
            }
        }


        if(nearestGhost != null)
            last_move = game.getNextMoveAwayFromTarget(from, to, Constants.DM.PATH);
        else if(nearestEdibleGhost != null)
            last_move = game.getApproximateNextMoveTowardsTarget(from, eto, last_move, Constants.DM.PATH);
        else if(distp != -1)
            last_move = game.getApproximateNextMoveTowardsTarget(from, top, last_move, Constants.DM.PATH);

        return last_move;
    }
}
