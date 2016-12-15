package website.kale.androidchess;

/**
 * Knight, subclass of ChessPiece
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class Knight extends ChessPiece {
	
	/**
	 * Sole constructor
	 * @param color	the color of the piece
	 */
	public Knight(Color color) {
		super(color);
		setType(ChessPiece.Type.KNIGHT);
	}
}
