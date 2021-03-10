package etf.santorini.ki150381d;

public class AI extends Player {
	public int maxDepth = 4; // ovo treba da se postavi kad pocne igra!
	private Move bestMove;
	private Strategy strategy;

	public AI(Board board) {
		super(board);
		bestMove = null;
	}

	public AI(AI ai) {
		super(ai);
		bestMove = null;
	}

	public Move nextMove_toMove() {
		long time = System.currentTimeMillis();
		bestMove = strategy.computeMove();
		long elapsed = System.currentTimeMillis() - time;
		long toSleep = 1500 - elapsed > 0 ? 1500 - elapsed : 1;
		try {
			Thread.sleep(toSleep); // radi preglednosti
		} catch (InterruptedException e) {
		}

		return bestMove;

	}

	public Move nextMove_toBuild() {
		try {
			Thread.sleep(1500); // radi preglednosti
		} catch (InterruptedException e) {
		}
		return bestMove; // onaj move koji je nextMove_toMove setovao
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public void setInitPositions() {
		strategy.setInitPositions();
	}

}
