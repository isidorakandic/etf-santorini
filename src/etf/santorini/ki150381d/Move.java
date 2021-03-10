package etf.santorini.ki150381d;

public class Move {
	private Position posToMove;
	private Position posToBuild;
	private int player; // ili samo int
	private int piece;
	private Board board;

	public String toString() {
		return "player: " + player + ", piece:" + piece + ", posToMove: (" + posToMove.getX() + ", " + posToMove.getY()
				+ "), posToBuild: (" + posToBuild.getX() + ", " + posToBuild.getY() + ")";
	}

	// odakle se ovaj konstuktor poziva i sta ce mi?
	public Move(Board board, Position posToMove, Position posToBuild, int player, int piece) {
		this.board = board;
		this.posToMove = posToMove;
		this.posToBuild = posToBuild;
		this.player = player;
		this.piece = piece;
	}

	public Move(Board board, int player, int piece, int field, int build) throws CantMoveException {
		// konstruktor koji se poziva iz minimaxa i iz provere kraja
		this.player = player;
		this.piece = piece;
		this.board = board;
		posToMove = new Position();
		posToBuild = new Position();

		// odredjivanje pozicije za pomeranje

		if (field == 0 || field == 1 || field == 2) {
			posToMove.setX(board.getPlayer(player).getPosition(piece).getX() - 1);
		} else if (field == 3 || field == 4) {
			posToMove.setX(board.getPlayer(player).getPosition(piece).getX());
		} else {
			posToMove.setX(board.getPlayer(player).getPosition(piece).getX() + 1);
		}

		if (field == 0 || field == 3 || field == 5) {
			posToMove.setY(board.getPlayer(player).getPosition(piece).getY() - 1);
		} else if (field == 1 || field == 6) {
			posToMove.setY(board.getPlayer(player).getPosition(piece).getY());
		} else {
			posToMove.setY(board.getPlayer(player).getPosition(piece).getY() + 1);
		}

		// odredjivanje pozicije za gradnju
		// odredjuje se u odnosu na posToMove!
		if (build == 0 || build == 1 || build == 2) {
			posToBuild.setX(posToMove.getX() - 1);
		} else if (build == 3 || build == 4) {
			posToBuild.setX(posToMove.getX());
		} else {
			posToBuild.setX(posToMove.getX() + 1);
		}

		if (build == 0 || build == 3 || build == 5) {
			posToBuild.setY(posToMove.getY() - 1);
		} else if (build == 1 || build == 6) {
			posToBuild.setY(posToMove.getY());
		} else {
			posToBuild.setY(posToMove.getY() + 1);
		}

		// ako smo u cosku ne mozemo da se pomerimo
		if (posToMove.getX() < 0 || posToMove.getX() > 4 || posToBuild.getX() < 0 || posToBuild.getX() > 4
				|| posToMove.getY() < 0 || posToMove.getY() > 4 || posToBuild.getY() < 0 || posToBuild.getY() > 4)
			throw new CantMoveException();

	}

	public boolean isMovePossible() { // AI provera!

		Position currPos = board.getPlayer(player).getPosition(piece);
		// ne moze ako posToMove nije susedno nasem trenutnom polozaju
		if (currPos.getX() < posToMove.getX() - 1 || currPos.getX() > posToMove.getX() + 1)
			return false;
		if (currPos.getY() < posToMove.getY() - 1 || currPos.getY() > posToMove.getY() + 1)
			return false;
		// ne moze ako posToMove nije prazno
		// ovo obezbedjuje da ne mozemo da se pomerimo na mesto na kom trenutno stojimo
		if (board.getField(posToMove).getPiece() != 0)
			return false;
		// ne moze ako posToMove ima 4 plocice
		if (board.getField(posToMove).getTiles() == 4)
			return false;
		// ne moze ako je posToMove vise od 1 nivo visi od trenutne pozicije
		if (board.getField(posToMove).getTiles() > board.getField(currPos).getTiles() + 1)
			return false;

		// ne moze da se gradi ako polje nije susedno
		if (posToMove.getX() < posToBuild.getX() - 1 || posToMove.getX() > posToBuild.getX() + 1)
			return false;
		if (posToMove.getY() < posToBuild.getY() - 1 || posToMove.getY() > posToBuild.getY() + 1)
			return false;
		// ne moze da se gradi na isto polje na koje se pomera
		if (posToMove.getX() == posToBuild.getX() && posToMove.getY() == posToBuild.getY())
			return false;

		// ne moze da se gradi ako polje nije prazno
		// ali moze da se gradi na poziciji koja ce biti prazna nakon sto se pomeri sa
		// nje
		// ne moze ako polje nije prazno i piece koji se pomera nije na tom polju
		// ako piece koji se pomera jeste na tom polju onda moze

		if (board.getField(posToBuild).getPiece() != 0) {
			if (posToBuild.getX() != currPos.getX())
				return false;
			if (posToBuild.getY() != currPos.getY())
				return false;
		}

		// ne moze da se gradi ako posToBuild ima vec 4 plocice
		if (board.getField(posToBuild).getTiles() == 4)
			return false;

		return true;
	}

	public boolean isMovePossible_justMove() {
		Position currPos = board.getPlayer(player).getPosition(piece);
		// ne moze ako posToMove nije susedno nasem trenutnom polozaju
		if (currPos.getX() < posToMove.getX() - 1 || currPos.getX() > posToMove.getX() + 1)
			return false;
		if (currPos.getY() < posToMove.getY() - 1 || currPos.getY() > posToMove.getY() + 1)
			return false;
		// ne moze ako posToMove nije prazno
		if (board.getField(posToMove).getPiece() != 0)
			return false;
		// ne moze ako posToMove ima 4 plocice
		if (board.getField(posToMove).getTiles() == 4)
			return false;
		// ne moze ako je posToMove vise od 1 nivo visi od trenutne pozicije
		if (board.getField(posToMove).getTiles() > board.getField(currPos).getTiles() + 1)
			return false;
		return true;
	}

	public boolean isMovePossible_justBuild() {
		Position currPos = board.getPlayer(player).getPosition(piece);
		if (currPos.getX() < posToBuild.getX() - 1 || currPos.getX() > posToBuild.getX() + 1)
			return false;
		if (currPos.getY() < posToBuild.getY() - 1 || currPos.getY() > posToBuild.getY() + 1)
			return false;
		// ne moze da se gradi ako polje nije prazno
		if (board.getField(posToBuild).getPiece() != 0)
			return false;
		// ne moze da se gradi ako posToBuild ima vec 4 plocice
		if (board.getField(posToBuild).getTiles() == 4)
			return false;
		return true;
	}

	public void setBoard(Board board) { // AI ovo koristi
		this.board = board;
	}

	public Position getPosToMove() {
		return posToMove;
	}

	public void setPosToMove(Position posToMove) {
		this.posToMove = posToMove;
	}

	public Position getPosToBuild() {
		return posToBuild;
	}

	public void setPosToBuild(Position posToBuild) {
		this.posToBuild = posToBuild;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getPiece() {
		return piece;
	}

	public void setPiece(int piece) {
		this.piece = piece;
	}

}
