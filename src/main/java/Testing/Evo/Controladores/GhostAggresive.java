package Testing.Evo.Controladores;

import Testing.Testing;
import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;

public class GhostAggresive extends GhostController {
    private EnumMap<Constants.GHOST, Constants.MOVE> last_moves = new EnumMap<>(Constants.GHOST.class);
    private EnumMap<Constants.GHOST, Constants.MOVE> moves = new EnumMap<>(Constants.GHOST.class);
    private Random rnd = new Random();

    public GhostAggresive(){
        super();
        for(Constants.GHOST ghostType : Constants.GHOST.values())
                last_moves.put(ghostType,Constants.MOVE.NEUTRAL);


    }
    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        moves.clear();

        for (Constants.GHOST ghostType : Constants.GHOST.values()) {

            if (game.doesGhostRequireAction(ghostType)) {
                Constants.MOVE last_move = last_moves.get(ghostType);
                int to = game.getPacmanCurrentNodeIndex();
                int from = game.getGhostCurrentNodeIndex(ghostType);

                Constants.MOVE next_move;

                try {
                    next_move = game.getApproximateNextMoveTowardsTarget(from, to, last_move, Constants.DM.MANHATTAN);
                }catch (NullPointerException e){
                    next_move = Constants.MOVE.NEUTRAL;
                }
                moves.put(ghostType, next_move);
                last_moves.put(ghostType,next_move);
            }
        }

        return moves;
    }

}
