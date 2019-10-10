package Testing.Evo.Controladores;

import Testing.Testing;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class MsPacManRandom extends PacmanController {
    private Constants.MOVE last_move;

    public MsPacManRandom(){
        super();
        last_move = Constants.MOVE.NEUTRAL;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        Random rnd = new Random();
        int index = game.getPacmanCurrentNodeIndex();
        Constants.MOVE[] moves = game.getPossibleMoves(index, last_move);

        if(moves != null)
            last_move = moves[rnd.nextInt(moves.length)];
        else last_move = Constants.MOVE.NEUTRAL;

        return last_move;

    }
}
