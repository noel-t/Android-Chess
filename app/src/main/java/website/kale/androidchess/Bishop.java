package website.kale.androidchess;

public class Bishop extends ChessPiece {

	public Bishop(Color color) {
		super(color);
		setType(ChessPiece.Type.BISHOP);
	}
	
	/*public void printAbbreviation(){
		if(this.getColor() == ChessPiece.Color.WHITE)
			System.out.print("w");
		else
			System.out.print("b");
		System.out.print("B");
	}*/

}
