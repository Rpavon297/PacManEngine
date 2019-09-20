package Testing.Evo;

import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.Arrays;
import java.util.List;

public class Gramatica {
    /**
     *       <S> = <exp>
     *       <exp> = <pog2> <pog3> <ghost> <edible> <pill> <ppill> <arriba> <abajo> <dcha> <izda>
     *
     *               <ghostX>    = if (fantasma cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *               <edibleX>   = if (fantasma comestible cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *               <pillX>     = if (pill cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *
     */
    private final int[] DEFAULT_SETTINGS = {2,10,200,10};
    private final int N_INST = 10;
    private final int HOLGURA = 2;
    private final Constants.DM DM = Constants.DM.PATH;
    private int wraps;
    private int maxWraps;
    private int fleeDist;
    private int chaseDist;
    private int pillDist;

    public Gramatica(){
        wraps = 0;
        maxWraps = DEFAULT_SETTINGS[0];
        fleeDist = DEFAULT_SETTINGS[1];
        chaseDist = DEFAULT_SETTINGS[2];
        pillDist = DEFAULT_SETTINGS[3];
    }

    public Gramatica(int maxWraps, int fleeDist, int chaseDist, int pillDist){
        this.wraps = 0;
        this.maxWraps = maxWraps;
        this.fleeDist = fleeDist;
        this.chaseDist = chaseDist;
        this.pillDist = pillDist;
    }

    public void S(List<Double> codones, Game game){
        int instruc = codones.get(0).intValue() % N_INST;

        int i = decode(codones, game, 0, Constants.MOVE.NEUTRAL, true);
    }

    public int pog2(List<Double> codones, Game game, int i, Constants.MOVE move, boolean operativa){
        i++;
        i = decode(codones, game, i, move, operativa);
        i++;
        return decode(codones, game, i, move, operativa);
    }

    public int pog3(List<Double> codones, Game game, int i,Constants.MOVE move, boolean operativa){
        i++;
        i = decode(codones, game, i, move, operativa);
        i++;
        i = decode(codones, game, i, move, operativa);
        i++;
        return decode(codones, game, i, move, operativa);
    }

    public int ghost(List<Double> codones, Game game, int i, Constants.MOVE move, boolean operativa){
        i++;

        if(!operativa){
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            return decode(codones, game, i, move, false);
        }

        double dist = fleeDist;
        int from = game.getPacmanCurrentNodeIndex();
        Constants.GHOST cercano = null;

        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            double ndist = game.getDistance(from, game.getGhostCurrentNodeIndex(ghostType),DM);
            if(ndist < dist){
                dist = ndist;
                cercano = ghostType;
            }
        }

        int fromx = game.getNodeXCood(from),
                fromy = game.getNodeYCood(from);

        int tox = game.getNodeXCood(game.getGhostCurrentNodeIndex(cercano)),
                toy = game.getNodeYCood(game.getGhostCurrentNodeIndex(cercano));

        List<Constants.MOVE> posibles = Arrays.asList(game.getPossibleMoves(from));

        //Si están alineados en el eje x
        if(fromx <= tox + HOLGURA && fromx >= tox-HOLGURA){
            //Si viene por la izquierda
            if(tox <= fromx) {
                if(posibles.contains(Constants.MOVE.RIGHT))
                    move = Constants.MOVE.RIGHT;
                else{
                    posibles.remove(Constants.MOVE.LEFT);
                    if(posibles.size() > 0) move = posibles.get(0);
                    else move = Constants.MOVE.LEFT;
                }

                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, operativa);
                i++;
                return decode(codones, game, i, move, false);
            }
            //Si viene por la derecha
            else{
                if(posibles.contains(Constants.MOVE.LEFT))
                    move = Constants.MOVE.LEFT;
                else{
                    posibles.remove(Constants.MOVE.RIGHT);
                    if(posibles.size()>0) move = posibles.get(0);
                    else move = Constants.MOVE.RIGHT;
                }

                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, operativa);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                return decode(codones, game, i, move, false);
            }
        }
        //Si están alineados en el eje y
        if(fromy <= toy + HOLGURA && fromy >= toy - HOLGURA){
            //Si está arriba
            if(fromy <= toy){
                if(posibles.contains(Constants.MOVE.DOWN))
                    move = Constants.MOVE.DOWN;
                else{
                    posibles.remove(Constants.MOVE.UP);
                    if(posibles.size() > 0) move =  posibles.get(0);
                    else move = Constants.MOVE.UP;
                }

                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, operativa);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                return decode(codones, game, i, move, false);
            }

            //si está abajo
            else{
                if(posibles.contains((Constants.MOVE.UP)))
                    move = Constants.MOVE.UP;
                else{
                    posibles.remove(Constants.MOVE.DOWN);
                    if(posibles.size() > 0) move = posibles.get(0);
                    else move = Constants.MOVE.DOWN;
                }

                i = decode(codones, game, i, move, operativa);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                i = decode(codones, game, i, move, false);
                i++;
                return decode(codones, game, i, move, false);
            }
        }
        i++;
        //Si no están alineados
        move = game.getNextMoveAwayFromTarget(from,game.getGhostCurrentNodeIndex(cercano),DM);

        i = decode(codones, game, i, move, false);
        i++;
        i = decode(codones, game, i, move, false);
        i++;
        i = decode(codones, game, i, move, false);
        i++;
        i = decode(codones, game, i, move, false);
        i++;
        return decode(codones, game, i, move, operativa);
    }

    public int edible(List<Double> codones, Game game, int i, Constants.MOVE move, boolean operativa){
        i++;

        if(!operativa){
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            i = decode(codones, game, i, move, false);
            i++;
            return decode(codones, game, i, move, false);
        }

        double dist = chaseDist;
        int from = game.getPacmanCurrentNodeIndex();
        Constants.GHOST cercano = null;

        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            double ndist = game.getDistance(from, game.getGhostCurrentNodeIndex(ghostType),DM);
            if(ndist < dist){
                dist = ndist;
                cercano = ghostType;
            }
        }
    }

    public int pill(List<Double> codones, Game game, int i,Constants.MOVE move, boolean operativa){
        return 0;
    }

    public int ppill(List<Double> codones, Game game, int i, Constants.MOVE move, boolean operativa){
        return 0;
    }

    public int decode(List<Double> codones, Game game, int i, Constants.MOVE move, boolean operativa){
        if(i >= codones.size()){
            i = 0;
            this.wraps++;
        }
        if(this.wraps == this.maxWraps)
            return i;
        int instruc = codones.get(0).intValue() % N_INST;

        switch(instruc){
            case 0:
                return pog2(codones, game, i, move, operativa);
            case 1:
                return pog3(codones, game, i, move, operativa);
            case 2:
                return ghost(codones, game, i, move, operativa);
            case 3:
                return edible(codones, game, i, move, operativa);
            case 4:
                return pill(codones, game, i, move, operativa);
            case 5:
                return ppill(codones, game, i, move, operativa);
            case 6:
                if(operativa)
                    game.updatePacMan(move);
                return i;
            default:
                return i;
        }
    }
}