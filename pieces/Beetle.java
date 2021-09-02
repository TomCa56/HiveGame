package pieces;

import java.awt.Color;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Beetle class
 */
public class Beetle extends Piece {
	final static private Color color = Color.magenta;

	/**
	 * constructor
	 */
	public Beetle(Game game, boolean isFromPlayerA) {
		super("Beetle", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Beetle can move only one step and be placed on top on another piece(s).
	 * Should not violate the one hive rule.
	 */

	@Override
	public boolean moveTo(int x, int y) {
		//verifica se o neighbour em todas as direções é nulo ou não
		//mov igual à QUEEN - exceto o 'passsar por cima', que é feito no Game
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