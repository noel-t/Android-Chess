package website.kale.androidchess;

/**
 * Representation of a chess square
 * @author Noel Taide, Doug Wittnebert
 *
 */
public class Square {
	private int row;
	private int column;
	public Square(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public void setRow(int row){
		this.row = row;
	}
	
	public void setColumn(int column){
		this.column = column;
	}

}
