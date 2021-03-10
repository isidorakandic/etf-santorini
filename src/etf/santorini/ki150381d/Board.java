package etf.santorini.ki150381d;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
	public static final int ROWS = 5;
	public static final int COLUMNS = 5;
	private Field[][] fields;
	private Player[] players; // player 1 je MIN, player 0 je MAX

	private Position prevPos; // pomocna promenljiva zbog upisa u fajl

	public Board() {
		fields = new Field[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++)
				fields[i][j] = new Field();
		}
		players = new Player[2];
	}

	public Board(Board board) { // za novo stanje u minimaxu!
		fields = new Field[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				fields[i][j] = new Field(board.getField(i, j)); // deep copy
			}
		}

		players = new Player[2];
		for (int i = 0; i < 2; i++) {
			if (board.getPlayer(i) instanceof Human)
				players[i] = new Human((Human) board.getPlayer(i));
			else
				players[i] = new AI((AI) board.getPlayer(i));
			players[i].setBoard(this);
		}
	}

	public Field getField(int i, int j) {
		return fields[i][j];
	}

	public Field getField(Position p) {
		return fields[p.getX()][p.getY()];
	}

	public Player getPlayer(int i) {
		return players[i];
	}

	public void makeMove(Move move) { // za AI, samo za minimax
		// provera validnosti poteza da se uradi pre ovoga!
		int posToMoveX = move.getPosToMove().getX();
		int posToMoveY = move.getPosToMove().getY();
		int posToBuildX = move.getPosToBuild().getX();
		int posToBuildY = move.getPosToBuild().getY();
		int tiles = fields[posToBuildX][posToBuildY].getTiles() + 1;

		fields[posToBuildX][posToBuildY].setTiles(tiles);

		int player = move.getPlayer();
		int piece = move.getPiece();
		// prvo obrisemo igraca sa stare pozicije
		// ako igrac pamti gde se nalazi ne moramo da idemo kroz ovo

		Position currPos = players[player].getPosition(piece);
		fields[currPos.getX()][currPos.getY()].setPiece(0);
		fields[currPos.getX()][currPos.getY()].setPlayer(0);

		fields[posToMoveX][posToMoveY].setPlayer(player + 1); // +1 ????!!!???? ************
		fields[posToMoveX][posToMoveY].setPiece(piece + 1);

		players[player].setPosition(piece, move.getPosToMove());

		// boardPanel.repaint(); ovo se zove iz minimaxa pa nema sta da se repaintuje!

	}

	public void makeMove_justMove(Move move) {

		int posToMoveX = move.getPosToMove().getX();
		int posToMoveY = move.getPosToMove().getY();

		int player = move.getPlayer();
		int piece = move.getPiece();

		// prvo obrisemo igraca sa stare pozicije
		Position currPos = players[player].getPosition(piece);
		fields[currPos.getX()][currPos.getY()].setPiece(0);
		fields[currPos.getX()][currPos.getY()].setPlayer(0);

		// postavimo igraca na novu poziciju
		fields[posToMoveX][posToMoveY].setPlayer(player + 1);
		fields[posToMoveX][posToMoveY].setPiece(piece + 1);

		prevPos = players[player].getPosition(piece);

		players[player].setPosition(piece, move.getPosToMove());

		TheGame.mainFrame.boardPanel.repaint();

	}

	public void makeMove_justBuild(Move move) {
		// provera validnosti poteza da se uradi pre ovoga!
		int posToBuildX = move.getPosToBuild().getX();
		int posToBuildY = move.getPosToBuild().getY();
		int tiles = fields[posToBuildX][posToBuildY].getTiles() + 1;

		fields[posToBuildX][posToBuildY].setTiles(tiles);

		// upis u fajl, samo ovde ide
		writeToFile(move, prevPos);

		TheGame.mainFrame.boardPanel.repaint();

		int winner = isTheEnd();
		if (winner != 0) {
			TheGame.mainFrame.gameOver(winner);
		}
	}

	public void initPosition(Move move) {
		int posToMoveX = move.getPosToMove().getX();
		int posToMoveY = move.getPosToMove().getY();
		int player = move.getPlayer(); // ovde je 0 ili 1
		int piece = move.getPiece();

		fields[posToMoveX][posToMoveY].setPlayer(player + 1); // u field player je 0, 1 ili 2!
		fields[posToMoveX][posToMoveY].setPiece(piece + 1); // u field je 0, 1 ili 2

		players[player].setPosition(piece, move.getPosToMove());

		// upis u fajl
		writeToFileInit(move);

		TheGame.mainFrame.boardPanel.repaint();
	}

	public int isTheEnd() { // 0 nije kraj, 1 pobedio player0, 2 pobedio player1
		// pobeda ako igrac stoji na 3 plocice
		for (int player = 0; player < 2; player++) {
			for (int piece = 0; piece < 2; piece++) {
				Position pos = players[player].getPosition(piece);
				if (pos == null)
					System.out.println("pos je null");
				if (fields == null)
					System.out.println("field je null");
				if (fields[pos.getX()][pos.getY()].getTiles() == 3) {
					return player + 1;
				}
			}
		}
		// igrac ne moze vise da se pomeri
		int possibleMoves;
		for (int player = 0; player < 2; player++) {
			possibleMoves = 0;
			for (int piece = 0; piece < 2; piece++) {
				for (int field = 0; field < 8; field++) {
					for (int build = 0; build < 8; build++) {
						try {
							Move newMove = new Move(this, player, piece, field, build);
							if (newMove.isMovePossible()) { // provera da li je potez moguc
								possibleMoves++;
								break;
							}
							if (possibleMoves > 0) // ako smo vec nasli neku mogucnost ne moramo da proveravamo dalje
								break;
						} catch (CantMoveException e) {
							// nista, samo idemo u sledecu iteraciju
						}
					}
				}
			}
			if (possibleMoves == 0) {
				if (player == 0)
					return 2;
				else
					return 1;
			} else
				possibleMoves = 0;

		}
		return 0;
	}

	public void setPlayer1(Player player) {
		players[0] = player;
		player.setId(0);
	}

	public void setPlayer2(Player player) {
		players[1] = player;
		player.setId(1);
	}

	public void loadFromFile() {
		File file = new File("input.txt");
		Scanner reader;
		try {
			reader = new Scanner(file);
			String str;
			for (int i = 0; i < 2; i++) {
				str = reader.nextLine();
				String[] strs = str.split(" ");
				Position piece1 = StringToPos(strs[0]);
				Position piece2 = StringToPos(strs[1]);
				Move move = new Move(this, piece1, null, i, 0);
				initPosition(move);
				move = new Move(this, piece2, null, i, 1);
				initPosition(move);

			}
			while (reader.hasNextLine()) {
				str = reader.nextLine();
				String[] strs = str.split(" ");
				Position from = StringToPos(strs[0]);
				Position to = StringToPos(strs[1]);
				Position toBuild = StringToPos(strs[2]);
				int player = getField(from).getPlayer() - 1;
				int piece = getField(from).getPiece() - 1;
				Move move = new Move(this, to, toBuild, player, piece);
				makeMove_justMove(move);
				makeMove_justBuild(move);
				// promenimo i turn
				Player.turn = (Player.turn + 1) % 2;
			}
			reader.close();
			Player.setInitial = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void writeToFile(Move move, Position prevPos) {
		String str = PosToString(prevPos) + " "; // sa koje poz
		str = str.concat(PosToString(move.getPosToMove()) + " "); // na koju se pomera
		str = str.concat(PosToString(move.getPosToBuild())); // na koju se gradi
		TheGame.writer.println(str);
		TheGame.writer.flush();
	}

	private void writeToFileInit(Move move) {
		String str = PosToString(move.getPosToMove()) + " ";
		if (move.getPiece() == 1)
			TheGame.writer.println(str);
		else
			TheGame.writer.print(str);
		TheGame.writer.flush();
	}

	private String PosToString(Position p) {
		String str = "ABCDE";
		String ret = str.substring(p.getX(), p.getX() + 1);
		ret += p.getY() + 1;
		return ret;
	}

	private Position StringToPos(String str) {
		int x = str.charAt(0) - 'A';
		int y = str.charAt(1) - '0';
		y--;
		return new Position(x, y);

	}

}
