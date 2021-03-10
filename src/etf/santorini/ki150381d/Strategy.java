package etf.santorini.ki150381d;

public abstract interface Strategy {
	public Move computeMove();

	public void setInitPositions();

	public double heuristicValue(Board state);

	public default double distance(Position p1, Position p2) {
		return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
	}
}
