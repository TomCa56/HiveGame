package tps.tp4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JPanel;

import pieces.Piece;
import tps.tp4.Game.Direction;

public class Board extends JPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// background color for the board
	static Color BOARDBACKGROUNDCOLOR = new Color(0xC9F76F);

	// number of cells in the board
	public static final int DIMX = 31;
	public static final int DIMY = 15;

	// the board data - array 2D of BoardPlaces
	private BoardPlace[][] board = new BoardPlace[DIMX][DIMY];

	// game reference
	private Game game;

	//verifica se é clique de seleção ou colocação de peça
	public boolean firstClick = true;
	//guarda x e y do BP de onde se MOVE
	public int oldX = 0;
	public int oldY = 0;

	// methods ===============================================

	/**
	 * constructor
	 * 
	 */
	public Board(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(600, 400));
		initBoard();
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int count = 0;//garante só um clique de seleção/colocação
				// para ver as pecas por baixo do beetle - RMB
				if (e.getButton() == MouseEvent.BUTTON3) {
					String status = "";
					//func do clickOnBoard para verificar BP
					for (int y = 0; y < DIMY; y++) {
						for (int x = 0; x < DIMX; x++) {
							if (board[x][y].isInsideBoardPlace(e.getX(), e.getY()) == true
									&& getBoardPlace(x, y) != null) {
								// se o bp nao estiver vazio - procura todas pecas no array
								for (int i = 0; i < getBoardPlace(x, y).pieces.size(); i++) {

									String plr = getBoardPlace(x, y).pieces.get(i).isFromPlayerA() ? " from Player A; "
											: " from Player B; ";
									int nr = i + 1;
									status += "#" + nr + ": " + getBoardPlace(x, y).pieces.get(i) + plr;
								}

								game.setStatusInfo(status);
							}
						}
					}
				} else {
					// mover ou colocar
					if (game.boardMove) {// se possival mover
						if (firstClick) {// escolher
							clickOnBoard(e.getX(), e.getY(), null);
							firstClick = false;//prepara colocação
							
						} else {// colocar
							while (count < 1) {
								clickOnBoard(e.getX(), e.getY(), game.currentHiveLabel);
								game.setStatusInfo(game.currentHiveLabel.getPiece().toString() + " moved");
								count++;
							}
						}
						firstClick = true;//prepara seleção
						count = 0;//reset ao clique
					}

					game.boardMove = true;

					// disable JButtons dps de colocar a peca
					//caso seja colocação de uma peça nova 
					if (game.move == false) {
						while (count < 1) {

							clickOnBoard(e.getX(), e.getY(), game.currentHiveLabel);
							game.lastButton.setEnabled(false);
							game.lastButton.setBackground(Color.gray);

							count++;

						}

					}

				}
			}

		});
	}

	/**
	 * Create the board places for pieces
	 */
	private void initBoard() {
		setBackground(BOARDBACKGROUNDCOLOR);

		for (int y = 0; y < DIMY; y++) {
			for (int x = 0; x < DIMX; x++) {
				board[x][y] = new BoardPlace(this, x, y);
			}
		}

	}

	/**
	 * method called by the mouseListener of the board. Should check if the x, y
	 * received is inside any of the polygons. In affirmative case should call
	 * game.clickOnBoard with the (x, y) of the BoardPlace clicked
	 */
	public void clickOnBoard(int xPix, int yPix, HiveLabel h) {
		for (int y = 0; y < DIMY; y++) {
			for (int x = 0; x < DIMX; x++) {
				if (board[x][y].isInsideBoardPlace(xPix, yPix) == true) {
					//caso o BP seja valido chama cliqueOnBoard do game
					game.clickOnBoard(x, y);
					//guarda xy para operações futuras
					oldX = x;
					oldY = y;

				}
			}
		}

	}

	/**
	 * clears the board data - clear all the pieces on board
	 */
	public void resetBoard() {
		board = new BoardPlace[DIMX][DIMY];
	}

	/**
	 * draw the board - call the paintComponent for the super and for each one of
	 * the BoardPlaces
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y = 0; y < DIMY; y++) {
			for (int x = 0; x < DIMX; x++) {
				board[x][y].paintComponent(g);
			}
		}
	}

	/**
	 * get the neighbor point starting from x,y and going in the d direction. If the
	 * point doesn't exist the method returns null.
	 */
	public static Point getNeighbourPoint(int x, int y, Direction d) {
		Point p = new Point(x, y);

		// IMPORTANT NOTE: as the move depends on X, we must work on Y first
		switch (d) {
		case N:
			if (y == 0)
				return null;
			p.y--;
			break;
		case NE:
			// y first
			if (p.x % 2 == 0) {
				if (y == 0)
					return null;
				p.y--;
			}
			// then x
			if (x == Board.DIMX - 1)
				return null;
			p.x++;
			break;
		case SE:
			if (p.x % 2 == 1) {
				if (y == Board.DIMY - 1)
					return null;
				p.y++;
			}
			if (x == Board.DIMX - 1)
				return null;
			p.x++;
			break;
		case S:
			if (y == Board.DIMY - 1)
				return null;
			p.y++;
			break;
		case SO:
			if (p.x % 2 == 1) {
				if (y == Board.DIMY - 1)
					return null;
				p.y++;
			}
			if (x == 0)
				return null;
			p.x--;
			break;
		case NO:
			if (p.x % 2 == 0) {
				if (y == 0)
					return null;
				p.y--;
			}
			if (x == 0)
				return null;
			p.x--;
			break;
		}
		return p;
	}

	/**
	 * returns the (tail) piece on board[x][y]
	 */
	public Piece getPiece(int x, int y) {

		return getBoardPlace(x, y).getPiece();

	}

	/**
	 * returns the BoardPlace at board[x][y]
	 */
	public BoardPlace getBoardPlace(int x, int y) {

		return board[x][y];
	}

	/**
	 * add a piece (on tail) on the BoardPlace x,y. Should increase the
	 * numberOfPiecesOnBoard of the player that own the piece. Any change to the
	 * board should call the repaint() method. Every piece on board should keep its
	 * BoardPlace coordinates on board.
	 */
	public void addPiece(Piece p, int x, int y) {
		getBoardPlace(x, y).addPiece(p);
		p.setXY(x, y);

		game.repaint();

	}

	/**
	 * Removes the piece if this piece is on tail on its BoardPlace. Should adjust
	 * numberOfPiecesOnBoard from its owner
	 */
	@SuppressWarnings("unused")
	public boolean remPiece(Piece p) {
		for (int i = 0; i < DIMX; i++) {
			for (int j = 0; j < DIMY; j++) {
				//retira a peça no ultimo idx
				if (board[i][j].pieces.indexOf(p) == board[i][j].pieces.size() - 1)
					board[i][j].pieces.remove(p);
				return true;
			}
		}
		return false;
	}

	/**
	 * check if staring from x,y is just one hive. The number of adjacent pieces
	 * should be all the pieces on board. Can be used an ArrayList to collect the
	 * pieces.
	 */

	public boolean justOneHive(int x, int y) {

		if (getBoardPlace(x, y) != null) {
			boolean NreturnBoolean = false;
			int X_N = getNeighbourPoint(x, y, Direction.N).x;
			int Y_N = getNeighbourPoint(x, y, Direction.N).y;
			Piece placeN = getBoardPlace(X_N, Y_N).getPiece();
			if (placeN != null) {
				NreturnBoolean = true;
			}

			boolean NOreturnBoolean = false;
			int X_NO = getNeighbourPoint(x, y, Direction.NO).x;
			int Y_NO = getNeighbourPoint(x, y, Direction.NO).y;
			Piece placeNO = getBoardPlace(X_NO, Y_NO).getPiece();
			if (placeNO != null) {
				NOreturnBoolean = true;
			}

			boolean NEreturnBoolean = false;
			int X_NE = getNeighbourPoint(x, y, Direction.NE).x;
			int Y_NE = getNeighbourPoint(x, y, Direction.NE).y;
			Piece placeNE = getBoardPlace(X_NE, Y_NE).getPiece();
			if (placeNE != null) {
				NEreturnBoolean = true;
			}

			boolean SreturnBoolean = false;
			int X_S = getNeighbourPoint(x, y, Direction.S).x;
			int Y_S = getNeighbourPoint(x, y, Direction.S).y;
			Piece placeS = getBoardPlace(X_S, Y_S).getPiece();
			if (placeS != null) {
				SreturnBoolean = true;
			}

			boolean SOreturnBoolean = false;
			int X_SO = getNeighbourPoint(x, y, Direction.SO).x;
			int Y_SO = getNeighbourPoint(x, y, Direction.SO).y;
			Piece placeSO = getBoardPlace(X_SO, Y_SO).getPiece();
			if (placeSO != null) {
				SOreturnBoolean = true;
			}

			boolean SEreturnBoolean = false;
			int X_SE = getNeighbourPoint(x, y, Direction.SE).x;
			int Y_SE = getNeighbourPoint(x, y, Direction.SE).y;
			Piece placeSE = getBoardPlace(X_SE, Y_SE).getPiece();
			if (placeSE != null) {
				SEreturnBoolean = true;
			}
			
			//valida todos os vizinhos - se forem todos NULL está sozinho
			return NreturnBoolean || NEreturnBoolean || NOreturnBoolean || SreturnBoolean || SEreturnBoolean
					|| SOreturnBoolean;

		}

		return false;
	}

	/**
	 * Get all the pieces that are connected with the x, y received, and put them on
	 * the List received.
	 */
	int getPiecesFromThisPoint(int x, int y, List<Piece> pieces) {
		int X_N = getNeighbourPoint(x, y, Direction.N).x;
		int Y_N = getNeighbourPoint(x, y, Direction.N).y;
		BoardPlace north = getBoardPlace(X_N, Y_N);
		int X_NO = getNeighbourPoint(x, y, Direction.NO).x;
		int Y_NO = getNeighbourPoint(x, y, Direction.NO).y;
		BoardPlace northwest = getBoardPlace(X_NO, Y_NO);
		int X_NE = getNeighbourPoint(x, y, Direction.NE).x;
		int Y_NE = getNeighbourPoint(x, y, Direction.NE).y;
		BoardPlace northeast = getBoardPlace(X_NE, Y_NE);
		int X_S = getNeighbourPoint(x, y, Direction.S).x;
		int Y_S = getNeighbourPoint(x, y, Direction.S).y;
		BoardPlace south = getBoardPlace(X_S, Y_S);
		int X_SO = getNeighbourPoint(x, y, Direction.SO).x;
		int Y_SO = getNeighbourPoint(x, y, Direction.SO).y;
		BoardPlace southwest = getBoardPlace(X_SO, Y_SO);
		int X_SE = getNeighbourPoint(x, y, Direction.SE).x;
		int Y_SE = getNeighbourPoint(x, y, Direction.SE).y;
		BoardPlace southeast = getBoardPlace(X_SE, Y_SE);

		//verifica todos os neighbours se não forem null nem existirem na lista bde pcs
		//é colocado na lista 
		//para cada nova peça é chamado o método 
		boolean imEqual = false;
		if (game.move == false) {
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == getBoardPlace(x, y).getPiece()) {
					imEqual = true;
					break;
				}
			}
			if (imEqual == false) {
				pieces.add(getBoardPlace(x, y).getPiece());
			}
		}

		if (north.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == north.getPiece()) {
					isEqual = true;
					break;
				}
			}

			if (isEqual == false) {
				pieces.add(north.getPiece());
				getPiecesFromThisPoint(X_N, Y_N, pieces);
			}

		}
		if (northeast.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == northeast.getPiece()) {
					isEqual = true;
					break;
				}
			}

			if (isEqual == false) {
				pieces.add(northeast.getPiece());
				getPiecesFromThisPoint(X_NE, Y_NE, pieces);
			}

		}
		if (southeast.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == southeast.getPiece()) {
					isEqual = true;
					break;
				}
			}
			if (isEqual == false) {
				pieces.add(southeast.getPiece());
				getPiecesFromThisPoint(X_SE, Y_SE, pieces);
			}

		}
		if (south.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == south.getPiece()) {
					isEqual = true;
					break;
				}
			}

			if (isEqual == false) {
				pieces.add(south.getPiece());
				getPiecesFromThisPoint(X_S, Y_S, pieces);
			}

		}
		if (southwest.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == southwest.getPiece()) {
					isEqual = true;
					break;
				}
			}

			if (isEqual == false) {
				pieces.add(southwest.getPiece());
				getPiecesFromThisPoint(X_SO, Y_SO, pieces);
			}

		}
		if (northwest.getPiece() != null) {
			boolean isEqual = false;
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i) == northwest.getPiece()) {
					isEqual = true;
					break;
				}
			}

			if (isEqual == false) {
				pieces.add(northwest.getPiece());
				getPiecesFromThisPoint(X_NO, Y_NO, pieces);
			}
		}
		
		//retorna o nr de peças atual 
		return pieces.size();

	}

	//Métodos não usados
	
	// private static final long serialVersionUID = 1L;
	// private PlayerData playerAData, playerBData, currentPlayerData;
	// private Font piecesFont;

	/**
	 * sets one boardPlace selected state
	 * 
	 * public void setSelXY(int x, int y, boolean selectedState) {
	 * 		board[x][y].setSelected(selectedState); 
	 * }
	 * 
	 */

}
