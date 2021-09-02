package pieces;

import java.awt.Color;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * QueenBee class
 */
public class QueenBee extends Piece {
	final static private Color color = Color.yellow;

	/**
	 * constructor
	 */
	public QueenBee(Game game, boolean isFromPlayerA) {
		super("QueenBee", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The QueenBee can move only one step. Should not violate the one hive rule and
	 * the physical possible move rule.
	 * 
	 */
	@Override
	public boolean moveTo(int x, int y) {
		//verifica se o neighbour em todas as direções é nulo ou não
		for (Direction d : Direction.values()) {
			game.getBoard();
			game.getBoard();
			if (game.getBoard().getBoardPlace(Board.getNeighbourPoint(x, y, d).x,
					Board.getNeighbourPoint(x, y, d).y) == game.getBoard().getBoardPlace(game.getBoard().oldX,
							game.getBoard().oldY)) {
				return true;
			}
		}

		return false;

	}

}