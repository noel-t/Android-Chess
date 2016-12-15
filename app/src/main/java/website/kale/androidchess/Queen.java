package website.kale.androidchess;

/**
 * Queen, subclass of ChessPiece
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class Queen extends ChessPiece {

	/**
	 * Sole constructor
	 * @param color	the color of the piece
	 */
	public Queen(Color color) {
		super(color);
		setType(ChessPiece.Type.QUEEN);
	}
}
