package etf.santorini.ki150381d;

import java.io.IOException;
import java.io.PrintWriter;

import etf.santorini.ki150381d.gui.MainFrame;

public class TheGame {

	public static MainFrame mainFrame;
	public static Board board;

	public static PrintWriter writer;

	// Launch the application.
	public static void main(String[] args) {
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		board = new Board();
		mainFrame = new MainFrame();

	}

}
