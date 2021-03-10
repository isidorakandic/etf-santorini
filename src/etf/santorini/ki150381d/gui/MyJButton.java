package etf.santorini.ki150381d.gui;

import javax.swing.JButton;

import etf.santorini.ki150381d.Position;

@SuppressWarnings("serial")
public class MyJButton extends JButton {
	private int x;
	private int y;

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Position getXY() {
		return new Position(x, y);
	}
}
