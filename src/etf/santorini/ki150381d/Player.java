package etf.santorini.ki150381d;

import java.util.concurrent.*;

public abstract class Player implements Runnable {
	public volatile static boolean theEnd = false;
	protected int id; // da li sam player 0 ili 1
	protected Board board;
	protected Position[] position; // 0 je poz piece0, 1 je poz piece1
	protected int lastBuilt; // poslednja pozicija na kojoj smo gradili, treba nam za minimax
	protected int lastPieceMoved; // isto zbog minimaxa

	private static Semaphore turn0 = null;
	private static Semaphore turn1 = null;
	// za pocetne pozicije, hocemo da prvo prvi igrac igra
	private static Semaphore canStart = new Semaphore(0, true);

	public static int turn = 0; // ostalo samo zbog prikaza! i load from file...

	static boolean setInitial = true;

	private boolean init = true;

	public Player(Board board) {
		this.board = board;
		position = new Position[2]; // indeksi 0 i 1
	}

	public Player(Player player) { // u minimaxu zelimo sve da kopiramo kad kopiramo board, pa i playere!
		id = player.id;
		// sta sa boardom? - to cu posle setovati u konstruktoru boarda
		board = null;
		position = new Position[2];
		position[0] = new Position(player.getPosition(0));
		position[1] = new Position(player.getPosition(1));
		lastBuilt = player.getLastBuilt();
		lastPieceMoved = player.getLastPieceMoved();
	}

	public abstract void setInitPositions();

	public void run() {
		try {

			// ------------------------
			if (turn0 == null)
				turn0 = new Semaphore((turn + 1) % 2, true);
			if (turn1 == null)
				turn1 = new Semaphore(turn, true);
			// ------------------------

			while (!theEnd) {
				if (id == 0) {
					turn0.acquire();
				} else {
					turn1.acquire();
				}

				if (!theEnd) { // proverimo da se kraj nije desio dok smo mi cekali potez
					// ja sam na potezu, igram.
					// prvo pocetne pozicije da se postave
					// -------------------------------------
					if (init && setInitial) {
						init = false;
						if (id == 1)
							canStart.acquire();
						setInitPositions(); // postavljanje pocetnih pozicija
						if (id == 0)
							canStart.release();
					} else { // ili je init ili je play
						// -------------------------
						board.makeMove_justMove(nextMove_toMove());

						board.makeMove_justBuild(nextMove_toBuild());
					}

					if (id == 0)
						turn1.release();
					else
						turn0.release();

					turn = (turn + 1) % 2; // jedino mesto gde se turn menja
					TheGame.mainFrame.lblTurnColor.repaint();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	protected abstract Move nextMove_toBuild();

	protected abstract Move nextMove_toMove();

	public Position getPosition(int i) {
		return position[i];
	}

	public void setPosition(int i, Position p) {
		position[i] = p;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLastBuilt() {
		return lastBuilt;
	}

	public int getLastPieceMoved() {
		return lastPieceMoved;
	}

	public void setBoard(Board board) {
		this.board = board;

	}

	public int getId() {
		return id;
	}

	public Board getBoard() {
		return board;
	}
}
