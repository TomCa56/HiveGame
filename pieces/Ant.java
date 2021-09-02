package pieces;

import java.awt.Color;

import tps.tp4.Game;

/**
 * Ant class
 */
public class Ant extends Piece {
	final static private Color color = Color.blue;

	/**
	 * constructor
	 */
	public Ant(Game game, boolean isFromPlayerA) {
		super("Ant", color, game, isFromPlayerA);

	}

	/**
	 * move this piece to x,y if doesn't violate the rules
	 * 
	 * The Ant must move any numbers of steps. Should not violate the one hive rule
	 * and the physical possible move rule in each step.
	 */
	public boolean moveTo(int x, int y) {
		//MOVIMENTO DA FORMIGA CORRESPONDE AO MOVIMENTO GEN�RICO COM AS RESTRI��ES GERAIS A TODAS AS PE�AS 
		//ASSIM A FORMIGA J� SE MOVE DE FORMA CONDICIONADA SEM A NECESSIDADE DO moveTo
		return true;
	}

	//M�TODOS N�O USADOS
	
	/**
	 * Find if current Ant can move in any number of steps to the final position.
	 * The Spider should try all the paths. But must prevent loops, For that, it
	 * receives a ArrayList with the BoardPlaces that we already moved. If the new
	 * one is already there, that means that is a loop, so it must abandon that
	 * path.
	 * 
	 * private boolean findPlace(int nextX, int nextY, int xFinal, int yFinal,
	 * Direction lastDirection, ArrayList<BoardPlace> pathList) {
	 * 		return false;
	 * }
	 * 
	 */
	
}