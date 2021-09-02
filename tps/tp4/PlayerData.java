package tps.tp4;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pieces.Ant;
import pieces.Beetle;
import pieces.Grasshopper;
import pieces.Piece;
import pieces.QueenBee;
import pieces.Spider;

/**
 * class that keep and control the data from one player
 * 
 */
public class PlayerData {

	private static Color ACTIVEPLAYERCOLOR = Color.orange;
	private static Color INACTIVEPLAYERCOLOR = Color.gray;

	public JPanel sidePanel = new JPanel();

	public JButton Queen;
	public JButton Grasshopper1;
	public JButton Grasshopper2;
	public JButton Beetle1;
	public JButton Beetle2;
	public JButton Spider1;
	public JButton Spider2;
	public JButton Spider3;
	public JButton Ant1;
	public JButton Ant2;
	public JButton Ant3;

	private JLabel movesLabel;
	private JLabel playerLabel = new JLabel();;
	private JLabel playerColorLabel = new JLabel("Player Color");
	public HiveLabel queenBeeLabel = null;
	public QueenBee queenBee = null;

	private int numberOfPiecesOnBoard = 0;
	private int numberOfMoves = 0;
	public int movesPerTurn = 0;

	private boolean playerWon = false;

	

	/**
	 * Constructor - should build the side panel for the player
	 */
	public PlayerData(Game game, boolean playerIsActive) {
		sidePanel.setBackground(new Color(0xC9F76F));

		//dependendo do jogador cria o painel no local e com a cor correta
		if (playerIsActive) {
			sidePanel.setBounds(50, 160, 100, 430);
			playerLabel.setText("Player A");
			playerColorLabel.setBackground(Color.BLACK);
		} else {
			sidePanel.setBounds(830, 160, 100, 430);
			playerLabel.setText("Player B");
			playerColorLabel.setBackground(Color.LIGHT_GRAY);
		}

		//inicializa labels e buttons
		playerColorLabel.setOpaque(true);
		playerColorLabel.setPreferredSize(new Dimension(100, 25));
		playerColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerLabel.setOpaque(true);
		playerLabel.setPreferredSize(new Dimension(100, 25));
		playerLabel.setHorizontalAlignment(SwingConstants.CENTER);

		movesLabel = new JLabel();
		movesLabel.setPreferredSize(new Dimension(100, 25));
		movesLabel.setBackground(Color.green);
		movesLabel.setOpaque(true);
		displayNumberOfMoves();

		Queen = new JButton("Queen Bee");
		Queen.setPreferredSize(new Dimension(100, 25));
		Queen.setBackground(Color.YELLOW);
		Queen.setOpaque(true);

		Beetle1 = new JButton("Beetle");
		Beetle1.setPreferredSize(new Dimension(100, 25));
		Beetle1.setBackground(Color.magenta);
		Beetle1.setOpaque(true);

		Beetle2 = new JButton("Beetle");
		Beetle2.setPreferredSize(new Dimension(100, 25));
		Beetle2.setBackground(Color.magenta);
		Beetle2.setOpaque(true);

		Grasshopper1 = new JButton("Grasshopper");
		Grasshopper1.setPreferredSize(new Dimension(120, 25));
		Grasshopper1.setBackground(new Color(70, 90, 40));
		Grasshopper1.setOpaque(true);

		Grasshopper2 = new JButton("Grasshopper");
		Grasshopper2.setPreferredSize(new Dimension(120, 25));
		Grasshopper2.setBackground(new Color(70, 90, 40));
		Grasshopper2.setOpaque(true);

		Spider1 = new JButton("Spider");
		Spider1.setPreferredSize(new Dimension(100, 25));
		Spider1.setBackground(new Color(0xA62D00));
		Spider1.setOpaque(true);

		Spider2 = new JButton("Spider");
		Spider2.setPreferredSize(new Dimension(100, 25));
		Spider2.setBackground(new Color(0xA62D00));
		Spider2.setOpaque(true);

		Spider3 = new JButton("Spider");
		Spider3.setPreferredSize(new Dimension(100, 25));
		Spider3.setBackground(new Color(0xA62D00));
		Spider3.setOpaque(true);

		Ant1 = new JButton("Ant");
		Ant1.setPreferredSize(new Dimension(100, 25));
		Ant1.setBackground(Color.BLUE);
		Ant1.setOpaque(true);

		Ant2 = new JButton("Ant");
		Ant2.setPreferredSize(new Dimension(100, 25));
		Ant2.setBackground(Color.BLUE);
		Ant2.setOpaque(true);

		Ant3 = new JButton("Ant");
		Ant3.setPreferredSize(new Dimension(100, 25));
		Ant3.setBackground(Color.BLUE);
		Ant3.setOpaque(true);

		sidePanel.add(playerLabel);
		sidePanel.add(playerColorLabel);
		sidePanel.add(Queen);
		sidePanel.add(Beetle1);
		sidePanel.add(Beetle2);
		sidePanel.add(Grasshopper1);
		sidePanel.add(Grasshopper2);
		sidePanel.add(Spider1);
		sidePanel.add(Spider2);
		sidePanel.add(Spider3);
		sidePanel.add(Ant1);
		sidePanel.add(Ant2);
		sidePanel.add(Ant3);
		sidePanel.add(movesLabel);
	}

	/**
	 * get side panel
	 */
	JPanel getSidePanel() {
		return sidePanel;
	}

	/**
	 * get number of moves of this player
	 */
	int getNumberOfMoves() {
		return numberOfMoves;
	}

	/**
	 * increment number of moves of this player
	 */
	void incNumberOfMoves() {
		this.numberOfMoves = this.numberOfMoves + 1;
	}

	/**
	 * get Queen Bee reference of this player
	 */

	QueenBee getQueenBee() {
		return queenBee;
	}

	/**
	 * sets the number of moves ...
	 */
	void setNumberOfMoves(int n) {
		numberOfMoves = n;
	}

	/**
	 * get the number of pieces on board ...
	 */
	int getNumberOfPiecesOnBoard() {
		return numberOfPiecesOnBoard;
	}

	/**
	 * set the number of pieces on board ...
	 */
	void setNumberOfPiecesOnBoard(int np) {
		numberOfPiecesOnBoard = np;
	}

	/**
	 * increases the number of pieces on board ...
	 */
	void incNumberOfPiecesOnBoard() {
		numberOfPiecesOnBoard++;
	}

	/**
	 * decreases the number of pieces on board ..
	 */
	void decNumberOfPiecesOnBoard() {
		numberOfPiecesOnBoard--;
	}

	/**
	 * set this player background as current player or not
	 */
	public void setPlayerPanelActive(boolean active) {
		if (active) {
			//altera a cor do painel dependendo de ser ativo ou nao
			playerLabel.setBackground(ACTIVEPLAYERCOLOR);
		} else {
			playerLabel.setBackground(INACTIVEPLAYERCOLOR);
		}
		//coloca enabled ou disabled
		Component[] components = this.getSidePanel().getComponents();
		for (Component component : components) {
			component.setEnabled(active);
			if (component.getBackground() == Color.gray)
				component.setEnabled(false);
		}

	}

	/**
	 * check if queen bee of this player is already on board
	 */
	// CHECKS IF BUTTON WAS USED
	public boolean isQueenBeeAlreadyOnBoard() {
		return !Queen.isEnabled();
	}

	/**
	 * display the current number of moves in the last label
	 */
	public void displayNumberOfMoves() {
		movesLabel.setText(String.valueOf(numberOfMoves));
		movesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	/**
	 * get the reference for the queen bee of this player
	 */
	public HiveLabel getQueenBeeLabel() {
		return queenBeeLabel;
	}

	/**
	 * sets if player won
	 */
	void setPlayerWon(boolean won) {
		playerWon = won;
	}

	/**
	 * return true if player won
	 */
	boolean playerWon() {
		return playerWon;
	}

}

/**
 * classe que suporta as labels das peças iniciais de cada jogador
 */
class HiveLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Piece p;
	@SuppressWarnings("unused")
	private Game game;

	public HiveLabel(Piece p, Game game) {
		this.p = p;
		this.game = game;
	}

	public Piece getPiece() {
		return p;
	}

	public String toString() {
		return p.toString();
	}

}

/**
 * enum with the several pieces and create methods
 */
enum PType {
	QUEENBEE {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new QueenBee(game, isFromPlayerA);
		};
	},
	BEETLE {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Beetle(game, isFromPlayerA);
		};
	},
	GRASHOPPER {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Grasshopper(game, isFromPlayerA);
		};
	},
	SPIDER {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Spider(game, isFromPlayerA);
		};
	},
	ANT {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Ant(game, isFromPlayerA);
		};
	};

	abstract Piece createNew(Game game, boolean isFromPlayerA);
}

//MÉTODOS E CLASSES NÃO USADOS

//private static final long serialVersionUID = 1L;
//final static Border unselBorder = BorderFactory.createLineBorder(Color.darkGray);
//final static Border selBorder = BorderFactory.createLineBorder(Color.white, 3);
//private boolean isDeactivated = false;

/**
 * auxiliary class
 * 
 * private class PiecesAndItsNumber { PType tipo; int nPecas;
 * 
 * public PiecesAndItsNumber(PType tipo, int nPecas) { this.tipo = tipo;
 * this.nPecas = nPecas; }
 * 
 * }
 * 
 * Initializes the counters and the labels
 * 
 * public void init(boolean playerIsActive) {
 * 
 * }
 * 
 *
 * HIVELABEL CLASS 
 * 
 * public void init() { 
 * }
 * 
 * public void activate() { 
 * 		setBorder(selBorder); 
 * }
 * 
 * public void setToNormal() { 
 * }
 * 
 * public void deactivate() { 
 * 		setBorder(unselBorder); 
 * } 
 * public boolean isDeactivated() { 
 * 		return isDeactivated; 
 * }
 *
 *
 * one Queen, two Beetles, two Grasshoppers, three Spiders and three Ants
 * 
 * Don't change this 
 * 
 * private final PiecesAndItsNumber[] ListaDePecas = new PiecesAndItsNumber[] { 
 * 		new PiecesAndItsNumber(PType.QUEENBEE, 1), 
 * 		new PiecesAndItsNumber(PType.BEETLE, 2), 
 * 		new PiecesAndItsNumber(PType.GRASHOPPER, 2), 
 * 		new PiecesAndItsNumber(PType.SPIDER, 3), 
 * 		new PiecesAndItsNumber(PType.ANT, 3) };
 */
