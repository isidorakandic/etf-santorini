package etf.santorini.ki150381d;

public class Position {
	private int x, y;

	public Position() {

	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position p1) {
		x = p1.getX();
		y = p1.getY();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void set(Position p) {
		this.x = p.getX();
		this.y = p.getY();
	}

}
