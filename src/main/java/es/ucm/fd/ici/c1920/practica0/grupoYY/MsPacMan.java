package es.ucm.fd.ici.c1920.practica0.grupoYY;

import pacman.controllers.Controller;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class MsPacMan extends PacmanController {

    private Constants.MOVE last_move;

    public MsPacMan(){
        super();
        last_move = Constants.MOVE.NEUTRAL;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        double limit = 20;
        int from = game.getPacmanCurrentNodeIndex();

        Constants.GHOST nearestGhost = null, nearestEdibleGhost = null;
        double dist = limit, edist = limit;
        int to = -1, eto =  -1;

        //Buscar el fantasma mas cercano y el fantasma comestible mas cercano
        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            int nto = game.getGhostCurrentNodeIndex(ghostType);
            double ndist = game.getDistance(from, nto, Constants.DM.MANHATTAN);

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
            double ndistp = game.getDistance(from,p,last_move, Constants.DM.MANHATTAN);
            if(ndistp < distp || distp == -1) {
                distp = ndistp;
                top = p;
            }
        }
        for(int pp : powerpills){
            double ndistp = game.getDistance(from,pp,last_move, Constants.DM.MANHATTAN);
            if(ndistp < distp || distp == -1) {
                distp = ndistp;
                top = pp;
            }
        }

        if(nearestGhost != null)
            last_move = game.getNextMoveAwayFromTarget(from, to, last_move, Constants.DM.MANHATTAN);
        else if(nearestEdibleGhost != null)
            last_move = game.getApproximateNextMoveTowardsTarget(from, eto,last_move, Constants.DM.MANHATTAN);
        else if(distp != -1)
            last_move = game.getApproximateNextMoveTowardsTarget(from, top, last_move, Constants.DM.MANHATTAN);

        return last_move;
    }
}
