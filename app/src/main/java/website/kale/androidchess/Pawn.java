package website.kale.androidchess;

/**
 * Pawn, subclass of ChessPiece
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class Pawn extends ChessPiece {
	
	/**
	 * Sole constructor
	 * @param color	the color of the piece
	 */
	public Pawn(Color color) {
		super(color);
		setType(ChessPiece.Type.PAWN);
	}
}
