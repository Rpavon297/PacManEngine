package Testing.Evo.Algoritmo;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static pacman.game.Constants.MOVE.DOWN;

public class Gramatica extends PacmanController {
    /**
     *       <S> = <exp>
     *       <exp> = <pog2> <pog3> <flee> <edible> <pill> <ppill> <arriba> <abajo> <dcha> <izda>
     *
     *               <ghostX>    = if (fantasma cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *               <edibleX>   = if (fantasma comestible cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *               <pillX>     = if (pill cerca en direccion X) <exp> if(direccion Y) <exp> ...
     *
     */
    private final int[] DEFAULT_SETTINGS = {2,10,200,100000};
    private final int SEC_SWITCH = 1000;
    private final int N_INST = 7;
    private final int HOLGURA = 2;
    private final Constants.DM DM = Constants.DM.PATH;
    private int wraps;
    private int maxWraps;
    private int fleeDist;
    private int chaseDist;
    private int pillDist;
    private int flow_index;
    private List<Double> codones;

    private Constants.MOVE move;

    public Gramatica(){
        wraps = 0;
        flow_index = 0;
        maxWraps = DEFAULT_SETTINGS[0];
        fleeDist = DEFAULT_SETTINGS[1];
        chaseDist = DEFAULT_SETTINGS[2];
        pillDist = DEFAULT_SETTINGS[3];
        move = Constants.MOVE.NEUTRAL;
    }

    public Gramatica(List<Double> codones){
        wraps = 0;
        maxWraps = DEFAULT_SETTINGS[0];
        fleeDist = DEFAULT_SETTINGS[1];
        chaseDist = DEFAULT_SETTINGS[2];
        pillDist = DEFAULT_SETTINGS[3];
        move = Constants.MOVE.NEUTRAL;
        this.codones = codones;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        int i = decode(game, 0, true);
        return move;
    }

    public Gramatica(int maxWraps, int fleeDist, int chaseDist, int pillDist){
        this.wraps = 0;
        this.maxWraps = maxWraps;
        this.fleeDist = fleeDist;
        this.chaseDist = chaseDist;
        this.pillDist = pillDist;
        move = Constants.MOVE.NEUTRAL;
    }

    /*
        //Encadena dos funciones
        public int pog2( Game game, int i, boolean operativa){
            i++;
            i = decode(game, i,  operativa);
            i++;
            return decode(game, i,  operativa);
        }

        //Encadena tres funciones
        public int pog3( Game game, int i, boolean operativa){
            i++;
            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  operativa);
            i++;
            return decode(game, i,  operativa);
        }
    */

    //Comprueba donde está el fantasma mas cercano
    public int flee(Game game, int i,  boolean operativa){
        i++;

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

        //Si no hay fantasmas cerca
        if(cercano == null){

            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        int fromx = game.getNodeXCood(from),
                fromy = game.getNodeYCood(from);

        int tox = game.getNodeXCood(game.getGhostCurrentNodeIndex(cercano)),
                toy = game.getNodeYCood(game.getGhostCurrentNodeIndex(cercano));

        //Si viene por la izquierda arriba
        if(tox <= fromx && toy <= fromy) {
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            return decode(game, i,  false);
        }
        //Si viene por la derecha arriba
        if(tox > fromx && toy <= fromy){
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        //Si está abajo izquierda
        if(tox <= fromx){
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        //si está abajo derecha
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  operativa);
        i++;
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  false);
        i++;
        return decode(game, i,  false);


    }

    //Comprueba dodne está la pill mas cercana
    public int pill(Game game, int i, boolean operativa){
        i++;
        int pills[] = game.getActivePillsIndices();
        double distp = -1;
        int to = -1, from = game.getPacmanCurrentNodeIndex();

        for(int p : pills){
            double ndistp = game.getDistance(from,p, DM);

            if(ndistp <= this.pillDist && (ndistp < distp || distp == -1)) {
                distp = ndistp;
                to = p;
            }
        }

        int fromx = game.getNodeXCood(from),
                fromy = game.getNodeYCood(from),
                tox = game.getNodeXCood(to),
                toy = game.getNodeYCood(to);

        //Si viene por la izquierda arriba
        if(tox <= fromx && toy <= fromy) {
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            return decode(game, i,  false);
        }
        //Si viene por la derecha arriba
        if(tox > fromx && toy <= fromy){
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        //Si está abajo izquierda
        if(tox <= fromx){
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  operativa);
            i++;
            i = decode(game, i,  false);
            i++;
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        //si está abajo derecha
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  operativa);
        i++;
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  false);
        i++;
        i = decode(game, i,  false);
        i++;
        return decode(game, i,  false);
    }

    /*
    public int edible( Game game, int i, boolean operativa){
        i++;

        if(!operativa){
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  false);
        }

        double dist = chaseDist;
        int from = game.getPacmanCurrentNodeIndex();
        Constants.GHOST cercano = null;

        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            double ndist = game.getDistance(from, game.getGhostCurrentNodeIndex(ghostType),DM);
            if(ndist < dist && game.isGhostEdible(ghostType)){
                dist = ndist;
                cercano = ghostType;
            }
        }

        if(cercano != null){
            i = decode(game, i, operativa);
            i++;
            return decode(game, i ,  false);
        }else{
            i = decode(game, i,  false);
            i++;
            return decode(game, i ,  operativa);
        }
    }*/

    //Comprueba si tiene un obstáculo delante
    public int blocked( Game game, int i, boolean operativa){
        i++;
        List<Constants.MOVE> moves = asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
        Constants.MOVE lastMove = game.getPacmanLastMoveMade();

        switch(lastMove){
            case UP:
                if(moves.contains(Constants.MOVE.UP)){
                    i = decode(game, i,  operativa);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    return decode(game, i,  false);
                }else{
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  operativa);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    return decode(game, i,  false);
                }
            case DOWN:
                if(moves.contains(Constants.MOVE.DOWN)){
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  operativa);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    return decode(game, i,  false);
                }else{
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  operativa);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    return decode(game, i,  false);
                }
            case LEFT:
                if(moves.contains(Constants.MOVE.LEFT)){
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  operativa);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                return decode(game, i,  false);
            }else{
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  false);
                i++;
                i = decode(game, i,  operativa);
                i++;
                i = decode(game, i,  false);
                i++;
                return decode(game, i,  false);
            }
            case RIGHT:
                if(moves.contains(Constants.MOVE.RIGHT)){
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  operativa);
                    i++;
                    return decode(game, i,  false);
                }else{
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    i = decode(game, i,  false);
                    i++;
                    return decode(game, i,  operativa);
                }
            default:
                return decode(game,i,operativa);
        }
    }

    /*
    public int ppill(Game game, int i, boolean operativa){
        i++;
        int pills[] = game.getActivePowerPillsIndices();
        double distp = -1;
        int to = -1, from = game.getPacmanCurrentNodeIndex();

        for(int p : pills){
            double ndistp = game.getDistance(from,p, DM);

            if(ndistp <= this.pillDist && (ndistp < distp || distp == -1)) {
                distp = ndistp;
                to = p;
            }
        }

        if(to == -1){
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  operativa);
        }else{
            i = decode(game, i, operativa);
            i++;
            return decode(game, i,  false);
        }
    }*/

    public int decode( Game game, int i, boolean operativa){
        flow_index ++;
        if(flow_index > SEC_SWITCH)
            return i;

        if (i >= codones.size()) {
            i = 0;
            this.wraps++;
        }
        if (this.wraps == this.maxWraps)
            return i;
        int instruc = codones.get(i).intValue() % N_INST;

        switch (instruc) {
            case 0:
                return flee(game, i, operativa);
            case 1:
                return blocked(game, i, operativa);
            case 2:
                if (operativa)
                    this.move = Constants.MOVE.UP;
                return i;
            case 3:
                if (operativa)
                    this.move = DOWN;
                return i;
            case 4:
                if (operativa)
                    this.move = Constants.MOVE.RIGHT;
                return i;
            case 5:
                if (operativa)
                    this.move = Constants.MOVE.LEFT;
                return i;
            case 6:
                return pill(game, i, operativa);
            default:
                i++;
                return i;
        }
    }



}