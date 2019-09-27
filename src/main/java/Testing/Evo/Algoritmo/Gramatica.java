package Testing.Evo.Algoritmo;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.Arrays;
import java.util.List;

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
    private final int[] DEFAULT_SETTINGS = {2,10,200,10000};
    private final int SEC_SWITCH = 1000;
    private final int N_INST = 10;
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

    public int pog2( Game game, int i, boolean operativa){
        i++;
        i = decode(game, i,  operativa);
        i++;
        return decode(game, i,  operativa);
    }

    public int pog3( Game game, int i, boolean operativa){
        i++;
        i = decode(game, i,  operativa);
        i++;
        i = decode(game, i,  operativa);
        i++;
        return decode(game, i,  operativa);
    }

    public int flee(Game game, int i,  boolean operativa){
        i++;

        if(!operativa){
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

        //Si están alineados en el eje x
        if(fromx <= tox + HOLGURA && fromx >= tox-HOLGURA){
            //Si viene por la izquierda
            if(tox <= fromx) {
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
            //Si viene por la derecha
            else{
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
        }
        //Si están alineados en el eje y
        if(fromy <= toy + HOLGURA && fromy >= toy - HOLGURA){
            //Si está arriba
            if(fromy <= toy){
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

            //si está abajo
            else{
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
        }
        i++;
        //Si no están alineados

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
    }

    public int pill( Game game, int i, boolean operativa){
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

        if(to == -1){
            i = decode(game, i,  false);
            i++;
            return decode(game, i,  operativa);
        }else{
            i = decode(game, i,operativa);
            i++;
            return decode(game, i,  false);
        }
    }

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
    }

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
                return pog2(game, i, operativa);
            case 1:
                return pog3(game, i, operativa);
            case 2:
                return flee(game, i, operativa);
            case 3:
                return edible(game, i, operativa);
            case 4:
                return pill(game, i, operativa);
            case 5:
                return ppill(game, i, operativa);
            case 6:
                if (operativa)
                    this.move = Constants.MOVE.UP;
                return i;
            case 7:
                if (operativa)
                    this.move = Constants.MOVE.DOWN;
                return i;
            case 8:
                if (operativa)
                    this.move = Constants.MOVE.RIGHT;
                return i;
            case 9:
                if (operativa)
                    this.move = Constants.MOVE.LEFT;
                return i;
            default:
                i++;
                return i;
        }
    }



}