package website.kale.androidchess;

/**
 * King, subclass of ChessPiece
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class King extends ChessPiece {

	/**
	 * Sole constructor
	 * @param color	the color of the piece
	 */
	public King(Color color) {
		super(color);
		setType(ChessPiece.Type.KING);
	}
}
