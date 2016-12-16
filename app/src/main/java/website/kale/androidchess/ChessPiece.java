package website.kale.androidchess;
import java.io.Serializable;

public abstract class ChessPiece implements Serializable {
	public enum Color {
		WHITE, BLACK;
	}
	
	public enum Type {
		PAWN, ROOK, KNIGHT, BISHOP, KING, QUEEN;
	}
	
	private Color color;
	private Type type;
	public boolean hasMoved = false;
	
	public ChessPiece(Color color) {
		this.color = color;
	}
	
	public final Color getColor() {
		return this.color;
	}
	
	public final void setType(Type type){
		this.type = type;
	}
	
	public final Type getType(){
		return this.type;
	}
	
	public void printAbbreviation(){
		if(this.getColor() == Color.WHITE)
			System.out.print("w");
		else
			System.out.print("b");
		
		if(this.type == Type.KING){
			System.out.print("K");
		}
		if(this.type == Type.BISHOP){
			System.out.print("B");
		}
		if(this.type == Type.KNIGHT){
			System.out.print("N");
		}
		if(this.type == Type.PAWN){
			System.out.print("p");
		}
		if(this.type == Type.QUEEN){
			System.out.print("Q");
		}
		if(this.type == Type.ROOK){
			System.out.print("R");
		}
	}
	
}
