package etf.santorini.ki150381d;

public class Field {
	// private int x, y; // pozicija
	private int player; // 0 prazno; 1 prvi igrac; 2 drugi igrac
	private int piece; // 0 prazno, 1 prvi piece igraca, 2 drugi piece igraca
	private int tiles; // broj plocica na datom polju

	public Field() {
		player = 0;
		piece = 0;
		tiles = 0;
	}

	public Field(Field field) {
		player = field.getPlayer();
		piece = field.getPiece();
		tiles = field.getTiles();
	}

	public int getTiles() {
		return tiles;
	}

	public void setTiles(int tiles) {
		this.tiles = tiles;
	}

	public int getPiece() {
		return piece;
	}

	public void setPiece(int piece) {
		this.piece = piece;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

}
