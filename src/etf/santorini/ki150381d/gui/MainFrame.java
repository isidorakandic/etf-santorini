package etf.santorini.ki150381d.gui;

import java.awt.*;

import javax.swing.*;

import etf.santorini.ki150381d.*;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class MainFrame {
	private JFrame frame;
	private MyJButton fields[][] = new MyJButton[5][5];
	private JPanel cards;
	public MyJPanel boardPanel; // public zbog refreah-a
	private JPanel newGamePanel;
	private JPanel mainPanel;

	// boje
	private static final Color beige = new Color(255, 255, 204);
	private static final Color blue = new Color(0, 153, 204);

	// font
	private static final Font font = new Font("Palatino Linotype", Font.ITALIC, 30);

	// za new game panel --------------
	private JTextField tfP1Piece1;
	private JTextField tfP1Piece2;
	private JTextField tfP1Depth;
	private JTextField tfP2Piece1;
	private JTextField tfP2Piece2;
	private JTextField tfP2Depth;
	JComboBox<String> cmbP1Algorithm;
	JComboBox<String> cmbP2Algorithm;
	JComboBox<String> cmbPlayer1;
	JComboBox<String> cmbPlayer2;
	JCheckBox chckbxLoadGame;
	// -------------------

	public JComboBox<String> cmbPiece; // kojim igracem igramo!

	public JLabel lblTurnColor = null;

	// --- objekti delovi realizacije sistema ---
	// private Board board;

	// Create the application.
	public MainFrame() {
		initialize();
		cmbPlayer1.setSelectedIndex(1);
		cmbPlayer2.setSelectedIndex(0);
		cmbP1Algorithm.setSelectedIndex(2);
		cmbP2Algorithm.setSelectedIndex(2);
		tfP1Piece1.setText("A1");
		tfP1Piece2.setText("B1");
		tfP2Piece1.setText("C3");
		tfP2Piece2.setText("C4");
		tfP1Depth.setText("3");
		tfP2Depth.setText("3");
		frame.setVisible(true);
	}

	private void initFrame() {
		frame = new JFrame();
		// frame.getContentPane().setBackground(beige);
		frame.setBounds(100, 100, 970, 740);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				TheGame.writer.close();
			}

		});
		frame.getContentPane().setLayout(null);
		frame.setTitle("Santorini");
	}

	private void initBoardPanel() {
		// tabla. dodamo je u glavni panel
		// tabla ima paint koji boji i ispisuje plocice i igrace
		// u MyJPanel je paint overridovano
		boardPanel = new MyJPanel(fields, TheGame.board);

		boardPanel.setBounds(121, 42, 550, 550);
		boardPanel.setLayout(new GridLayout(5, 5));

		// postavljanje polja u board
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				fields[i][j] = new MyJButton();
				fields[i][j].setXY(i, j);
				fields[i][j].addActionListener(new MoveActionListener());
				boardPanel.add(fields[i][j]);
			}

		mainPanel.add(boardPanel);
	}

	private void initNewGamePanel() {

		newGamePanel = new JPanel();
		newGamePanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		newGamePanel.setLayout(null);
		newGamePanel.setBackground(blue);

		// ************* PLAYER 1 *************
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(34, 56, 416, 451);
		leftPanel.setForeground(new Color(51, 153, 204));
		leftPanel.setBackground(new Color(255, 255, 204));
		newGamePanel.add(leftPanel);
		leftPanel.setLayout(null);

		JLabel lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setForeground(blue);
		lblPlayer1.setFont(font);
		lblPlayer1.setBounds(32, 53, 135, 52);
		leftPanel.add(lblPlayer1);

		tfP1Piece1 = new JTextField();
		tfP1Piece1.setForeground(new Color(102, 153, 204));
		tfP1Piece1.setBackground(beige);
		tfP1Piece1.setFont(font);
		tfP1Piece1.setBounds(192, 118, 179, 37);
		leftPanel.add(tfP1Piece1);
		tfP1Piece1.setColumns(10);

		JLabel lblP1Piece1 = new JLabel("Piece 1");
		lblP1Piece1.setForeground(blue);
		lblP1Piece1.setFont(font);
		lblP1Piece1.setBounds(32, 118, 135, 52);
		leftPanel.add(lblP1Piece1);

		JLabel lblP1Piece2 = new JLabel("Piece 2");
		lblP1Piece2.setForeground(blue);
		lblP1Piece2.setFont(font);
		lblP1Piece2.setBounds(32, 183, 135, 52);
		leftPanel.add(lblP1Piece2);

		JLabel lblP1Algorithm = new JLabel("Algorithm");
		lblP1Algorithm.setForeground(blue);
		lblP1Algorithm.setFont(font);
		lblP1Algorithm.setBounds(32, 248, 135, 52);
		leftPanel.add(lblP1Algorithm);

		JLabel lblP1Depth = new JLabel("Depth");
		lblP1Depth.setForeground(blue);
		lblP1Depth.setFont(font);
		lblP1Depth.setBounds(32, 313, 135, 52);
		leftPanel.add(lblP1Depth);

		cmbPlayer1 = new JComboBox<String>();
		cmbPlayer1.setOpaque(true);
		cmbPlayer1.setForeground(blue);
		cmbPlayer1.setModel(new DefaultComboBoxModel<String>(new String[] { "Human", "AI" }));
		cmbPlayer1.setFont(font);
		cmbPlayer1.setBackground(beige);
		cmbPlayer1.setBounds(192, 53, 179, 37);
		leftPanel.add(cmbPlayer1);
		cmbPlayer1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbPlayer1.getSelectedItem() == "Human") {
					tfP1Piece1.setEnabled(true);
					tfP1Piece2.setEnabled(true);
					cmbP1Algorithm.setEnabled(false);
					tfP1Depth.setEnabled(false);
					// ***
					cmbPlayer2.setModel(new DefaultComboBoxModel<String>(new String[] { "Human", "AI" }));
					tfP2Piece1.setEnabled(true);
					tfP2Piece2.setEnabled(true);
					cmbP2Algorithm.setEnabled(false);
					tfP2Depth.setEnabled(false);
				} else {
					tfP1Piece1.setEnabled(false);
					tfP1Piece2.setEnabled(false);
					cmbP1Algorithm.setEnabled(true);
					tfP1Depth.setEnabled(true);
					// ako je prvi igrac AI i drugi mora da bude, jer Human mora prvi da igra ako
					// postoji
					// (zbog pocetnih pozicija
					cmbPlayer2.setModel(new DefaultComboBoxModel<String>(new String[] { "AI" }));
					// i onda setujemo enabled za odg polja
					tfP2Piece1.setEnabled(false);
					tfP2Piece2.setEnabled(false);
					cmbP2Algorithm.setEnabled(true);
					tfP2Depth.setEnabled(true);
				}

			}
		});

		tfP1Piece2 = new JTextField();
		tfP1Piece2.setForeground(new Color(102, 153, 204));
		tfP1Piece2.setFont(font);
		tfP1Piece2.setColumns(10);
		tfP1Piece2.setBackground(beige);
		tfP1Piece2.setBounds(192, 183, 179, 37);
		leftPanel.add(tfP1Piece2);

		cmbP1Algorithm = new JComboBox<String>();
		cmbP1Algorithm.setModel(new DefaultComboBoxModel<String>(new String[] { "Minimax", "Alpha-Beta", "Advanced" }));
		cmbP1Algorithm.setOpaque(true);
		cmbP1Algorithm.setForeground(blue);
		cmbP1Algorithm.setFont(font);
		cmbP1Algorithm.setBackground(beige);
		cmbP1Algorithm.setBounds(192, 248, 179, 37);
		leftPanel.add(cmbP1Algorithm);
		cmbP1Algorithm.setEnabled(false);

		tfP1Depth = new JTextField();
		tfP1Depth.setForeground(new Color(102, 153, 204));
		tfP1Depth.setFont(font);
		tfP1Depth.setColumns(10);
		tfP1Depth.setBackground(beige);
		tfP1Depth.setBounds(192, 313, 179, 37);
		leftPanel.add(tfP1Depth);
		tfP1Depth.setEnabled(false);

		// ************* PLAYER 2 *************
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setForeground(new Color(51, 153, 204));
		rightPanel.setBackground(beige);
		rightPanel.setBounds(483, 56, 416, 451);
		newGamePanel.add(rightPanel);

		JLabel lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setForeground(blue);
		lblPlayer2.setFont(font);
		lblPlayer2.setBounds(32, 53, 135, 52);
		rightPanel.add(lblPlayer2);

		tfP2Piece1 = new JTextField();
		tfP2Piece1.setForeground(new Color(102, 153, 204));
		tfP2Piece1.setFont(font);
		tfP2Piece1.setColumns(10);
		tfP2Piece1.setBackground(beige);
		tfP2Piece1.setBounds(192, 118, 179, 37);
		rightPanel.add(tfP2Piece1);

		JLabel lblP2Piece1 = new JLabel("Piece 1");
		lblP2Piece1.setForeground(blue);
		lblP2Piece1.setFont(font);
		lblP2Piece1.setBounds(32, 118, 135, 52);
		rightPanel.add(lblP2Piece1);

		JLabel lblP2Piece2 = new JLabel("Piece 2");
		lblP2Piece2.setForeground(blue);
		lblP2Piece2.setFont(font);
		lblP2Piece2.setBounds(32, 183, 135, 52);
		rightPanel.add(lblP2Piece2);

		JLabel lblP2Algorithm = new JLabel("Algorithm");
		lblP2Algorithm.setForeground(blue);
		lblP2Algorithm.setFont(font);
		lblP2Algorithm.setBounds(32, 248, 135, 52);
		rightPanel.add(lblP2Algorithm);

		JLabel lblP2Depth = new JLabel("Depth");
		lblP2Depth.setForeground(blue);
		lblP2Depth.setFont(font);
		lblP2Depth.setBounds(32, 313, 135, 52);
		rightPanel.add(lblP2Depth);

		cmbPlayer2 = new JComboBox<String>();
		cmbPlayer2.setModel(new DefaultComboBoxModel<String>(new String[] { "Human", "AI" }));
		cmbPlayer2.setOpaque(true);
		cmbPlayer2.setForeground(blue);
		cmbPlayer2.setFont(font);
		cmbPlayer2.setBackground(beige);
		cmbPlayer2.setBounds(192, 53, 179, 37);
		rightPanel.add(cmbPlayer2);
		cmbPlayer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbPlayer2.getSelectedItem() == "Human") {
					tfP2Piece1.setEnabled(true);
					tfP2Piece2.setEnabled(true);
					cmbP2Algorithm.setEnabled(false);
					tfP2Depth.setEnabled(false);
				} else {
					tfP2Piece1.setEnabled(false);
					tfP2Piece2.setEnabled(false);
					cmbP2Algorithm.setEnabled(true);
					tfP2Depth.setEnabled(true);
				}

			}
		});

		tfP2Piece2 = new JTextField();
		tfP2Piece2.setForeground(new Color(102, 153, 204));
		tfP2Piece2.setFont(font);
		tfP2Piece2.setColumns(10);
		tfP2Piece2.setBackground(beige);
		tfP2Piece2.setBounds(192, 183, 179, 37);
		rightPanel.add(tfP2Piece2);

		cmbP2Algorithm = new JComboBox<String>();
		cmbP2Algorithm.setModel(new DefaultComboBoxModel<String>(new String[] { "Minimax", "Alpha-Beta", "Advanced" }));
		cmbP2Algorithm.setOpaque(true);
		cmbP2Algorithm.setForeground(blue);
		cmbP2Algorithm.setFont(font);
		cmbP2Algorithm.setBackground(beige);
		cmbP2Algorithm.setBounds(192, 248, 179, 37);
		rightPanel.add(cmbP2Algorithm);
		cmbP2Algorithm.setEnabled(false);

		tfP2Depth = new JTextField();
		tfP2Depth.setForeground(new Color(102, 153, 204));
		tfP2Depth.setFont(font);
		tfP2Depth.setColumns(10);
		tfP2Depth.setBackground(beige);
		tfP2Depth.setBounds(192, 313, 179, 37);
		rightPanel.add(tfP2Depth);
		tfP2Depth.setEnabled(false);

		// ************* SUBMIT & CHKBOX *************
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(395, 566, 151, 61);
		btnSubmit.setOpaque(true);
		btnSubmit.setForeground(new Color(51, 153, 204));
		btnSubmit.setBackground(beige);
		btnSubmit.setFont(new Font("Palatino Linotype", Font.BOLD | Font.ITALIC, 30));
		newGamePanel.add(btnSubmit);

		// --------- BTN ACTION LISTENER ----------------
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// igraci *****************
				Player player1, player2;
				if (cmbPlayer1.getSelectedItem() == "Human")
					player1 = new Human(TheGame.board);
				else
					player1 = new AI(TheGame.board);
				if (cmbPlayer2.getSelectedItem() == "Human")
					player2 = new Human(TheGame.board);
				else
					player2 = new AI(TheGame.board);
				TheGame.board.setPlayer1(player1);
				TheGame.board.setPlayer2(player2);

				// pocetne pozicije ***********
				if (tfP1Piece1.isEnabled())
					((Human) (player1)).setInitPiece1(strToPos(tfP1Piece1.getText()));

				if (tfP1Piece2.isEnabled())
					((Human) (player1)).setInitPiece2(strToPos(tfP1Piece2.getText()));

				if (tfP2Piece1.isEnabled())
					((Human) (player2)).setInitPiece1(strToPos(tfP2Piece1.getText()));

				if (tfP2Piece2.isEnabled())
					((Human) (player2)).setInitPiece2(strToPos(tfP2Piece2.getText()));

				// algorithm *************
				// PLAYER1
				if (cmbP1Algorithm.isEnabled()) {
					// dubina
					int depth = Integer.parseInt(tfP1Depth.getText());
					// algoritam
					if (cmbP1Algorithm.getSelectedItem() == "Minimax") {
						((AI) player1).setStrategy(new Minimax(depth, player1));
					} else if (cmbP1Algorithm.getSelectedItem() == "Alpha-Beta") {
						((AI) player1).setStrategy(new AlphaBeta(depth, player1));
					} else if (cmbP2Algorithm.getSelectedItem() == "Advanced") {
						((AI) player1).setStrategy(new Advanced(depth, player1));
					}
				}

				// PLAYER2
				if (cmbP2Algorithm.isEnabled()) {
					// dubina
					int depth = Integer.parseInt(tfP2Depth.getText());
					// algoritam
					if (cmbP2Algorithm.getSelectedItem() == "Minimax") {
						((AI) player2).setStrategy(new Minimax(depth, player2));
					} else if (cmbP2Algorithm.getSelectedItem() == "Alpha-Beta") {
						((AI) player2).setStrategy(new AlphaBeta(depth, player2));
					} else if (cmbP2Algorithm.getSelectedItem() == "Advanced") {
						((AI) player2).setStrategy(new Advanced(depth, player2));
					}
				}

				// *****************************

				// load game opcija --------------
				if (chckbxLoadGame.isSelected())
					TheGame.board.loadFromFile();
				// -------------------------------

				// promena prozora
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, "main");

				// pokreni niti
				new Thread(player1).start();
				new Thread(player2).start();
			}
		});

		// ----------------------------------------------

		chckbxLoadGame = new JCheckBox("Load game from file");
		chckbxLoadGame.setBounds(84, 572, 267, 49);
		chckbxLoadGame.setForeground(beige);
		chckbxLoadGame.setBackground(blue);
		chckbxLoadGame.setFont(font);
		chckbxLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxLoadGame.isSelected()) {
					tfP1Piece1.setEnabled(false);
					tfP1Piece2.setEnabled(false);
					tfP2Piece1.setEnabled(false);
					tfP2Piece2.setEnabled(false);
				} else {
					tfP1Piece1.setEnabled(true);
					tfP1Piece2.setEnabled(true);
					tfP2Piece1.setEnabled(true);
					tfP2Piece2.setEnabled(true);
				}

			}
		});
		newGamePanel.add(chckbxLoadGame);
	}

	@SuppressWarnings("serial")
	private void initMainPanel() {
		// def glavni panel, koji posle stavimo u cards
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		// mainPanel.setBackground(new Color(231, 240, 246));
		mainPanel.setBackground(beige);

		// A, B, C ....
		JPanel ver = new JPanel();
		mainPanel.add(ver);
		ver.setBounds(26, 42, 64, 550);
		ver.setLayout(new GridLayout(5, 1));
		String str = "ABCDE";

		// 1, 2, 3...
		JPanel hor = new JPanel();
		mainPanel.add(hor);
		hor.setBounds(121, 617, 550, 52);
		hor.setLayout(new GridLayout(1, 5));

		for (int i = 0; i < 5; i++) {
			Label lbl = new Label(str.substring(i, i + 1));
			lbl.setBackground(beige);
			lbl.setForeground(blue);
			lbl.setFont(font);
			lbl.setAlignment(Label.CENTER);
			ver.add(lbl);
		}

		for (int i = 0; i < 5; i++) {
			Label lbl = new Label("" + (i + 1));
			lbl.setBackground(beige);
			lbl.setForeground(blue);
			lbl.setFont(font);
			lbl.setAlignment(Label.CENTER);
			hor.add(lbl);
		}

		JLabel lblTurn = new JLabel("Turn");
		lblTurn.setFont(font);
		lblTurn.setForeground(blue);
		lblTurn.setBounds(748, 157, 107, 65);
		mainPanel.add(lblTurn);

		mainPanel.setLayout(null);

		// ovo treba da se updateuje pri promeni poteza!!!
		lblTurnColor = new JLabel("") {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (Player.turn == 0) {
					g.setColor(MyJPanel.player1);
				} else if (Player.turn == 1)
					g.setColor(MyJPanel.player2);
				g.fillOval(0, 0, this.getWidth(), this.getHeight());
			}
		};
		lblTurnColor.setOpaque(true);
		lblTurnColor.setBackground(beige);
		lblTurnColor.setBounds(745, 235, 80, 80);
		mainPanel.add(lblTurnColor);
	}

	// Initialize the contents of the frame.
	private void initialize() {

		initFrame();

		initMainPanel();

		initBoardPanel();

		// TheGame.board.setBoardPanel(boardPanel); // ovo nam treba za repaint

		initNewGamePanel();

		// za promenu prikazanog prozora definisemo cards panel
		cards = new JPanel();
		cards.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		cards.setLayout(new CardLayout());
		frame.getContentPane().add(cards);

		// dodamo nasa 2 panela u cards
		cards.add(newGamePanel, "newGame");
		cards.add(mainPanel, "main");

		// inicijalno prikazujemo new game --------
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, "newGame");
	}

	private Position strToPos(String str) {
		int x = 0;
		int y = 0;
		switch (str.substring(0, 1)) {
		case "A":
			x = 0;
			break;
		case "B":
			x = 1;
			break;
		case "C":
			x = 2;
			break;
		case "D":
			x = 3;
			break;
		case "E":
			x = 4;
			break;
		}
		y = Integer.parseInt(str.substring(1)) - 1;
		return new Position(x, y);
	}

	public void gameOver(int winner) {
		Player.theEnd = true;
		JLabel label = new JLabel("Game over! Winner is Player " + winner + "!");
		label.setFont(font);
		label.setForeground(blue);
		UIManager.put("Button.font", font);
		UIManager.put("Button.background", beige);
		UIManager.put("Button.foreground", blue);
		UIManager.put("OptionPane.background", beige);
		UIManager.put("Panel.background", beige);
		JOptionPane.showMessageDialog(frame, label, "", JOptionPane.PLAIN_MESSAGE);
		TheGame.writer.close();

	}
}