package pieces;

import java.awt.Color;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Grasshopper class
 */
public class Grasshopper extends Piece {
	final static private Color color = new Color(70, 90, 40);

	/**
	 * constructor
	 */
	public Grasshopper(Game game, boolean isFromPlayerA) {
		super("Grasshopper", color, game, isFromPlayerA);
		
	}

	boolean straightSearch(int fromX, int fromY, int toX, int toY, Direction d) {
		game.getBoard();
		//procura todos as peças em linha reta até encontrar o XY ou um null
		int X = (int) Board.getNeighbourPoint(fromX, fromY, d).getX();
		game.getBoard();
		int Y = (int) Board.getNeighbourPoint(fromX, fromY, d).getY();

		if (game.getBoard().getBoardPlace(X, Y).getPiece() == null) {
			if (X == toX && Y == toY) {
				return true;
			} else {
				return false;
			}
		} else {
			return true && straightSearch(X, Y, toX, toY, d);
		}
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Grasshopper must move in strait jumps over at least one piece (but not
	 * empty places). Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {

		for (Direction d : Direction.values()) {

			game.getBoard();
			int rX = (int) Board.getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, d).getX();
			game.getBoard();
			int rY = (int) Board.getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, d).getY();

			// valida raio e decide dir - Grasshopper não se pode mover numa direção cujo neighbour seja null

			if (game.getBoard().getBoardPlace(rX, rY).getPiece() != null) {
				if (straightSearch(rX, rY, x, y, d)) {
					return true;
				}
			}
		}

		return false;

	}

}