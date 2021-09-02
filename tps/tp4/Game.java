package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import layouts.CenterLayout;
import pieces.Ant;
import pieces.Beetle;
import pieces.Grasshopper;
import pieces.Piece;
import pieces.QueenBee;
import pieces.Spider;

import java.io.BufferedReader;
import java.io.File;

/**
 * HIVE GAME: one Queen, two Beetles, two Grasshoppers, three Spiders and three
 * Ants
 * 
 * http://en.wikipedia.org/wiki/Hive_(game)
 * http://www.gen42.com/downloads/rules/Hive_Rules.pdf
 */

/**
 * the main class - that supports the game
 */
public class Game extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// enumerate that supports directions
	public enum Direction {
		N, NE, SE, S, SO, NO
	}
	
	//Variáveis gerais do Game
	private static final Color COLORPLAYER_A = Color.black;
	private static final Color COLORPLAYER_B = Color.lightGray;
	private static Game g;// usado no main, giveUp e startAgain
	private boolean endOfGame = false;//colocado a true quando qualquer jogador (ou os dois) ganham
	private Board board;
	private JPanel controlPanelOut;// painel inativo
	private JPanel controlPanel;// painel ativo
	private JLabel mainLabel;// HIVE GAME: current player ->
	private static String plr;// nome do jogador a mostrar na main label
	private JLabel lb_message = new JLabel();// log

	// buttons to move the Hive, if possible
	private JButton bn_moveUp;
	private JButton bn_moveDown;
	private JButton bn_moveNO;
	private JButton bn_moveNE;
	private JButton bn_moveSE;
	private JButton bn_moveSO;
	private JButton bn_changePlayer;
	private JButton bn_startAgain;
	private JButton bn_giveUp;

	// PlayerData
	public boolean isPlayerAToPlay;// jogador atual
	public PlayerData currentPlayerData;// data do jogador atual
	public PlayerData playerAData;// data plr A
	public PlayerData playerBData;// data plr B
	public boolean boardMove = true;// mover pecas - permite clique no board
	public boolean move = true;// distincao colocacao pecas e mov
	public Piece currentPiece = null;
	public HiveLabel currentHiveLabel = null;
	public JButton lastButton;// ultima peça selecionada para colocação

	// ScoreWindow
	private JFrame scoreWin;// score window
	private static JTextArea scoreArea = new JTextArea("SCORES");// area to add new scores
	private JTextField nameField = new JTextField("Insert Name", 10);
	private JButton submit = new JButton("Submit");
	private String name = "";// string q guarda username
	private int nr = 0;// int guarda score
	static Map<String, Integer> scoreList = new HashMap<String, Integer>();// set de user e score
	static int maxScoreLength = 11;// maximo de scores guardados
	
	//Recursos - música
	Clip mainSound;
	Clip gameSound;

	/**
	 * methods =============================================
	 */

	/**
	 * main
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// create object Game
				g = new Game();
				// launch the frame, but will be activated with some delay
				g.mainMenu();
			}
		});
		// System.out.println("End of main...");
	}

	// escolhe musica de INICO ou de JOGO
	void music(Clip sound) {
		// Sound de menu e de fim de jogo
		if (sound == mainSound) {
			try {
				mainSound = AudioSystem.getClip();
				mainSound.open(AudioSystem.getAudioInputStream(new File("src\\tps\\tp4\\main.mp3")));
				mainSound.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception exc) {
				exc.printStackTrace(System.out);
			}
			// Sound enquanto se está a jogar
		} else if (sound == gameSound) {
			try {
				gameSound = AudioSystem.getClip();
				gameSound.open(AudioSystem.getAudioInputStream(new File("src\\tps\\tp4\\game.mp3")));
				gameSound.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception exc) {
				exc.printStackTrace(System.out);
			}
		}
	}

	// som do cliques botoes e board
	void click() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("src\\tps\\tp4\\button.mp3")));
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	/**
	 * the JFrame initialization method
	 */
	private void init() {

		// ativar IMAGEM de background
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src\\tps\\tp4\\background_color.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ativar musica
		if (gameSound != null)
			gameSound.close();
		if (mainSound != null)
			mainSound.close();
		music(gameSound);

		endOfGame = false;

		// frame settings
		setSize(1000, 800);
		getContentPane().setBackground(Board.BOARDBACKGROUNDCOLOR);
		setLayout(new CenterLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// mainLabel
		plr = isPlayerAToPlay ? "PlayerA" : "PlayerB";
		mainLabel = new JLabel("Hive Game: current player -> " + plr, SwingConstants.CENTER);
		mainLabel.setBackground(Color.GRAY);
		mainLabel.setOpaque(true);
		mainLabel.setBounds(0, (int) (getHeight() * 0.05), (int) (getWidth()), (int) (getHeight() / 8));
		mainLabel.setFont(new Font(mainLabel.getFont().getName(), Font.TYPE1_FONT, 50));

		// action buttons
		bn_startAgain = new JButton("Start Again");
		bn_startAgain.setBounds((int) (getWidth() * 0.02), (int) (getHeight() * 0.765), (int) getWidth() / 10, 30);
		bn_moveUp = new JButton("move N");
		bn_moveUp.setBounds((int) (getWidth() * 0.02 * 2 + getWidth() / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_moveDown = new JButton("move S");
		bn_moveDown.setBounds((int) (getWidth() * 0.02 * 3 + getWidth() * 2 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_moveNO = new JButton("move NO");
		bn_moveNO.setBounds((int) (getWidth() * 0.02 * 4 + getWidth() * 3 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_moveNE = new JButton("move NE");
		bn_moveNE.setBounds((int) (getWidth() * 0.02 * 5 + getWidth() * 4 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_moveSE = new JButton("move SE");
		bn_moveSE.setBounds((int) (getWidth() * 0.02 * 6 + getWidth() * 5 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_moveSO = new JButton("move SO");
		bn_moveSO.setBounds((int) (getWidth() * 0.02 * 7 + getWidth() * 6 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_changePlayer = new JButton("Change Player");
		bn_changePlayer.setBounds((int) (getWidth() * 0.02 * 8 + getWidth() * 7 / 10), (int) (getHeight() * 0.765),
				(int) getWidth() / 10, 30);
		bn_giveUp = new JButton("Give Up");
		bn_giveUp.setBounds((int) (getWidth() / 2 - getWidth() / 10 / 2), (int) (getHeight() * 0.815),
				(int) getWidth() / 10, 30);

		// CONTROL BUTTONS ACTIONS
		// Move Up button
		bn_moveUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveHiveUp();

			}
		});
		// Move NE button
		bn_moveNE.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveNE();
			}
		});
		// Move NO button
		bn_moveNO.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveNO();
			}
		});
		// Move Down button
		bn_moveDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveDown();

			}
		});
		// Move SE button
		bn_moveSE.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveSE();
			}
		});
		// Move SO button
		bn_moveSO.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				moveSO();
			}
		});

		// NEW GAME
		bn_startAgain.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				click();
				startAgain();
			}
		});

		// CHANGE PLAYER ACTION
		bn_changePlayerAction();

		// give up
		bn_giveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click();
				//DEPENDENDO DO JOGADOR A DESISTIR - O OUTRO GANHA
				g.getPlayerData(isPlayerAToPlay).setPlayerWon(false);
				g.getPlayerData(!isPlayerAToPlay).setPlayerWon(true);
				doFinishGameActions();
			}
		});

		// END GAME USERNAME
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// NÃO ACEITA NOMES OU SCORES IGUAIS
				// 	name = nameField.getText();
				// 	nr = currentPlayerData.getNumberOfMoves();

				// ACEITA NOMES OU SCRS IGUAIS
				if (nameField.getText().equals("Insert Name")) {
					// do nothing
					setStatusInfo("Choose username");
				} else {
					name = checkName(nameField.getText());
					nr = checkScore(currentPlayerData.getNumberOfMoves() * 100);// para garantir aceitação de scores
																				// iguais - VER CHECKSCORE
					addScore(name, nr);
					nameField.setText("Insert Name");
					submit.setEnabled(false);
					nameField.setEnabled(false);
				}

			}
		});

		// log
		lb_message.setBounds(0, (int) (getHeight() * 0.85), getWidth(), 30);
		// msg de novo jogo na inicialização
		setStatusInfo("New Game started");

		// Averigua o player a jogar, ativa o seu painel e desativa o painel do outro
		// jogador - INICIALIZA TODA A PLAYER DATA NECESSÁRIA
		controlPanel = new JPanel();
		controlPanelOut = new JPanel();
		playerAData = new PlayerData(this, true);
		playerBData = new PlayerData(this, false);
		currentPlayerData = getPlayerData(isPlayerAToPlay);
		currentPlayerData.setPlayerPanelActive(true);
		getPlayerData(!isPlayerAToPlay).setPlayerPanelActive(false);
		controlPanel = currentPlayerData.getSidePanel();
		controlPanelOut = getPlayerData(!isPlayerAToPlay).getSidePanel();

		// ACTION LABELS - SET PIECE
		// Inicializar os botões e associar peças
		playerBData.Queen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				click();
				if (currentPlayerData.movesPerTurn == 0) {//garante que só é colocada uma peça por turno
					move = false;
					boardMove = false;
					lastButton = playerBData.Queen;
					currentPiece = new QueenBee(g, isPlayerAToPlay);
					playerBData.queenBee = (QueenBee) currentPiece;//para validar colocação até quarta jogada
					currentHiveLabel = new HiveLabel(currentPiece, g);
					setStatusInfo("New " + currentPiece.toString() + " selected");

				} else {
					setStatusInfo("One piece per turn");
				}

			}
		});

		playerAData.Queen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				click();
				if (currentPlayerData.movesPerTurn == 0) {
					move = false;
					boardMove = false;
					lastButton = playerAData.Queen;
					currentPiece = new QueenBee(g, isPlayerAToPlay);
					playerAData.queenBee = (QueenBee) currentPiece;
					currentHiveLabel = new HiveLabel(currentPiece, g);
					setStatusInfo("New " + currentPiece.toString() + " selected");

				} else {
					setStatusInfo("One piece per turn");
				}
			}
		});
		
		clickOnPieceLabelOnSidePanel(playerBData.Beetle1, new Beetle(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Beetle1, new Beetle(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Beetle2, new Beetle(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Beetle2, new Beetle(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Grasshopper1, new Grasshopper(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Grasshopper1, new Grasshopper(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Grasshopper2, new Grasshopper(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Grasshopper2, new Grasshopper(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Spider1, new Spider(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Spider1, new Spider(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Spider2, new Spider(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Spider2, new Spider(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Spider3, new Spider(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Spider3, new Spider(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Ant1, new Ant(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Ant1, new Ant(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Ant2, new Ant(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Ant2, new Ant(g, true));
		clickOnPieceLabelOnSidePanel(playerBData.Ant3, new Ant(g, false));
		clickOnPieceLabelOnSidePanel(playerAData.Ant3, new Ant(g, true));

		// adds
		add(mainLabel);
		add(lb_message);
		add(controlPanel);
		add(controlPanelOut);
		add(bn_startAgain);
		add(bn_giveUp);
		add(bn_moveNO);
		add(bn_moveSO);
		add(bn_moveSE);
		add(bn_moveDown);
		add(bn_moveNE);
		add(bn_moveUp);
		add(bn_changePlayer);

		buildMenu();

		// board
		board = new Board(this);
		add(board, BorderLayout.CENTER);

		setVisible(true);

	}

	/**
	 * the JFrame initialization method - corre primeiro
	 */
	private void mainMenu() {
		
		//imagem de background
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src\\tps\\tp4\\background.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//música
		if (mainSound != null)
			mainSound.close();
		if (gameSound != null)
			gameSound.close();
		music(mainSound);
		
		//frame settings
		setSize(1000, 800);
		setLayout(new CenterLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//main label
		JLabel main = new JLabel("Hive Game", SwingConstants.CENTER);
		main.setBounds(0, (int) (getHeight() * 0.05), (int) (getWidth()), (int) (getHeight() / 8));
		main.setFont(new Font(main.getFont().getName(), Font.TYPE1_FONT, 100));
		main.setForeground(Board.BOARDBACKGROUNDCOLOR);

		//botões de menu inicial
		JPanel buttons = new JPanel(new GridLayout(5, 1, 30, 15));
		JButton play = new JButton("Play");
		JButton rules = new JButton("Rules");
		JButton scores = new JButton("High Scores");
		JButton about = new JButton("About");
		JButton quit = new JButton("Quit");
		
		//pára e reinicia musica
		JButton mute = new JButton("Music: On/Off");
		mute.setBounds(this.getWidth() - 160, this.getHeight() - 115, 110, 30);
		mute.setVisible(true);
		mute.setBackground(Board.BOARDBACKGROUNDCOLOR);

		buttons.setOpaque(false);
		buttons.add(play);
		buttons.add(rules);
		buttons.add(scores);
		buttons.add(about);
		buttons.add(quit);

		// Dar as mesmas caracteristicas a todos os componentes do painel
		Component[] components = buttons.getComponents();
		for (Component component : components) {
			component.setPreferredSize(new Dimension(250, 75));
			component.setBackground(new Color(0x9CCF3A));
			((JComponent) component).setOpaque(true);
			component.setFont(new Font(play.getFont().getName(), Font.TYPE1_FONT, 30));
		}

		add(mute);
		add(main);
		add(buttons);

		setVisible(true);

		// Mute button
		mute.addActionListener(new ActionListener() {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (count % 2 == 0) {
					mainSound.close();
					count++;
				} else {
					music(mainSound);
					count++;
				}

			}
		});
		// Play button
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				main.setVisible(false);
				buttons.setVisible(false);

				g.init();

			}
		});
		// Rules button
		rules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				rules();

			}
		});
		// Scores button
		scores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				viewScores();

			}
		});
		// About button
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				about();

			}
		});
		// Quit button
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				click();
				System.exit(ABORT);

			}
		});

	}

	/**
	 * build menu
	 */
	private void buildMenu() {
		JMenuBar bar;
		JMenu menu;
		JMenuItem restart;
		JMenuItem main;
		JMenuItem scores;
		JMenuItem about;
		JMenuItem rules;
		JMenuItem mute;
		JMenuItem quit;

		bar = new JMenuBar();
		menu = new JMenu("Options...");
		bar.add(menu);
		setJMenuBar(bar);

		// NEW GAME
		restart = new JMenuItem("Restart Game");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAgain();
			}
		});
		menu.add(restart);

		// main menu
		main = new JMenuItem("Main Menu");
		main.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu();
			}
		});
		menu.add(main);

		// ABOUT GAME
		about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});
		menu.add(about);

		// JANELA SCORES
		scores = new JMenuItem("View Scores");
		scores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewScores();
			}
		});
		menu.add(scores);

		// REGRAS
		rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules();
			}
		});
		menu.add(rules);

		// mute
		mute = new JMenuItem("Music - On/Off");
		mute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (endOfGame) {
					if (mainSound.isActive())
						mainSound.close();
					else
						music(mainSound);
				}

				else if (!endOfGame) {
					if (gameSound.isActive())
						gameSound.close();
					else
						music(gameSound);
				}

			}

		});
		menu.add(mute);

		// SAIR
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(ABORT);
			}
		});
		menu.add(quit);

	}

	/**
	 * activate About window
	 */
	private void about() {
		int w = 500;
		int h = 400;
		// FRAME
		JFrame aboutWin = new JFrame("About");
		aboutWin.setSize(w, h);

		// ABOUT
		JTextArea txt = new JTextArea("HIVE GAME em Java Swing." + "\n" + "ISEL" + "\n" + "Modelação e Programação"
				+ "\n" + "Licenciatura em Eng Informática e de Multimédia" + "\n" + "Semestre Verão 2019/2020" + "\n"
				+ "Autores: " + "\n" + "Guilherme Rodrigues 41863" + "\n" + "Tomás Carvalho 42357" + "\n" + "LEIM 21N");
		txt.setBounds(0, 0, w, h / 2);
		txt.setBackground(Board.BOARDBACKGROUNDCOLOR);
		txt.setFont(new Font(scoreArea.getFont().getName(), Font.TYPE1_FONT, 12));

		// paineis E IMGS

		JPanel f2 = new JPanel();
		f2.setBackground(Board.BOARDBACKGROUNDCOLOR);
		URL url2 = getClass().getResource("42357.jpg");
		ImageIcon img2 = new ImageIcon(
				new ImageIcon(url2).getImage().getScaledInstance(w / 2, h / 2, Image.SCALE_DEFAULT));
		JLabel foto2 = new JLabel("", img2, JLabel.CENTER);
		f2.add(foto2);
		validate();

		// ADDS
		aboutWin.add(txt, BorderLayout.NORTH);
		aboutWin.add(f2, BorderLayout.WEST);
		aboutWin.setVisible(true);

	}

	/**
	 * activate View scores window
	 */
	private void viewScores() {
		//frame
		scoreWin = new JFrame("Scores");
		scoreWin.setSize(500, 400);
		scoreWin.setVisible(true);
		//area de texto onde são colocados scores
		scoreArea.setText("SCORES" + "\n" + sort());
		scoreArea.setBackground(Board.BOARDBACKGROUNDCOLOR);
		scoreArea.setFont(new Font(scoreArea.getFont().getName(), Font.TYPE1_FONT, 20));
		scoreWin.add(scoreArea);

	}

	// AUXILARY METHODS FOR SCORES
	//verifica se já existe algum reg com um nome igual - acrescenta * em caso positivo
	static String checkName(String name) {
		if (scoreList.get(name) != null) {
			return checkName(name + "*");
		}
		return name;
	}
	
	//verifica se existe algum score igual ao obtido - caso positivo subtrai para ficar atrás
	static int checkScore(int sc) {
		for (int score : scoreList.values()) {
			if (sc == score) {
				return checkScore(sc - 1);
			}

		}
		return sc;
	}

	// sort scores
	public static String sort() {
		//lê do ficheiro de registos os scores guardados
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("scores.txt"));
			String str;
			while ((str = br.readLine()) != null) {
				String[] entry = str.split(" : ", 2);
				if (entry.length >= 2) {
					String key = entry[0];
					String value = entry[1];
					//adiciona TUDO á lista de scores
					scoreList.put(key, Integer.valueOf(value));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//copia todos scores do mapa para ordenação 
		String scoreStr = "";
		SortedSet<Integer> scores = new TreeSet<Integer>(scoreList.values());
		for (int score : scores) {
			String u = "";
			//ordena de acordo com o nr
			for (String user : scoreList.keySet()) {
				if (score == scoreList.get(user))
					u = user;
			}
			
			scoreStr += u + " : " + score + "\n";

		}
		//guarda lista no ficheiro novamente
		try (PrintWriter out = new PrintWriter("scores.txt")) {
			out.println(scoreStr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return scoreStr;
	}

	// acrescenta nova entrada a lista
	public static void addScore(String name, int score) {

		int least = 0;
		//quando lista ultrapassa limite de entradas
		if (scoreList.size() >= maxScoreLength) {
			//vai buscar valor mais baixo 
			least = new TreeSet<Integer>(scoreList.values()).first();
			//se recebido for maior
			if (score >= least) {
				String last = "";
				//associa nome 
				for (String values : scoreList.keySet()) {
					if (least == scoreList.get(values))
						last = values;
				}
				//e retira 
				scoreList.remove(last, least);
				//coloca novo
				scoreList.put(name, score);

				sort();
			}

		} else {
			scoreList.put(name, score);
			sort();
		}
	}

	/**
	 * activate RULES window
	 */
	private void rules() {
		JFrame rules = new JFrame("Rules");
		rules.setSize(1200, 720);
		JTextArea txt = new JTextArea("-------REGRAS GERAIS-------" + "\n"
				+ "1: Quando se coloca uma peça no tabuleiro ela não pode ter vizinhos inimigos, à exceção da primeira peça do segundo jogador a jogar. \r\n"
				+ "2: A abelha rainha (QueenBee) tem de ser colocada obrigatoriamente até à 4ª jogada. Só se pode fazer movimentos de peças de um jogador "
				+ "\n" + "depois de ele ter colocador a sua abelha rainha.  \r\n"
				+ "3: O Hive deve ser sempre um. Os movimentos das peças também não deve gerar dois Hives, mesmo que momentaneamente, ou seja, os "
				+ "\n" + "lugares de origem e de destino de qualquer movimento de uma posição, " + "\n"
				+ "deve ser conectados  por outra(s) peça(s), que não aquela que se quer mover. Um movimento de várias posições (exceto no caso do gafanhoto) "
				+ "\n" + "é uma composição de vários movimentos de uma posição." + "\n"
				+ "Todas as peças (bichos) têm de respeitar esta regra. \r\n"
				+ "4: Regra de fim de jogo: o jogo termina quando uma abelha rainha (QueenBee), ou as duas (empate), estiver(em) totalmente rodeada(s) por (quaisquer) peças. \r\n"
				+ "5: Um jogador pode passar a sua vez, contando como um seu movimento. \r\n"
				+ "6: As peças Abelha Rainha (QueenBee), Aranha (Spider) e formiga (Ant) só se podem deslocar se o movimento for fisicamente realizável em cada passo."
				+ "\n"
				+ "Cada peça para poder colocada  no tabuleiro tem de poder deslizar fisicamente do rebordo para o lugar de destino. "
				+ "\n" + "\n" + "------REGRAS ESPECIFICAS DE PEÇAS------" + "\n"
				+ "QUEEN: A abelha rainha (QueenBee) só se pode deslocar uma posição. \r\n"
				+ "BEETLE: Os escaravelhos (Beetle) só se deslocam 1 posição mas podem subir para cima de outras peças colocadas, mesmo ficando uns por cima dos outros. "
				+ "\n" + "É a única peça que pode subir para cima de outras." + "\n"
				+ "A peça que estiver por cima é que pode ser movimentada e que conta como a peça daquela posição. O escravelho não pode ser colocado inicialmente em cima "
				+ "\n" + "de outra peça. \r\n"
				+ "GRASSHOPPER: Os gafanhotos (Grasshopper) têm de saltar, mas sempre em linha reta e por cima de pelo menos uma posição ocupada. Mas não podem saltar "
				+ "\n" + "por cima de posições não ocupadas.  \r\n"
				+ "SPIDER: As aranhas (Spider) devem deslocar-se sempre em movimentos de 3 posições diferentes . \r\n"
				+ "ANT: As formigas (Ant) podem deslocar-se um qualquer nº de posições." + "\n" + "\n"
				+ "------CARACTERISTICAS DA APLICAÇÃO------" + "\n"
				+ "- Para saber que peças estão por baixo do Beetle clicar sobre o mesmo com o botão direito do rato"
				+ "\n" + "- Botão de 'Music: On/Off' pára e recomeça a música" + "\n"
				+ "	- No ecrã inicial encontra-se no canto inferior direito" + "\n"
				+ "	- No ecrã de jogo e final de jogo na barra de opções" + "\n"
				+ "- A lista de scores guarda a pontuação como movimento * 100" + "\n"
				+ "	- Se for recebido um user igual a um existente é acrescentado um asterisco" + "\n"
				+ "	- Se for recebido um score já conseguido é subtraido 1");
		txt.setBounds(0, 0, rules.getWidth(), rules.getHeight());
		txt.setBackground(Board.BOARDBACKGROUNDCOLOR);
		txt.setFont(new Font(scoreArea.getFont().getName(), Font.TYPE1_FONT, 15));
		rules.add(txt);
		rules.setVisible(true);

	}

	/**
	 * get color from player
	 */
	static public Color getColorFromPlayer(boolean isPlayerA) {
		return isPlayerA ? COLORPLAYER_A : COLORPLAYER_B;
	}

	/**
	 * get board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * get player data
	 */
	public PlayerData getPlayerData(boolean fromPlayerA) {
		return fromPlayerA ? playerAData : playerBData;
	}

	/**
	 * change player actions - to be called from the menu or from the button
	 * changePlayer
	 */
	private void bn_changePlayerAction() {
		bn_changePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click();
				changePlayer();

			}
		});
	}

	/**
	 * change player actions
	 * 
	 */
	public void changePlayer() {
		
		currentPlayerData.incNumberOfMoves();
		currentPlayerData.displayNumberOfMoves();
		currentPlayerData.setPlayerPanelActive(false);//coloca o painel atual a falso 
		currentPlayerData = getPlayerData(!isPlayerAToPlay);//associa o current player data ao outro jogador
		currentPlayerData.setPlayerPanelActive(true);//coloca o painel do novo jogador ATUAL A true
		isPlayerAToPlay = !isPlayerAToPlay;//altera valor do jogador atual
		plr = isPlayerAToPlay ? "PlayerA" : "PlayerB";
		mainLabel.setText("Hive Game: current player -> " + plr);
		playerAData.movesPerTurn = 0;
		playerBData.movesPerTurn = 0;
		setStatusInfo(plr + " a jogar");
		
		//reset
		currentPiece = null;
		currentHiveLabel = null;
		board.firstClick = true;
		move = true;// prepara para mover pecas

	}

	/**
	 * start again actions
	 * 
	 */
	private void startAgain() {
		//para qualque musica que esteja a passar
		if (mainSound != null)
			mainSound.close();
		if (gameSound != null)
			gameSound.close();
		//reset 
		board.resetBoard();
		//fecha janela atual 
		dispose();
		//inicializa nova
		init();

	}

	/**
	 * check if coordinate x,y only have friendly neighbor of current player
	 */
	private boolean onlyHaveFriendlyNeighbors(int x, int y, Piece h) {
		Piece check = board.getBoardPlace(x, y).getPiece();
		boolean owner;
		//para validar uma peça especifica
		if (h == null) {
			//se não for dado h é validada a peça do BP atual
			owner = check.isFromPlayerA();
		} else {
			owner = h.isFromPlayerA();
		}
		//para cada dir vai encontrar o neighbour
		boolean NreturnBoolean;
		int X_N = Board.getNeighbourPoint(x, y, Direction.N).x;
		int Y_N = Board.getNeighbourPoint(x, y, Direction.N).y;
		Piece placeN = board.getBoardPlace(X_N, Y_N).getPiece();
		if (placeN == null) {
			NreturnBoolean = true;
		} else if (placeN.isFromPlayerA() == owner) {
			NreturnBoolean = true;
		} else {
			//so no caso de ser uma peça do oponente é que devolve FALSE
			NreturnBoolean = false;
		}

		boolean NOreturnBoolean;
		int X_NO = Board.getNeighbourPoint(x, y, Direction.NO).x;
		int Y_NO = Board.getNeighbourPoint(x, y, Direction.NO).y;
		Piece placeNO = board.getBoardPlace(X_NO, Y_NO).getPiece();
		if (placeNO == null) {
			NOreturnBoolean = true;
		} else if (placeNO.isFromPlayerA() == owner) {
			NOreturnBoolean = true;
		} else {
			NOreturnBoolean = false;
		}

		boolean NEreturnBoolean;
		int X_NE = Board.getNeighbourPoint(x, y, Direction.NE).x;
		int Y_NE = Board.getNeighbourPoint(x, y, Direction.NE).y;
		Piece placeNE = board.getBoardPlace(X_NE, Y_NE).getPiece();
		if (placeNE == null) {
			NEreturnBoolean = true;
		} else if (placeNE.isFromPlayerA() == owner) {
			NEreturnBoolean = true;
		} else {
			NEreturnBoolean = false;
		}

		boolean SreturnBoolean;
		int X_S = Board.getNeighbourPoint(x, y, Direction.S).x;
		int Y_S = Board.getNeighbourPoint(x, y, Direction.S).y;
		Piece placeS = board.getBoardPlace(X_S, Y_S).getPiece();
		if (placeS == null) {
			SreturnBoolean = true;
		} else if (placeS.isFromPlayerA() == owner) {
			SreturnBoolean = true;
		} else {
			SreturnBoolean = false;
		}

		boolean SOreturnBoolean;
		int X_SO = Board.getNeighbourPoint(x, y, Direction.SO).x;
		int Y_SO = Board.getNeighbourPoint(x, y, Direction.SO).y;
		Piece placeSO = board.getBoardPlace(X_SO, Y_SO).getPiece();
		if (placeSO == null) {
			SOreturnBoolean = true;
		} else if (placeSO.isFromPlayerA() == owner) {
			SOreturnBoolean = true;
		} else {
			SOreturnBoolean = false;
		}

		boolean SEreturnBoolean;
		int X_SE = Board.getNeighbourPoint(x, y, Direction.SE).x;
		int Y_SE = Board.getNeighbourPoint(x, y, Direction.SE).y;
		Piece placeSE = board.getBoardPlace(X_SE, Y_SE).getPiece();
		if (placeSE == null) {
			SEreturnBoolean = true;
		} else if (placeSE.isFromPlayerA() == owner) {
			SEreturnBoolean = true;
		} else {
			SEreturnBoolean = false;
		}
		
		//DEVOLVE A INTERCEÇÃO DO BP DE CADA DIR - BASTA HAVER UMA PEÇA DO OPONENTE PARA NÃO PODER COLOCAR
		return NreturnBoolean && NEreturnBoolean && NOreturnBoolean && SreturnBoolean && SEreturnBoolean
				&& SOreturnBoolean;

	}

	// AUXILIARY METHODS FOR CLICKONBOARD
	// para quando jogador tenta selecionar peça do oponente ou coloca num destino impossivel é feito reset
	void reset() {
		board.firstClick = true;
		lastButton = null;
	}

	// GERE NUMERO DE PECAS ONBOARD QUANDO BEETLE ESTA EM CIMA DE OUTRA PECA
	void beetle_vs_piecesOnBoard(int x, int y) {
		if (move == true) {
			if (currentPiece instanceof Beetle) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					if (board.getBoardPlace(board.oldX, board.oldY).getPiece() == null) {
						currentPlayerData.decNumberOfPiecesOnBoard();
					}

				} else {

					if (board.getBoardPlace(board.oldX, board.oldY).getPiece() == null) {

					} else {
						currentPlayerData.incNumberOfPiecesOnBoard();
					}

				}

			}
		} else {
			currentPlayerData.incNumberOfPiecesOnBoard();
		}
	}

	// Força a colocação da rainha na quarta jogada
	void queenBytheFourth(int x, int y) {
		//atribui rainha ao valor da peça atual e do lastButton
		currentPiece = new QueenBee(g, isPlayerAToPlay);
		lastButton = currentPlayerData.Queen;
		lastButton.setEnabled(false);
		lastButton.setBackground(Color.gray);
		placePiece(x, y);
		setStatusInfo("Queen must be played by the fourth turn");
	}

	// coloca a peca
	void placePiece(int x, int y) {
		beetle_vs_piecesOnBoard(x, y);
		board.addPiece(currentPiece, x, y);
		setStatusInfo(currentPiece.toString() + " placed");
		currentPlayerData.movesPerTurn++;
		changePlayer();

	}

	// quando movida para um local impossivel coloca a peca no local original
	void goBack() {
		board.addPiece(currentHiveLabel.getPiece(), board.oldX, board.oldY);
		setStatusInfo("Cant' place here");
		currentPiece = null;
		currentHiveLabel = null;

	}

	// gere movimentos de cada tipo de peca
	void specificMove(int x, int y) {

		if (currentPiece != null) {
			if (currentPiece.moveTo(x, y) == false) {
				goBack();
			} else {
				placePiece(x, y);
			}
		}

	}

	/**
	 * a click on the board
	 * 
	 */
	public void clickOnBoard(int x, int y) {
		//distingue colocação e mov - segundo clique de colocação só é possivel se for um BP vazio ou se a peça for um Beetle
		if (board.getBoardPlace(x, y).getPiece() == null || currentPiece instanceof Beetle) {
			if (move == false) {// colocar peca

				if (board.justOneHive(x, y) == false
						&& playerAData.getNumberOfPiecesOnBoard() + playerBData.getNumberOfPiecesOnBoard() >= 1) {
					//desde que a primeira peça é colocada nenhuma outra pode ficar afastada da colmeia
					reset();
					setStatusInfo("cant break the hive");

				} else if (onlyHaveFriendlyNeighbors(x, y, currentPiece) == false
						&& playerAData.getNumberOfPiecesOnBoard() >= 1 && playerBData.getNumberOfPiecesOnBoard() >= 1) {
					// se o nr de pcs onBoard for mais que uma não se pode colocar ao do oponente
					
					reset();
					setStatusInfo("piece has to be placed by same player pieces only");

				} else if (currentPlayerData.getNumberOfMoves() >= 3
						&& currentPlayerData.isQueenBeeAlreadyOnBoard() == false) {
					// a rainha tem de ser jogada ate a quarta jogada
					queenBytheFourth(x, y);
				} else {
					//COLOCAÇÃO NORMAL
					placePiece(x, y);

				}

			} else {// mover peca
				
				if (board.justOneHive(x, y) == false
						&& playerAData.getNumberOfPiecesOnBoard() + playerBData.getNumberOfPiecesOnBoard() > 1) {
					//desde que a primeira peça é colocada nenhuma outra pode ficar afastada da colmeia
					goBack();
				}

				else if (board.getPiecesFromThisPoint(x, y,
						new ArrayList<Piece>()) < (playerAData.getNumberOfPiecesOnBoard()
								+ playerBData.getNumberOfPiecesOnBoard()) - 1) {
					// nao deixa colocar a peca de maneira a criar mais que uma hive
					goBack();
					setStatusInfo("Don't break the hive");
				}

				else {
					//COLOCAÇÃO NORMAL com ultima linha de verificação - movimento especifico da peça
					specificMove(x, y);

				}
			}

		}
		// escolher peca
		else if (board.getBoardPlace(x, y).getPiece().isFromPlayerA() == isPlayerAToPlay
				&& currentPlayerData.isQueenBeeAlreadyOnBoard()) {//só pode escolher se for a sua vez e se já tiver jogado a rainha
			// situacao normal
			currentPiece = board.getBoardPlace(x, y).getPiece();//atribui valor a currentPiece
			move = true;//coloca o move a true (para board Mouse Listener)
			board.getBoardPlace(x, y).remPiece(currentPiece);//retira a peça atual 
			board.repaint();
			setStatusInfo(currentPiece.toString() + " selected to be moved");
			currentHiveLabel = new HiveLabel(currentPiece, g);//cria hiveLabel com valor da peça atual
			//NO SEGUNDO CLICK VAI SER CHAMADA A PRIMEIRA PARTE DO METODO

		} else {
			// caso se escolha uma peca do outro jogador
			setStatusInfo("Piece is not from current player");
			//caso não se tenha colocado a Rainha
			if (!currentPlayerData.isQueenBeeAlreadyOnBoard())
				setStatusInfo("To move place Bee");
			// caso se tente colocar uma peca q nao o escaravelho em cima de outra peca - QUANDO SE CLICA SOBRE UMA PEÇA ESTA VAI SE PREPARAR PARA MOVER - SE HOUVER UMA PEÇA A SER MOVIDA VAI SER COLOCADA DE VOLTA 
			if (move == true)
				goBack();
			if (currentPlayerData.isQueenBeeAlreadyOnBoard() == false)// caso se mover sem colocar a rainha
				setStatusInfo("Cant move without queen");
			currentPiece = null;
			currentHiveLabel = null;
		}
		
		//valida sempre o final de jogo
		checkFinishGame();
		doFinishGameActions();
		click();
	}

	/**
	 * a click on a label on side panel
	 */
	// implementacao diferente - associa botao a tipo de peca
	public Piece clickOnPieceLabelOnSidePanel(JButton button, Piece piece) {
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				click();
				//se nenhuma peça tiver sido colocada ou movida
				if (currentPlayerData.movesPerTurn == 0) {
					//prepara para mov
					move = false;
					boardMove = false;
					//atribui val peça e botão
					lastButton = button;
					currentPiece = piece;
					currentHiveLabel = new HiveLabel(currentPiece, g);
					setStatusInfo("New " + currentPiece.toString() + " selected");
				} else {
					setStatusInfo("One piece per turn");
				}
			}
		});

		return currentPiece;

	}

	/**
	 * check if can move physically from x,y in to the direction received
	 * 
	 * By physical we mean, that the piece has physical space to move. A piece, with
	 * the NE and NO places occupied, cannot move to N.
	 * 
	 * 
	 */
	public boolean canPhysicallyMoveTo(int x, int y, Direction d) {
		
		BoardPlace BoardPlaceN = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.N).x,
				Board.getNeighbourPoint(x, y, Direction.N).y);
		BoardPlace BoardPlaceNO = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.NO).x,
				Board.getNeighbourPoint(x, y, Direction.NO).y);
		BoardPlace BoardPlaceNE = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.NE).x,
				Board.getNeighbourPoint(x, y, Direction.NE).y);
		BoardPlace BoardPlaceS = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.S).x,
				Board.getNeighbourPoint(x, y, Direction.S).y);
		BoardPlace BoardPlaceSO = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.SO).x,
				Board.getNeighbourPoint(x, y, Direction.SO).y);
		BoardPlace BoardPlaceSE = board.getBoardPlace(Board.getNeighbourPoint(x, y, Direction.SE).x,
				Board.getNeighbourPoint(x, y, Direction.SE).y);

		switch (d) {
		case N:
			if (BoardPlaceNO != null && BoardPlaceNE != null) {
				return false;
			}
		case NE:
			if (BoardPlaceN != null && BoardPlaceSE != null) {
				return false;
			}
		case NO:
			if (BoardPlaceN != null && BoardPlaceSO != null) {
				return false;
			}
		case S:
			if (BoardPlaceSE != null && BoardPlaceSO != null) {
				return false;
			}
		case SE:
			if (BoardPlaceS != null && BoardPlaceNE != null) {
				return false;
			}
		case SO:
			if (BoardPlaceS != null && BoardPlaceNO != null) {
				return false;
			}
		default:
			return true;
		}

		// return true;
	}

	/**
	 * set status info in label status
	 */
	public void setStatusInfo(String str) {
		lb_message.setText(str);
		lb_message.setHorizontalAlignment(SwingConstants.CENTER);
	}

	/**
	 * check if end of game - update playerAWon and/or playerBWon states RETORNA
	 * TRUE SE A OU B OU AMBOS (EMPATE) TIVEREM GANHO
	 */
	private boolean checkFinishGame() {
		boolean plrA = false;
		boolean plrB = false;
		if (checkFinishGame(true)) {
			playerBData.setPlayerWon(true);
			plrB = true;
		}

		if (checkFinishGame(false)) {
			playerAData.setPlayerWon(true);
			plrA = true;
		}
		return plrA || plrB;

	}

	/**
	 * check if queen of received player is surrounded
	 */
	private boolean checkFinishGame(boolean playerA) {
		for (int x = 0; x < Board.DIMX; x++) {
			for (int y = 0; y < Board.DIMY; y++) {
				for (Piece p : board.getBoardPlace(x, y).pieces) {//para o caso de arainha estar po baixo do Beetle
					
					//para todos bp verifica se está completamente rodeado
					if (p != null && p == getPlayerData(playerA).getQueenBee()) {
						boolean NreturnBoolean = false;
						int X_N = Board.getNeighbourPoint(x, y, Direction.N).x;
						int Y_N = Board.getNeighbourPoint(x, y, Direction.N).y;
						Piece placeN = board.getBoardPlace(X_N, Y_N).getPiece();
						if (placeN != null) {
							NreturnBoolean = true;
						}

						boolean NOreturnBoolean = false;
						int X_NO = Board.getNeighbourPoint(x, y, Direction.NO).x;
						int Y_NO = Board.getNeighbourPoint(x, y, Direction.NO).y;
						Piece placeNO = board.getBoardPlace(X_NO, Y_NO).getPiece();
						if (placeNO != null) {
							NOreturnBoolean = true;
						}

						boolean NEreturnBoolean = false;
						int X_NE = Board.getNeighbourPoint(x, y, Direction.NE).x;
						int Y_NE = Board.getNeighbourPoint(x, y, Direction.NE).y;
						Piece placeNE = board.getBoardPlace(X_NE, Y_NE).getPiece();
						if (placeNE != null) {
							NEreturnBoolean = true;
						}

						boolean SreturnBoolean = false;
						int X_S = Board.getNeighbourPoint(x, y, Direction.S).x;
						int Y_S = Board.getNeighbourPoint(x, y, Direction.S).y;
						Piece placeS = board.getBoardPlace(X_S, Y_S).getPiece();
						if (placeS != null) {
							SreturnBoolean = true;
						}

						boolean SOreturnBoolean = false;
						int X_SO = Board.getNeighbourPoint(x, y, Direction.SO).x;
						int Y_SO = Board.getNeighbourPoint(x, y, Direction.SO).y;
						Piece placeSO = board.getBoardPlace(X_SO, Y_SO).getPiece();
						if (placeSO != null) {
							SOreturnBoolean = true;
						}

						boolean SEreturnBoolean = false;
						int X_SE = Board.getNeighbourPoint(x, y, Direction.SE).x;
						int Y_SE = Board.getNeighbourPoint(x, y, Direction.SE).y;
						Piece placeSE = board.getBoardPlace(X_SE, Y_SE).getPiece();
						if (placeSE != null) {
							SEreturnBoolean = true;
						}

						return NreturnBoolean && NEreturnBoolean && NOreturnBoolean && SreturnBoolean && SEreturnBoolean
								&& SOreturnBoolean;
					}
				}
			}
		}
		return false;
	}

	/**
	 * do end of game actions
	 */
	private void doFinishGameActions() {
		//se qualquer um dos jogador tiver ganho 
		if (playerAData.playerWon() || playerBData.playerWon()) {
			//ativa campos de username
			submit.setEnabled(true);
			nameField.setEnabled(true);
			// MUSICA FINAL
			if (mainSound != null)
				mainSound.close();
			if (gameSound != null)
				gameSound.close();
			music(mainSound);
			// painel final
			JPanel blank = new JPanel();
			blank.setBackground(new Color(0xC9F76F));
			add(blank);
			blank.setVisible(true);
			// campo de nome jogaodr
			blank.add(nameField);
			nameField.setVisible(true);
			// botao para guardar score
			blank.add(submit);
			submit.setVisible(true);
			// destivar componentes
			bn_giveUp.setEnabled(false);
			bn_changePlayer.setEnabled(false);
			bn_moveDown.setEnabled(false);
			bn_moveUp.setEnabled(false);
			bn_moveNO.setEnabled(false);
			bn_moveNE.setEnabled(false);
			bn_moveSO.setEnabled(false);
			bn_moveSE.setEnabled(false);

			setStatusInfo("game is over");

			playerAData.getSidePanel().setVisible(false);
			playerBData.getSidePanel().setVisible(false);
			board.setVisible(false);

			// mensagem depende do/s vencedor/s
			if (playerAData.playerWon() && playerBData.playerWon() == false) {
				mainLabel.setText("Player A HAS WON");
				endOfGame = true;
			} else if (playerAData.playerWon() == false && playerBData.playerWon()) {
				mainLabel.setText("Player B HAS WON");
				endOfGame = true;

			} else if (playerAData.playerWon() && playerBData.playerWon()) {
				mainLabel.setText("It's a Tie");
				nameField.setVisible(false);
				submit.setVisible(false);
				endOfGame = true;
			}
		}

	}
	
	//valida sempre na direção inversa para verificar primeiro peças mais a uma dir especifica - CASO NORTE começa a 0,0 para encontrar primeiro peças mais a norte
	//caso SUL começa a 0,MAX para validar primeiro peças mais a SUL
	
	/**
	 * move hive UP, if it can be moved
	 */
	private void moveHiveUp() {
		for (int y = 0; y < Board.DIMY; y++) {
			for (int x = 0; x < Board.DIMX; x++) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					int dst_x = Board.getNeighbourPoint(x, y, Direction.N).x;
					int dst_y = Board.getNeighbourPoint(x, y, Direction.N).y;
					if (board.getBoardPlace(x, y).pieces.size() > 1) {
						for (Piece peca : board.getBoardPlace(x, y).pieces) {
							board.getBoardPlace(dst_x, dst_y).addPiece(peca);
						}

						board.getBoardPlace(x, y).pieces.clear();

					} else {
						Piece toAdd = board.getBoardPlace(x, y).getPiece();
						board.getBoardPlace(x, y).remPiece(toAdd);
						board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

					}

					board.repaint();

				}
			}
		}

	}

	/**
	 * move hive DOWN, if it can
	 */
	private void moveDown() {
		for (int x = Board.DIMX - 1; x >= 0; x--) {
			for (int y = Board.DIMY - 1; y >= 0; y--) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					int dst_x = Board.getNeighbourPoint(x, y, Direction.S).x;
					int dst_y = Board.getNeighbourPoint(x, y, Direction.S).y;
					if (board.getBoardPlace(x, y).pieces.size() > 1) {
						for (Piece peca : board.getBoardPlace(x, y).pieces) {
							board.getBoardPlace(dst_x, dst_y).addPiece(peca);
						}

						board.getBoardPlace(x, y).pieces.clear();

					} else {
						Piece toAdd = board.getBoardPlace(x, y).getPiece();
						board.getBoardPlace(x, y).remPiece(toAdd);
						board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

					}

					board.repaint();

				}
			}
		}
	}

	/**
	 * move hive NO, if it can
	 */
	private void moveNO() {
		for (int y = 0; y < Board.DIMY; y++) {
			for (int x = 0; x < Board.DIMX; x++) {
				for (int p = 0; p < board.getBoardPlace(x, y).pieces.size(); p++) {
					if (board.getBoardPlace(x, y).pieces.get(p) != null) {
						int dst_x = Board.getNeighbourPoint(x, y, Direction.NO).x;
						int dst_y = Board.getNeighbourPoint(x, y, Direction.NO).y;
						if (board.getBoardPlace(x, y).pieces.size() > 1) {
							for (Piece peca : board.getBoardPlace(x, y).pieces) {
								board.getBoardPlace(dst_x, dst_y).addPiece(peca);
							}

							board.getBoardPlace(x, y).pieces.clear();

						} else {
							Piece toAdd = board.getBoardPlace(x, y).getPiece();
							board.getBoardPlace(x, y).remPiece(toAdd);
							board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

						}

						board.repaint();

					}
				}

			}
		}
	}

	/**
	 * move hive NE, if it can
	 */
	private void moveNE() {
		for (int x = Board.DIMX - 1; x >= 0; x--) {
			for (int y = 0; y < Board.DIMY; y++) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					int dst_x = Board.getNeighbourPoint(x, y, Direction.NE).x;
					int dst_y = Board.getNeighbourPoint(x, y, Direction.NE).y;
					if (board.getBoardPlace(x, y).pieces.size() > 1) {
						for (Piece peca : board.getBoardPlace(x, y).pieces) {
							board.getBoardPlace(dst_x, dst_y).addPiece(peca);
						}

						board.getBoardPlace(x, y).pieces.clear();

					} else {
						Piece toAdd = board.getBoardPlace(x, y).getPiece();
						board.getBoardPlace(x, y).remPiece(toAdd);
						board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

					}

					board.repaint();

				}
			}
		}
	}

	/**
	 * move hive SO, if it can
	 */
	private void moveSO() {
		for (int x = 0; x < Board.DIMX; x++) {
			for (int y = Board.DIMY - 1; y >= 0; y--) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					int dst_x = Board.getNeighbourPoint(x, y, Direction.SO).x;
					int dst_y = Board.getNeighbourPoint(x, y, Direction.SO).y;
					if (board.getBoardPlace(x, y).pieces.size() > 1) {
						for (Piece peca : board.getBoardPlace(x, y).pieces) {
							board.getBoardPlace(dst_x, dst_y).addPiece(peca);
						}

						board.getBoardPlace(x, y).pieces.clear();

					} else {
						Piece toAdd = board.getBoardPlace(x, y).getPiece();
						board.getBoardPlace(x, y).remPiece(toAdd);
						board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

					}

					board.repaint();

				}
			}
		}
	}

	/**
	 * move hive SE, if it can
	 */
	private void moveSE() {
		for (int x = Board.DIMX - 1; x >= 0; x--) {
			for (int y = Board.DIMY - 1; y >= 0; y--) {
				if (board.getBoardPlace(x, y).getPiece() != null) {
					int dst_x = Board.getNeighbourPoint(x, y, Direction.SE).x;
					int dst_y = Board.getNeighbourPoint(x, y, Direction.SE).y;
					if (board.getBoardPlace(x, y).pieces.size() > 1) {
						for (Piece peca : board.getBoardPlace(x, y).pieces) {
							board.getBoardPlace(dst_x, dst_y).addPiece(peca);
						}

						board.getBoardPlace(x, y).pieces.clear();

					} else {
						Piece toAdd = board.getBoardPlace(x, y).getPiece();
						board.getBoardPlace(x, y).remPiece(toAdd);
						board.getBoardPlace(dst_x, dst_y).addPiece(toAdd);

					}

					board.repaint();

				}
			}
		}
	}

	// Métodos e Vars não usados

	// private Font fontPieces;
	// private static final long serialVersionUID = 1L;
	// private boolean placingQueenBee = false;
	// private Font fontCurrentPlayer;
	// private static final int MAX_NUMBER_OF_MOVES_TO_PLACE_QUEENBEE = 4;

	/**
	 * load resources: fonts, images, sounds
	 * 
	 * private void loadResources() {
	 * }
	 * 
	 * Can move to border, used to check if piece can be selected on hive physically
	 * sliding from the border. We can use a ArrayList to keep the boardPlaces and
	 * try to find a way to the border. The ArrayList is used to avoid loops. If a
	 * new boardPlace is already in the ArrayList so it will start a loop, so
	 * abandon that boardPlace as not valid move. This method only call the
	 * auxiliary method.
	 * 
	 * private boolean canMoveToBorder(int x, int y) { 
	 * 		return canMoveToBorder(x, y, new ArrayList<BoardPlace>()); 
	 * }
	 * 
	 * 
	 * can move to border - auxiliary method NAO UTILIZADO
	 * 
	 * private boolean canMoveToBorder(int x, int y, ArrayList<BoardPlace> path) {
	 * 		return false; 
	 * }
	 * 
	 * 
	 * Move the received piece unconditionally from its position to target position
	 * with: remPiece and addPiece. //DEBUG - nao usado
	 * 
	 * public void moveUnconditional(Piece p, int x, int y) { int bp_x = p.getX();
	 * int bp_y = p.getY(); board.getBoardPlace(bp_x, bp_y).remPiece(p);
	 * board.repaint(); board.addPiece(p, x, y); }
	 * 
	 * 
	 * 
	 * change state of general buttons
	 * feito no setpanelactive do playerdata - botoes gerais nao
	 * 
	 * private void enableControlButtons(boolean enable){
	 * }
	 * 
	 */

}
