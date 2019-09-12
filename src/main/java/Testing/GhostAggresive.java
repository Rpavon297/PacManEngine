package Testing;

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

        for(Constants.GHOST ghostType : Constants.GHOST.values()){
            if(game.doesGhostRequireAction(ghostType)){
                Constants.MOVE last_move = last_moves.get(ghostType);
            }
        }
        game.getApproximateNextMoveTowardsTarget();
    }
}
