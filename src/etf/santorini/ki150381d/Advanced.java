package etf.santorini.ki150381d;

public class Advanced implements Strategy {
	private int maxDepth; // ovo treba da se postavi kad pocne igra!
	private Move bestMove;
	private Player myPlayer;

	private Move lastMove;

	public Advanced(int depth, Player player) {
		maxDepth = depth;
		myPlayer = player;
	}

	public Move computeMove() {
		alphaBetaPruning(myPlayer.getBoard(), maxDepth, true, -Double.MAX_VALUE, Double.MAX_VALUE);
		bestMove.setBoard(myPlayer.getBoard());
		return bestMove;
	}

	public double heuristicValue(Board currBoard) {
		// da li ovo radi posao za terminalne slucajeve??
		Position moved = lastMove.getPosToMove();
		Position built = lastMove.getPosToBuild();
		double m = currBoard.getField(moved).getTiles();
		double l = currBoard.getField(built).getTiles();
		double k;
		double heuristicValue;
		// uvek racunam h. funkciju tako da veca odgovara meni, manja protivniku
		// max je trenutni igrac, min je protivnik
		int max = Player.turn;
		int min = (Player.turn + 1) % 2;
		k = distance(built, currBoard.getPlayer(min).getPosition(0))
				+ distance(built, currBoard.getPlayer(min).getPosition(1))
				- distance(built, currBoard.getPlayer(max).getPosition(0))
				- distance(built, currBoard.getPlayer(max).getPosition(1));
		heuristicValue = m + l * k;
		// ako mozemo da se popnemo na nivo 3 to uvek uradimo!
		// ako je potez pomeranje na 3 i mi ga pravimo to je super
		if (currBoard.getField(moved).getTiles() == 3)
			if (lastMove.getPlayer() == max)
				heuristicValue += 100;
			// ako on to radi to je lose!
			else if (lastMove.getPlayer() == min)
				heuristicValue -= 100;
		// na 4. nivo gradimo samo da blokiramo protivnika
		// ali i protivnik gradi na taj nivo samo da blokira nas, ali tu procenu necemo
		// uracunavati sad u ovo
		// ja gradim na 3 plocice!
		if (l == 3 && lastMove.getPlayer() == max) {
			Position[] opp = new Position[2];
			for (int i = 0; i < 2; i++) {
				opp[i] = new Position(currBoard.getPlayer(min).position[i]);
				// ako nije na 2 i nije blizu, ne gradi!
				if (currBoard.getField(opp[i]).getTiles() == 2 && Math.abs(built.getX() - opp[i].getX()) <= 1
						&& Math.abs(built.getY() - opp[i].getY()) <= 1) {
					heuristicValue += 100; // moramo da ga sprecimo da pobedi
				} else {
					heuristicValue -= 100;
				}
			}
		}
		// System.out.println("move: " + lastMove.toString() + "\t");
		// System.out.printf("h. value: %.2G \n", heuristicValue);
		return heuristicValue;

	}

	private double alphaBetaPruning(Board currBoard, int depth, boolean maxPlayer, double alpha, double beta) {
		double value;
		boolean prune = false;
		if (depth == 0 || currBoard.isTheEnd() != 0) { // or terminal position!
			return heuristicValue(currBoard);
		}
		if (maxPlayer) { // max player je uvek igrac0, neka bude
			value = -Double.MAX_VALUE;
			for (int piece = 0; piece < 2 && !prune; piece++) {
				for (int field = 0; field < 8 && !prune; field++) {
					for (int build = 0; build < 8 && !prune; build++) {
						Board newState = new Board(currBoard); // kopiramo tablu
						Move newMove = null;
						try {
							newMove = new Move(newState, myPlayer.getId(), piece, field, build); // ja igram potez pa je
																									// moj id
							// ja sam sebi uvek max
							if (newMove.isMovePossible()) { // provera da li je potez moguc (validan, po pravilima)
								newState.makeMove(newMove);
								lastMove = newMove;
								double ret = alphaBetaPruning(newState, depth - 1, false, alpha, beta);
								if (ret > value) {
									value = ret;
									if (depth == maxDepth) {
										bestMove = newMove;
//										System.out.println("----------------------------------------");
//										System.out.println("new best move: " + bestMove + "\n");
//										System.out.println("----------------------------------------");
									}
								}
								// *** dodatak u odnosu na minimax ***
								alpha = value > alpha ? value : alpha;
								if (alpha >= beta) {
									prune = true;
								}
							}
						} catch (CantMoveException e) {
							// System.out.println("can't make move");
						}
					}

				}
			}
		}

		else {
			value = Double.MAX_VALUE;
			for (int piece = 0; piece < 2 && !prune; piece++) {
				for (int field = 0; field < 8 && !prune; field++) {
					for (int build = 0; build < 8 && !prune; build++) {
						Board newState = new Board(currBoard); // kopiramo tablu
						try {
							Move newMove = new Move(newState, (myPlayer.getId() + 1) % 2, piece, field, build);
							// ovo je potez protivnika
							if (newMove.isMovePossible()) { // provera da li je potez moguc
								newState.makeMove(newMove);
								lastMove = newMove;
								double ret = alphaBetaPruning(newState, depth - 1, true, alpha, beta);
								value = ret < value ? ret : value;
								beta = value < beta ? value : beta;
								if (alpha > beta)
									prune = true;
							}
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
		Position p1 = null;

		// piece1 stavljamo negde u centar
		// -----------------------------
		p1 = new Position(2, 2);
		if (myPlayer.getBoard().getField(p1).getPiece() != 0)
			p1 = new Position(2, 3);
		else if (myPlayer.getBoard().getField(p1).getPiece() != 0)
			p1 = new Position(2, 1);
		// -----------------------------
		myPlayer.getBoard().initPosition(new Move(null, p1, null, myPlayer.getId(), 0));

		// piece 2
		// ako smo prvi

		Position p2 = null;

		if (myPlayer.getId() == 0)
			p2 = new Position(1, 1);
		else {
			Position[] opp = new Position[2];
			opp[0] = myPlayer.getBoard().getPlayer(0).getPosition(0);
			opp[1] = myPlayer.getBoard().getPlayer(0).getPosition(1);
			int[] q = new int[2]; // kvadrant u kome se nalaze igraci
			for (int i = 0; i < 2; i++) {
				if (opp[i].getX() <= 2 && opp[i].getY() <= 2)
					q[i] = 1;
				else if (opp[1].getX() > 2 && opp[1].getY() <= 2)
					q[i] = 2;
				else if (opp[i].getX() <= 2 && opp[i].getY() > 2)
					q[i] = 3;
				else
					q[i] = 4;
			}
			// ako je neki kvadrant prazan idemo u njega, else random
			if (q[0] != 1 && q[1] != 1)
				p2 = new Position(0, 0);
			else if (q[0] != 2 && q[1] != 2)
				p2 = new Position(0, 4);
			else if (q[0] != 3 && q[1] != 3)
				p2 = new Position(4, 0);
			else if (q[0] != 4 && q[1] != 4)
				p2 = new Position(4, 4);
			else { // random
				boolean end = false;
				int x = 0;
				int y = 0;
				while (!end) {
					x = (int) (Math.random() * 5);
					y = (int) (Math.random() * 5);
					p2 = new Position(x, y);
					if ((myPlayer.getBoard().getField(p2).getPiece() == 0) && (p1.getX() != x) && (p1.getY() != y))
						end = true;
				}
			}
		}
		// setujemo piece 2
		myPlayer.getBoard().initPosition(new Move(null, p2, null, myPlayer.getId(), 1));
	}
}
