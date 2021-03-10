package etf.santorini.ki150381d;

public class Minimax implements Strategy {
	private int maxDepth; // ovo treba da se postavi kad pocne igra!
	private Move bestMove;
	private Player myPlayer;

	private Move lastMove; // za h. funkciju

	public Minimax(int depth, Player player) {
		maxDepth = depth;
		myPlayer = player;
	}

	public Move computeMove() {
		minimax(myPlayer.getBoard(), maxDepth, true);
		bestMove.setBoard(myPlayer.getBoard());
		return bestMove;
	}

	public double heuristicValue(Board currBoard) {
		// da li ovo radi posao za terminalne slucajeve??
		Position moved = lastMove.getPosToMove();
		Position built = lastMove.getPosToBuild();
		double m = currBoard.getField(moved).getTiles();
		double l = currBoard.getField(built).getTiles();
		// uvek racunam h. funkciju tako da veca odgovara meni, manja protivniku
		// max je trenutni igrac, min je protivnik
		int max, min;
		max = Player.turn;
		min = (Player.turn + 1) % 2;
		double res;
		double k = distance(built, currBoard.getPlayer(min).getPosition(0))
				+ distance(built, currBoard.getPlayer(min).getPosition(1))
				- distance(built, currBoard.getPlayer(max).getPosition(0))
				- distance(built, currBoard.getPlayer(max).getPosition(1));
		res = m + l * k;
		// System.out.println("move: " + lastMove.toString() + "\t");
		// System.out.printf("h. value: %.2G \n", res);

		return res;
	}

	private double minimax(Board currBoard, int depth, boolean maxPlayer) {
		double value;
		if (depth == 0 || currBoard.isTheEnd() != 0) {
			return heuristicValue(currBoard);
		}
		if (maxPlayer) {
			value = -Double.MAX_VALUE;
			for (int piece = 0; piece < 2; piece++) {
				for (int field = 0; field < 8; field++) {
					for (int build = 0; build < 8; build++) {
						Board newState = new Board(currBoard); // kopiramo tablu
						Move newMove = null;
						try {
							newMove = new Move(newState, myPlayer.getId(), piece, field, build); // ja igram potez pa je
																									// moj id
							if (newMove.isMovePossible()) { // provera da li je potez moguc (validan, po pravilima)
								newState.makeMove(newMove);
								lastMove = newMove;
								double ret = minimax(newState, depth - 1, false);
								if (ret > value) {
									value = ret;
									if (depth == maxDepth) {
										bestMove = newMove;
										System.out.println("----------------------------------------");
										System.out.println("new best move: " + bestMove + "\n");
										System.out.println("----------------------------------------");
									}
								}
							} else
								continue;
						} catch (CantMoveException e) {
						}
					}
				}

			}
		}

		else {
			value = Double.MAX_VALUE;
			for (int piece = 0; piece < 2; piece++) {
				for (int field = 0; field < 8; field++) {
					for (int build = 0; build < 8; build++) {
						Board newState = new Board(currBoard); // kopiramo tablu
						try {
							Move newMove = new Move(newState, (myPlayer.getId() + 1) % 2, piece, field, build);
							// ovo je potez protivnika
							if (newMove.isMovePossible()) { // provera da li je potez moguc
								newState.makeMove(newMove);
								lastMove = newMove;
								double ret = minimax(newState, depth - 1, true);
								if (ret < value) {
									value = ret;
								}
							} else
								continue;
						} catch (CantMoveException e) {
							// samo nastavimo dalje
						}
					}
				}
			}
		}

		return value;
	}

	public void setInitPositions() {
		Position p = null;
		boolean end = false;

		// za piece 1
		int x1 = 0;
		int y1 = 0;
		while (!end) {
			x1 = (int) (Math.random() * 5);
			y1 = (int) (Math.random() * 5);
			p = new Position(x1, y1);
			if (myPlayer.getBoard().getField(p).getPiece() == 0) // prazno je
				end = true;
		}
		myPlayer.getBoard().initPosition(new Move(null, p, new Position(5, 5), myPlayer.getId(), 0));

		end = false;
		// za piece 2
		int x2 = 0;
		int y2 = 0;
		while (!end) {
			x2 = (int) (Math.random() * 5);
			y2 = (int) (Math.random() * 5);
			p = new Position(x2, y2);
			if ((myPlayer.getBoard().getField(p).getPiece() == 0) && (x1 != x2) && (y1 != y2)) // prazno je
				end = true;
		}
		myPlayer.getBoard().initPosition(new Move(null, p, new Position(5, 5), myPlayer.getId(), 1));
	}

}
