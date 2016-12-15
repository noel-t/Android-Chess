package website.kale.androidchess;

public class ChessBoard {
	ChessPiece[][] board = new ChessPiece[8][8];
	
	public void initialize(){
		int i = 0;
		for(i = 0; i < 8; i++){									//Initialize Black Pieces
			if( i == 0 || i == 7 )
				board[0][i] = new Rook(ChessPiece.Color.BLACK);
			else if( i == 1 || i == 6 )
				board[0][i] = new Knight(ChessPiece.Color.BLACK);
			else if( i == 2 || i == 5 )
				board[0][i] = new Bishop(ChessPiece.Color.BLACK);
			else if( i == 3 )
				board[0][i] = new Queen(ChessPiece.Color.BLACK);
			else
				board[0][i] = new King(ChessPiece.Color.BLACK);
		}
		for(i = 0; i < 8; i++){
			board[1][i] = new Pawn(ChessPiece.Color.BLACK);
		}
		
		for(i = 0; i < 8; i++){									//Initialize White Pieces
			if( i == 0 || i == 7 )
				board[7][i] = new Rook(ChessPiece.Color.WHITE);
			else if( i == 1 || i == 6 )
				board[7][i] = new Knight(ChessPiece.Color.WHITE);
			else if( i == 2 || i == 5 )
				board[7][i] = new Bishop(ChessPiece.Color.WHITE);
			else if( i == 3 )
				board[7][i] = new Queen(ChessPiece.Color.WHITE);
			else
				board[7][i] = new King(ChessPiece.Color.WHITE);
		}
		for(i = 0; i < 8; i++){
			board[6][i] = new Pawn(ChessPiece.Color.WHITE);
		}
	}
	
	public void printBoard(){
		for(int row = 0; row < 9; row++){
			for(int column = 0; column < 9; column++){
				if(row == 8 || column == 8){
					if(row == 8 && column == 8){
						//Do nothing
					}
					else{
						if(row == 8)
							System.out.print(" " + (char)(column + 97));
						else
							System.out.print(rowToChessRank(row));
					}
				}
				
				else if(this.board[row][column] == null){
					if(row % 2 == 0 && column % 2 == 1 || row % 2 == 1 && column % 2 == 0)
						System.out.print("##");
					else
						System.out.print("  ");
				}
				
				else
					this.board[row][column].printAbbreviation();
				
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static int rowToChessRank(int row){
		if(row == 0)
			return 8;
		if(row == 1)
			return 7;
		if(row == 2)
			return 6;
		if(row == 3)
			return 5;
		if(row == 4)
			return 4;
		if(row == 5)
			return 3;
		if(row == 6)
			return 2;
		else
			return 1;
	}
	public static int chessRankToRow(int rank){
		if(rank == 1)
			return 7;
		if(rank == 2)
			return 6;
		if(rank == 3)
			return 5;
		if(rank == 4)
			return 4;
		if(rank == 5)
			return 3;
		if(rank == 6)
			return 2;
		if(rank == 7)
			return 1;
		else
			return 0;
	}
	
	public static int chessFileToColumn(char file){
		if(file == 'h')
			return 7;
		if(file == 'g')
			return 6;
		if(file == 'f')
			return 5;
		if(file == 'e')
			return 4;
		if(file == 'd')
			return 3;
		if(file == 'c')
			return 2;
		if(file == 'b')
			return 1;
		else
			return 0;
	}
	
	public static char columnToChessFile(int column){
		if(column == 0)
			return 'a';
		if(column == 1)
			return 'b';
		if(column == 2)
			return 'c';
		if(column == 3)
			return 'd';
		if(column == 4)
			return 'e';
		if(column == 5)
			return 'f';
		if(column == 6)
			return 'g';
		else
			return 'h';
	}
}
