package pieces;

import java.awt.Color;

import tps.tp4.Game;

public abstract class Piece {

	final public static int DIMPIECE = 25;

	private boolean isFromPlayerA;
	private int x = -1, y = -1;
	protected Game game;
	private String name;
	private Color color;

	/**
	 * constructor
	 */
	public Piece(String name, Color color, Game game, boolean isFromPlayerA) {
		this.name = name;
		this.color = color;
		this.game = game;
		this.isFromPlayerA = isFromPlayerA;

	}

	/**
	 * toString
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * get color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * get if piece if from player A or not
	 */
	public boolean isFromPlayerA() {
		return isFromPlayerA;

	}

	/**
	 * set xy
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * get x
	 */
	public int getX() {
		return x;
	}

	/**
	 * set y
	 */
	public int getY() {
		return y;
	}

	/**
	 * move this piece to x,y if doesn't violate the rules
	 */
	public abstract boolean moveTo(int x, int y);

	
	//MÉTODOS NÃO USADOS
	
	/**
	 * checks if the x, y received position have one neighbor that is not me
	 * 
	 * protected boolean haveValidNeighbour(int x, int y) { 
	 * 		return game.getBoard().justOneHive(x, y); 
	 * }
	 * 
	 * move one step if it is verify the rules
	 * 
	 * protected boolean moveOneCheckedStep(int x, int y) {
	 * 		return false; 
	 * }
	 * 
	 * 
	 * move to the destination if the move from the current position to the destiny
	 * doesn't violate the one hive rule. It can move several steps.
	 * //FEITO NO GAME 
	 * protected boolean moveWithOnehiveRuleChecked(int x, int y) {
	 * 		return false; 
	 * }
	 * 
	 * get the direction from start coordinates to destiny coordinates
	 * 
	 * protected static Direction getDirection(int fromX, int fromY, int toX, int toY) {
	 * 		for (Direction d : Direction.values()) { 
	 * 			Point p = Board.getNeighbourPoint(fromX, fromY, d); 
	 * 			if (p == null) continue; if (p.x == toX && p.y == toY) { 
	 * 				return d; 
	 * 			} 
	 * 		} 
	 * 		return null; 
	 * }
	 * 
	 */

}
