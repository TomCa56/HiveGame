package tps.tp4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

import pieces.Piece;

public class BoardPlace {

	private static Color PIECEBACKGROUNDCOLOR = new Color(0x9CCF3A);

	public static int STARTXY = 5;

	// the place to have the pieces on this board place
	// pieces must be added at the tail, and the only accessible piece must be
	// the tail piece
	public ArrayList<Piece> pieces = new ArrayList<Piece>();

	// board reference
	@SuppressWarnings("unused")
	private Board board;

	// the board place coordinates
	int x, y;

	// the polygon for this board place
	Polygon polygon = new Polygon();

	// the selection polygon for this board place
	Polygon selPolygon = new Polygon();

	// the base xy from the board for this place
	private int baseX;
	private int baseY;

	// Methods ============================================

	/**
	 * constructor
	 */
	public BoardPlace(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;

		// base data for polygons
		baseX = STARTXY + (int) (x * Piece.DIMPIECE * 0.75);
		baseY = STARTXY + (int) (y * Piece.DIMPIECE);

		if (x % 2 == 1) {
			baseY += Piece.DIMPIECE / 2;
		}

		// build polygon for this board place
		polygon.addPoint(baseX + Piece.DIMPIECE / 4 + 1, baseY + 1);
		polygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4 - 1, baseY + 1);

		polygon.addPoint(baseX + Piece.DIMPIECE - 1, baseY + Piece.DIMPIECE / 2);
		polygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4 - 1, baseY + Piece.DIMPIECE - 1);
		polygon.addPoint(baseX + Piece.DIMPIECE / 4 + 1, baseY + Piece.DIMPIECE - 1);
		polygon.addPoint(baseX + 1, baseY + Piece.DIMPIECE / 2);

		// build selected polygon
		selPolygon.addPoint(baseX + Piece.DIMPIECE / 4, baseY - 1);
		selPolygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4, baseY - 1);

		selPolygon.addPoint(baseX + Piece.DIMPIECE, baseY + Piece.DIMPIECE / 2);
		selPolygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4, baseY + Piece.DIMPIECE);
		selPolygon.addPoint(baseX + Piece.DIMPIECE / 4, baseY + Piece.DIMPIECE);
		selPolygon.addPoint(baseX, baseY + Piece.DIMPIECE / 2);

	}

	/**
	 * get the tail piece - the others are not accessible
	 */
	public Piece getPiece() {
		if (pieces.size() == 0)
			return null;

		return pieces.get(pieces.size() - 1);
	}

	/**
	 * Add piece to tail
	 */
	public void addPiece(Piece p) {
		pieces.add(p);
	}

	/**
	 * remove piece P if it is on tail
	 */
	public boolean remPiece(Piece p) {
		if (pieces.indexOf(p) == pieces.size() - 1) {
			pieces.remove(p);
			return true;
		}
		return false;
	}

	/**
	 * clear all the pieces on this boardPlace
	 */
	public void clear() {
		for (int i = 0; i < pieces.size(); i++) {
			pieces.remove(i);
		}
	}

	/**
	 * to be viewed in debug watch
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	/**
	 * Paint this boardPiece - if it doesn't have any piece we should the draw
	 * polygon in background color
	 */
	public void paintComponent(Graphics g) {

		if (getPiece() == null) {
			// draw empty board place
			g.setColor(PIECEBACKGROUNDCOLOR);
			g.fillPolygon(polygon);
		} else {
			String str = getPiece().toString().substring(0, 1);
			g.setColor(Game.getColorFromPlayer(getPiece().isFromPlayerA()));
			g.fillPolygon(polygon);
			g.setColor(getPiece().getColor());
			g.drawString(str, this.baseX + 8, this.baseY + 18);

		}

		// if selected, draw selection
//		if (isSelected()) {
//			g.setColor(PIECESELECTIONCOLOR);
//			g.fillPolygon(polygon);
//		}
	}

	/**
	 * check if x,y received is inside the polygon of this boardPlace - uses the
	 * contains method from polygon
	 */
	public boolean isInsideBoardPlace(int x, int y) {
		return polygon.contains(x, y);
	}

	// metodos e vars nao usados

	// private static Color PIECESELECTIONCOLOR = Color.RED;
	// private boolean selected = false;

	/**
	 * set selected state
	 * 
	 * public void setSelected(boolean selected) { 
	 * 		this.selected = selected;
	 * }
	 * 
	 * 
	 * get selected state
	 * 
	 * public boolean isSelected() { 
	 * 		return this.selected; 
	 * }
	 * 
	 * equals, two BoardPlaces are equal if they have the same x and y
	 * 
	 * public boolean equals(Object o) { 
	 * 		if (o instanceof BoardPlace) { 
	 * 			if (this.x == ((BoardPlace) o).x && this.y == ((BoardPlace) o).y) { 
	 * 				return true; 
	 * 			} 
	 * 		}
	 * 		return false; 
	 * }
	 * 
	 * Migrate the state of this board place 1 position to the neighbor in the
	 * received direction. To be used is move HIVE up, down, NO, ....
	 * 
	 * public void migrateTo(Direction d) { 
	 * 		if (this.isSelected() == true && board.getNeighbourPoint(this.x, this.y, d) != null) {
	 * 			(board.getBoardPlace(board.getNeighbourPoint(this.x, this.y, d).x, board.getNeighbourPoint(this.x, this.y, d).y)).setSelected(true);
	 * 			this.setSelected(false); 
	 * 		}
	 * }
	 * 
	 */

}
