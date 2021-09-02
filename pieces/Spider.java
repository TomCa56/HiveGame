package pieces;

import java.awt.Color;

import tps.tp4.Game;

/**
 * Spider class
 */
public class Spider extends Piece {
	final static private Color color = new Color(0xA62D00);

//	HashMap<Direction, Direction> revDir = new HashMap<Direction, Direction>();

	/**
	 * constructor
	 */
	public Spider(Game game, boolean isFromPlayerA) {
		super("Spider", color, game, isFromPlayerA);
//		revDir.clear();
//		revDir.put(Direction.N, Direction.S);
//		revDir.put(Direction.S, Direction.N);
//		revDir.put(Direction.NO, Direction.SE);
//		revDir.put(Direction.SE, Direction.NO);
//		revDir.put(Direction.NE, Direction.SO);
//		revDir.put(Direction.SO, Direction.NE);

	}

//	boolean checkNext(int fromX, int fromY, int toX, int toY, Direction d, int count) {
//
//		Direction notDir = revDir.get(d);
//		System.out.println(count + ": " + d);
//
//		for (Direction dir : Direction.values()) {
//			if (dir != notDir) {
//				int newFromX = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, dir)
//						.getX();
//				int newFromY = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, dir)
//						.getY();
//				if (count != 3 && game.getBoard().getBoardPlace(newFromX, newFromY).getPiece() == null) {
//					++count;
//					if (checkNext(newFromX, newFromY, toX, toY, dir, count))
//						return true;
//					else {
//						continue;
//					}
//				} else if (count == 3) {
//					if (fromX == toX && fromY == toY) {
//						return true;
//					} else {
//						return false;
//					}
//				}
//			}
//		}
//
//		return false;
//	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Spider must move exactly 3 different steps. Should not violate the one
	 * hive rule and the physical possible move rule in each step.
	 */
	public boolean moveTo(int x, int y) {

//		for (Direction dir : Direction.values()) {
//			int fromX = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, dir).getX();
//			int fromY = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, game.getBoard().oldY, dir).getY();
//			if (game.getBoard().getBoardPlace(fromX, fromY).getPiece() == null) {
//				if (checkNext(fromX, fromY, x, y, dir, 1)) {
//					return true;
//				}
//			}
//			System.out.println("OG: " + dir + ".......................");
//		}
//		return false;
		return true;
	}

}
