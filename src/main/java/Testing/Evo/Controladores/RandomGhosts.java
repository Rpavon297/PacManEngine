package Testing.Evo.Controladores;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.EnumMap;
import java.util.Random;

public class RandomGhosts extends GhostController {
    private EnumMap<Constants.GHOST, Constants.MOVE> moves;

    public RandomGhosts(){
        this.moves = new EnumMap<Constants.GHOST, Constants.MOVE>(Constants.GHOST.class);
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        this.moves.clear();
        Random rnd = new Random();

        for (Constants.GHOST ghost : Constants.GHOST.values()) {

            if (game.doesGhostRequireAction(ghost)) {
                Constants.MOVE[] moves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
                this.moves.put(ghost, moves[rnd.nextInt(moves.length)]);
            }
        }
        return moves;
    }
}
