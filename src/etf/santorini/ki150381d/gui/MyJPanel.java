package etf.santorini.ki150381d.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import etf.santorini.ki150381d.*;

@SuppressWarnings("serial")
public class MyJPanel extends JPanel {

	private MyJButton fields[][];
	private Board board;

	private static final Color font = new Color(255, 0, 127);
	private static final Color beige = new Color(255, 255, 204);
	private static final Color tile0 = new Color(204, 229, 255);
	private static final Color tile1 = new Color(153, 204, 255);
	private static final Color tile2 = new Color(102, 178, 255);
	private static final Color tile3 = new Color(51, 153, 255);
	private static final Color tile4 = new Color(0, 128, 255);

	static final Color player1 = new Color(153, 255, 153);
	private static final Color player1Selected = new Color(102, 255, 102);
	static final Color player2 = new Color(255, 153, 153);
	private static final Color player2Selected = new Color(255, 102, 102);

	// pomocni za obelezavanje selektovanog
	private int player = -1;
	private Position pos;

	public MyJPanel(MyJButton[][] fields, Board board) {
		super();
		this.fields = fields;
		this.board = board;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				fields[i][j].setForeground(new Color(0, 0, 0));
				// fields[i][j].setBackground(new Color(224, 255, 255));
				fields[i][j].setOpaque(true);
				fields[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				fields[i][j].setFont(new Font("Bookman Old Style", Font.ITALIC, 30));
				Border border = BorderFactory.createLineBorder(beige, 3);
				fields[i][j].setBorder(border);

				Field field = board.getField(i, j);

				fields[i][j].setForeground(font);

				switch (field.getTiles()) {
				case 0:
					fields[i][j].setBackground(tile0);
					fields[i][j].setText("0");
					break;
				case 1:
					fields[i][j].setBackground(tile1);
					fields[i][j].setText("1");
					break;
				case 2:
					fields[i][j].setBackground(tile2);
					fields[i][j].setText("2");
					break;
				case 3:
					fields[i][j].setBackground(tile3);
					fields[i][j].setText("3");
					break;
				case 4:
					fields[i][j].setBackground(tile4);
					fields[i][j].setText("4");
					break;
				default:
					break;
				}

				switch (field.getPlayer()) {
				case 1:
					fields[i][j].setBackground(player1); // malo tamnije
					break;
				case 2:
					fields[i][j].setBackground(player2); // svetlo crvena, rozikasta...
				default:
					break;

				}

				// bojenje selektovanog
				switch (player) {
				case 1:
					fields[pos.getX()][pos.getY()].setBackground(player1Selected);
					break;
				case 2:
					fields[pos.getX()][pos.getY()].setBackground(player2Selected);
					break;
				default:
					break;
				}
			}
	}

	public void colorSelected(int player, Position pos) {
		this.player = player;
		this.pos = pos;
	}
}
