package etf.santorini.ki150381d.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import etf.santorini.ki150381d.*;

public class MoveActionListener implements ActionListener {
	private static int part = 0; // part 0 je move, part 1 je build
	private static int sel = 0; // 1 je piece0, 2 je piece1, 0 nista nije selektovano
	private static int playerToColor;
	private static Move move;

	public void actionPerformed(ActionEvent e) {
		Position clickedOn = ((MyJButton) e.getSource()).getXY();
		Player currPlayer = TheGame.board.getPlayer(Player.turn);

		if (currPlayer.getPosition(0).getX() == clickedOn.getX() && currPlayer.getPosition(0).getY() == clickedOn.getY()
				&& part == 0)
			sel = 1;
		else if (currPlayer.getPosition(1).getX() == clickedOn.getX()
				&& currPlayer.getPosition(1).getY() == clickedOn.getY() && part == 0) {
			sel = 2;
		} else if (sel != 0) { // ako smo nesto selektovali mozemo da pomerimo
			if (part == 0) {
				move = new Move(TheGame.board, clickedOn, null, Player.turn, sel - 1);
				if (move.isMovePossible_justMove()) {
					((Human) TheGame.board.getPlayer(Player.turn)).setMove_toMove(move);
					part = 1;
				}
				sel = 0;
			}
		} else if (part == 1) {
			move.setPosToBuild(clickedOn);
			if (move.isMovePossible_justBuild()) {
				((Human) TheGame.board.getPlayer(Player.turn)).setMove_toBuild(move);
				part = 0;
			}
		}

		if (sel != 0)
			playerToColor = Player.turn + 1;
		else
			playerToColor = 0;

		TheGame.mainFrame.boardPanel.colorSelected(playerToColor, clickedOn);
		TheGame.mainFrame.boardPanel.repaint();

	}

}
