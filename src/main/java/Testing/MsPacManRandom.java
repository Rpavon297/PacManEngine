package Testing;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class MsPacManRandom extends PacmanController {
    private Constants.MOVE last_move = Constants.MOVE.NEUTRAL;

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        Random rnd = new Random();
        int index = game.getPacmanCurrentNodeIndex();
        Constants.MOVE[] moves = game.getPossibleMoves(index, last_move);

        if(moves.length > 0)
            last_move = moves[rnd.nextInt(moves.length)];
        else last_move = Constants.MOVE.NEUTRAL;
        return last_move;
    }
}
