package website.kale.androidchess;

/**
 * Rook, subclass of ChessPiece
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class Rook extends ChessPiece {
	
	/**
	 * Sole constructor
	 * @param color	the color of the piece
	 */
	public Rook(Color color) {
		super(color);
		setType(ChessPiece.Type.ROOK);
	}
}
