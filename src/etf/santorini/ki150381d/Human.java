package etf.santorini.ki150381d;

import java.util.concurrent.Semaphore;

public class Human extends Player {
	private Move nextMove;
	private Semaphore moveReady = new Semaphore(0, true);
	private Semaphore buildReady = new Semaphore(0, true);

	private Position initPiece1;
	private Position initPiece2;

	public Human(Board board) {
		super(board);
	}

	public Human(Human human) {
		super(human);
		nextMove = null;
	}

	public Move nextMove_toMove() {
		try {
			moveReady.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return nextMove;
	}

	public Move nextMove_toBuild() {
		try {
			buildReady.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return nextMove;
	}

	public void setMove_toMove(Move move) {
		nextMove = move;
		moveReady.release();
	}

	public void setMove_toBuild(Move move) {
		nextMove.setPosToBuild(move.getPosToBuild());
		buildReady.release();
	}

	public void setInitPiece1(Position initPiece1) {
		this.initPiece1 = initPiece1;
	}

	public void setInitPiece2(Position initPiece2) {
		this.initPiece2 = initPiece2;
	}

	public void setInitPositions() {
		board.initPosition(new Move(null, initPiece1, null, id, 0));
		board.initPosition(new Move(null, initPiece2, null, id, 1));

	}
}
