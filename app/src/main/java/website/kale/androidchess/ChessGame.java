package website.kale.androidchess;
import java.io.*;
import java.util.ArrayList;


/**The driver, that is, the control flow of the game itself.
 * @author Noel Taide, Doug Wittnebert
 */

public class ChessGame{
	//public static boolean whiteToMove;
	public static ChessBoard board;
	public static boolean drawOffered = false;
	public static boolean unDid = false;
	public static boolean gameOver = false;
	public static ChessPiece takenPiece = null;
	public static ChessPiece.Type promotion = null;
	public static ArrayList<ChessBoard> moveList = new ArrayList<ChessBoard>();

	ChessGame(){
		board = new ChessBoard();
		board.initialize();
		moveList.add(board.getCopy());
	}

	public static void main(String[] args) throws IOException{

		while(!gameOver){
			if(isInStaleMate(colorToMove(board.whiteToMove))){
				printStaleMate();
				gameOver = true;
				return;
			}
			board.printBoard();
			if(board.whiteToMove)
				System.out.print("White's move: ");
			else
				System.out.print("Black's move: ");

			String move = getValidMove(board.whiteToMove);

			if(move.compareToIgnoreCase("undo") == 0){
				if(unDid == true){
					System.out.println("Can't undo more than once");
				}
				else if(moveList.size() > 1){
					System.out.println("Attempting to undo");
					//board = moveList.remove(moveList.size() - 2);
					//moveList.remove(moveList.size()-1);
					moveList.remove(moveList.size() - 1);
					board = moveList.get(moveList.size() - 1).getCopy();
					unDid = true;
				}
			}
			else{
				if(move.compareToIgnoreCase("resign") == 0){
					if(colorToMove(board.whiteToMove) == ChessPiece.Color.WHITE){
						System.out.println("Black wins");
						gameOver = true;
						System.out.println("Now printing all moves");
						for(ChessBoard i : moveList){
							i.printBoard();
						}
						return;
					}
					else{
						System.out.println("White wins");
						gameOver = true;
						System.out.println("Now printing all moves");
						for(ChessBoard i : moveList){
							i.printBoard();
						}
						return;
					}
				}

				else if(move.compareToIgnoreCase("draw?") == 0){
					if(offerDraw()){
						System.out.println("Draw");
						gameOver = true;
						return;
					}
				}
				else{
					Square origin = new Square(ChessBoard.chessRankToRow((move.charAt(1)) - '0'),ChessBoard.chessFileToColumn(move.charAt(0)));
					Square destination = new Square(ChessBoard.chessRankToRow(move.charAt(4) - '0'),ChessBoard.chessFileToColumn(move.charAt(3)));
					takenPiece = pieceAt(destination);
					makeMove(origin, destination);
					//moveList.add(board.getCopy());

					ChessPiece.Color currentColor = colorToMove(board.whiteToMove);
					ChessPiece.Color opposingColor = colorToMove(!board.whiteToMove);

					if(isInCheck(currentColor)){			//If player has put himself in check, revert move, declare invalid
						makeMove(destination, origin);
						board.board[destination.getRow()][destination.getColumn()] = takenPiece;
						printIllegalMove();
					}

					else{
						board.board[destination.getRow()][destination.getColumn()].hasMoved = true;
						if((destination.getRow() == 7 || destination.getRow() == 0) && board.board[destination.getRow()][destination.getColumn()].getColor() == currentColor && board.board[destination.getRow()][destination.getColumn()].getType() == ChessPiece.Type.PAWN){
							if(move.length() == 7){
								promotion = abbreviationToType(move.charAt(6));
								board.board[destination.getRow()][destination.getColumn()].setType(promotion);
							}
							else{
								board.board[destination.getRow()][destination.getColumn()].setType(ChessPiece.Type.QUEEN);
							}
						}

						if(isInCheck(opposingColor)){
							if(isInCheckMate(opposingColor)){
								board.printBoard();
								printCheckMate();
								if(board.whiteToMove){
									System.out.println("White wins");
								}
								else{
									System.out.println("Black wins");
								}
								gameOver = true;
								return;
							}
							else{
								printCheck();
							}
						}
						board.whiteToMove = !board.whiteToMove;
					}
					if(board.enPassant == true){
						board.board[board.prevMove.getRow()][board.prevMove.getColumn()] = null;
						board.enPassant = false;
					}
					board.prevMove = destination;
					moveList.add(board.getCopy());
					unDid = false;
					System.out.println();
				}
			}
		}
	}

	/**
	 * Asks the user for his or her move, only accepting valid moves.
	 * @param whiteToMove boolean describing whose turn it is to move
	 * @return String of length 5 or 7, describing the user's move
	 * @throws IOException if program fails to read user's input
	 */
	public static String getValidMove(boolean whiteToMove) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean valid = false;
		while(!valid){
			String moveString = br.readLine();

			if(moveString.compareToIgnoreCase("undo") == 0){
				valid = true;
				return moveString;
			}

			if(moveString.compareToIgnoreCase("resign") == 0){
				valid = true;
				return moveString;
			}
			if(moveString.compareToIgnoreCase("draw?") == 0){
				valid = true;
				return moveString;
			}
			else if(validateMove(moveString)){
				valid = true;
				return moveString;
			}
		}

		return null;
	}

	/*
	 * Checks whether the given move is valid for that player:
	 * 	1) Call validateFormat() to check that String move is well-formatted
	 * 	2) Player may only move their own piece
	 * 	TODO:
	 *  	3) Piece can only move legally
	 *  	4) Player cannot put himself in Check.
	 */

	/**
	 * Checks whether the given move is valid for that player.
	 * @param move a String of length 5 or 7 describing the player's attempted move.
	 * @return boolean associated with the validity of the player's move.
	 */
	public static boolean validateMove(String move){
		move = move.trim();
		move = move.toLowerCase();
		if(!validateFormat(move)){	//1)
			printIllegalMove();
			return false;
		}

		int originRow = ChessBoard.chessRankToRow((move.charAt(1)) - '0');
		int originColumn = ChessBoard.chessFileToColumn(move.charAt(0));
		Square origin = new Square(originRow,originColumn);

		int destinationRow = ChessBoard.chessRankToRow(move.charAt(4) - '0');
		int destinationColumn = ChessBoard.chessFileToColumn(move.charAt(3));
		Square destination = new Square(destinationRow,destinationColumn);

		if(!checkMovingOwnPiece(origin)){	//2)
			printIllegalMove();
			return false;
		}

		if(move.length() == 7 && board.board[origin.getRow()][origin.getColumn()].getType() != ChessPiece.Type.PAWN){
			return false;
		}

		if(!checkLegality(origin,destination)){	//3
			printIllegalMove();
			return false;

		}
		/* TODO: Implement 3) - 4)
		 *
		 */

		return true;
	}

	/**
	 * Checks whether the 5-or-7-character-long String has a valid format.
	 * @param move String describing the player's attempted move
	 * @return boolean telling describing the user's input is well-formatted
	 */
	public static boolean validateFormat(String move){
		if(move.length() != 5 && move.length() != 7){
			return false;
		}

		if(move.length() == 7){
			if(move.charAt(6) != 'r' && move.charAt(6) != 'n' && move.charAt(6) != 'b' && move.charAt(6) != 'q'
					&& move.charAt(6) != 'R' && move.charAt(6) != 'N' && move.charAt(6) != 'B' && move.charAt(6) != 'Q'){
				return false;
			}
			if(move.charAt(4) != '1' && move.charAt(4) != '8'){
				return false;
			}
		}

		if(move.charAt(0) < 97 || move.charAt(0)  > 104 || move.charAt(3)  < 97 || move.charAt(3)  > 104 ||	//(Column < 'a') or (Column >'h')
				move.charAt(1) < 49 || move.charAt(1) > 56 || move.charAt(4) < 49 || move.charAt(4) > 56){  // (Rank <'1') or (Rank > '8')
			return false;
		}
		return true;
	}

	public static boolean offerDraw() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println();
		if(board.whiteToMove){
			System.out.print("Black, ");
		}
		else{
			System.out.print("White, ");
		}
		System.out.println("enter 'draw' to accept draw offer");
		String moveString = br.readLine();
		System.out.println();
		if(moveString.compareToIgnoreCase("draw") == 0){
			return true;
		}
		return false;
	}

	/*
	 * Checks whether a piece exists at given square. If it does, its color must match with boolean whiteToMove
	 */

	/**
	 * Checks whether a piece exists at a given square. If it does, its color must match with boolean whiteToMove
	 * @param origin, the square holding the piece to be moved
	 * @return boolean describing whether the player is trying to move his own piece.
	 */
	public static boolean checkMovingOwnPiece(Square origin){
		if(board.board[origin.getRow()][origin.getColumn()] == null){
			return false;
		}
		if((!board.whiteToMove && board.board[origin.getRow()][origin.getColumn()].getColor() == ChessPiece.Color.WHITE) ||
				(board.whiteToMove && board.board[origin.getRow()][origin.getColumn()].getColor() == ChessPiece.Color.BLACK))
		{
			return false;
		}
		return true;
	}


	/**
	 * Checks that a piece cannot move to a square occupied by the same color.
	 * Checks arithmetically whether the piece on a given square can move to its destination, based on that piece's basic Chess rules.
	 * Calls helper methods for collision-checking.
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the given move is legal according to chess rules
	 */
	public static boolean checkLegality(Square origin, Square destination){
		if(board.board[destination.getRow()][destination.getColumn()] != null){ //Checks that piece can't move to square occupied by same color
			if(board.board[origin.getRow()][origin.getColumn()].getColor() == board.board[destination.getRow()][destination.getColumn()].getColor()){
				return false;
			}
		}

		ChessPiece.Type type = board.board[origin.getRow()][origin.getColumn()].getType();
		switch(type){
			case KNIGHT:
				if(!checkKnightMove(origin,destination)){
					return false;
				}
				break;
			case QUEEN:
				if(!checkQueenMove(origin,destination)){
					return false;
				}
				break;
			case PAWN:
				if(!checkPawnMove(origin,destination)){
					return false;
				}
				break;

			case BISHOP:
				if(!checkBishopMove(origin,destination)){
					return false;
				}
				break;
			case ROOK:
				if(!checkRookMove(origin,destination)){
					return false;
				}
				break;
			case KING:
				if(!checkKingMove(origin,destination)){
					return false;
				}
				break;
		}
		return true;
	}

	/**
	 * Checks whether the knight on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkKnightMove(Square origin, Square destination){
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();

		if(Math.abs(originRow - destRow) > 2 || Math.abs(originRow - destRow) > 2){
			return false;
		}

		if(Math.abs(originRow - destRow) == 2 && Math.abs(originColumn - destColumn) != 1){
			return false;
		}
		if(Math.abs(originRow - destRow) == 1 && Math.abs(originColumn - destColumn) != 2){
			return false;
		}
		if(Math.abs(originRow - destRow) == 0){
			return false;
		}
		if(Math.abs(originColumn - destColumn) == 0){
			return false;
		}

		return true;
	}

	/**
	 * Checks whether the queen on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkQueenMove(Square origin, Square destination){
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();
		int tempRow = originRow;
		int tempColumn = originColumn;

		//Basic movement check, return false if the move would be illegal given on an empty board
		if(Math.abs((destRow - originRow)) != Math.abs((destColumn - originColumn)) &&
				(originRow != destRow) && (originColumn != destColumn)){
			return false;
		}

		//8 conditionals below do collision checking for each movement case
		if(destRow == originRow && destColumn > originColumn){			//Move right
			tempColumn++;
			while(tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempColumn++;
			}
		}

		else if(destRow > originRow && destColumn > originColumn){		//Move down-right
			tempRow++;
			tempColumn++;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
				tempColumn++;
			}
		}

		else if(destRow > originRow && destColumn == originColumn){		//Move down
			tempRow++;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
			}
		}

		else if(destRow > originRow && destColumn < originColumn){		//Move down-left
			tempRow++;
			tempColumn--;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
				tempColumn--;
			}
		}

		else if(destRow == originRow && destColumn < originColumn){		//Move left
			tempColumn--;
			while(tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempColumn--;
			}
		}

		else if(destRow < originRow && destColumn < originColumn){		//Move up-left
			tempRow--;
			tempColumn--;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
				tempColumn--;
			}
		}

		else if(destRow < originRow && destColumn == originColumn){		//Move up
			tempRow--;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
			}
		}

		else if(destRow < originRow && destColumn > originColumn){		//Move up-right
			tempRow--;
			tempColumn++;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
				tempColumn++;
			}
		}

		return true;
	}

	/**
	 * Checks whether the pawn on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkPawnMove(Square origin, Square destination){ //TODO: En passant fix
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();
		int tempRow = originRow;
		int tempColumn = originColumn;

		if(Math.abs(destColumn - originColumn) > 1 || Math.abs(destRow - originRow) > 2){
			return false;
		}

		if(Math.abs(destColumn - originColumn) == 1 && Math.abs(destRow - originRow) == 2){
			return false;
		}

		ChessPiece.Color pawnColor = board.board[originRow][originColumn].getColor();
		if(pawnColor == ChessPiece.Color.WHITE){
			if(destRow - originRow >= 0){						//Pawn can only move forwards, never sideways
				return false;
			}

			if(destRow - originRow == -2){						//Pawn takes 2 steps forward
				if(board.board[originRow][originColumn].hasMoved){
					return false;
				}
				if(board.board[destRow][destColumn] != null){ //Collision checking
					return false;
				}
				if(board.board[destRow+1][destColumn] != null){
					return false;
				}
			}

			if(Math.abs(destColumn - originColumn) == 1){		//Diagonal capture
				if(board.board[destRow][destColumn] == null){
					if(board.board[destRow+1][destColumn] == null){
						return false;
					}
					if(board.board[destRow+1][destColumn].getType() == ChessPiece.Type.PAWN && board.board[destRow+1][destColumn].getColor() != pawnColor){
						if(board.prevMove.getRow() == destRow+1 && board.prevMove.getColumn() == destColumn){
							board.enPassant = true;
							return true;
						}
					}

					return false;
				}
			}

			if(destRow - originRow == -1 && destColumn - originColumn == 0){
				if(board.board[destRow][destColumn] != null){
					return false;
				}
			}
		}
		else if(pawnColor == ChessPiece.Color.BLACK){
			if(destRow - originRow <= 0){						//Pawn can only move forwards, never sideways
				return false;
			}

			if(destRow - originRow == 2){						//Pawn takes 2 steps forward
				if(board.board[originRow][originColumn].hasMoved){
					return false;
				}
				if(board.board[destRow][destColumn] != null){ //Collision checking
					return false;
				}
				if(board.board[destRow-1][destColumn] != null){
					return false;
				}
			}

			if(Math.abs(destColumn - originColumn) == 1){		//Diagonal capture
				if(board.board[destRow][destColumn] == null){
					if(board.board[destRow+1][destColumn] == null){
						return false;
					}
					if(board.board[destRow+1][destColumn].getType() == ChessPiece.Type.PAWN && board.board[destRow+1][destColumn].getColor() != pawnColor){
						if(board.prevMove.getRow() == destRow+1 && board.prevMove.getColumn() == destColumn){
							board.enPassant = true;
							return true;
						}
					}
					return false;
				}
			}

			if(destRow - originRow == 1 && destColumn - originColumn == 0){
				if(board.board[destRow][destColumn] != null){
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Checks whether the bishop on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkBishopMove(Square origin, Square destination){
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();
		int tempRow = originRow;
		int tempColumn = originColumn;

		//Basic movement check, return false if the move would be illegal given on an empty board
		if(Math.abs((destRow - originRow)) != Math.abs((destColumn - originColumn))){
			return false;
		}

		//4 conditionals below do collision checking for each movement case
		if(destRow > originRow && destColumn > originColumn){		//Move down-right
			tempRow++;
			tempColumn++;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
				tempColumn++;
			}
		}

		else if(destRow > originRow && destColumn < originColumn){		//Move down-left
			tempRow++;
			tempColumn--;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
				tempColumn--;
			}
		}

		else if(destRow < originRow && destColumn < originColumn){		//Move up-left
			tempRow--;
			tempColumn--;
			while(tempRow != destRow && tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
				tempColumn--;
			}
		}

		else if(destRow < originRow && destColumn > originColumn){		//Move up-right
			tempRow--;
			tempColumn++;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
				tempColumn++;
			}
		}

		return true;
	}

	/**
	 * Checks whether the rook on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkRookMove(Square origin, Square destination){
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();
		int tempRow = originRow;
		int tempColumn = originColumn;

		//Basic movement check, return false if the move would be illegal given on an empty board
		if((originRow != destRow) && (originColumn != destColumn)){
			return false;
		}

		//4 conditionals below do collision checking for each movement case
		if(destRow == originRow && destColumn > originColumn){			//Move right
			tempColumn++;
			while(tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempColumn++;
			}
		}

		else if(destRow > originRow && destColumn == originColumn){		//Move down
			tempRow++;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow++;
			}
		}

		else if(destRow == originRow && destColumn < originColumn){		//Move left
			tempColumn--;
			while(tempColumn != destColumn && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempColumn--;
			}
		}

		else if(destRow < originRow && destColumn == originColumn){		//Move up
			tempRow--;
			while(tempRow != destRow && isInBounds(tempRow,tempColumn)){
				if(board.board[tempRow][tempColumn] != null){
					return false;
				}
				tempRow--;
			}
		}

		return true;
	}


	/**
	 * Checks whether the king on the square origin can move to its destination legally
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 * @return boolean describing whether the move is legal.
	 */
	public static boolean checkKingMove(Square origin, Square destination){
		int originRow = origin.getRow();
		int originColumn = origin.getColumn();
		int destRow = destination.getRow();
		int destColumn = destination.getColumn();

		if(Math.abs(destRow - originRow) > 1){
			return false;
		}

		// White queen's side castle
		if(originRow == 7 && destRow == 7 && destColumn == 2){
			if(!board.board[originRow][originColumn].hasMoved){
				if(!isThreatened(origin,ChessPiece.Color.BLACK)){
					if(board.board[originRow][originColumn-1] == null && !isThreatened(new Square(originRow,originColumn-1),ChessPiece.Color.BLACK)){
						if(board.board[originRow][originColumn-2] == null && !isThreatened(new Square(originRow,originColumn-2),ChessPiece.Color.BLACK)){
							if(board.board[originRow][originColumn-3] == null){
								if(board.board[originRow][originColumn-4] != null){
									if(board.board[originRow][originColumn-4].getType() == ChessPiece.Type.ROOK){
										if(!board.board[originRow][originColumn-4].hasMoved){
											//System.out.println("White queen's side castle successful");
											makeMove(new Square(7,0), new Square(7, 3));
											if(board.board[7][3] != null)
												board.board[7][3].hasMoved = true;
											if(board.board[7][2] != null)
												board.board[7][2].hasMoved = true;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// Black queen's side castle
		else if(originRow == 0 && destRow == 0 && destColumn == 2){
			if(!board.board[originRow][originColumn].hasMoved){
				if(!isThreatened(origin,ChessPiece.Color.WHITE)){
					if(board.board[originRow][originColumn-1] == null && !isThreatened(new Square(originRow,originColumn-1),ChessPiece.Color.WHITE)){
						if(board.board[originRow][originColumn-2] == null && !isThreatened(new Square(originRow,originColumn-2),ChessPiece.Color.WHITE)){
							if(board.board[originRow][originColumn-3] == null){
								if(board.board[originRow][originColumn-4] != null){
									if(board.board[originRow][originColumn-4].getType() == ChessPiece.Type.ROOK){
										if(!board.board[originRow][originColumn-4].hasMoved){
											//System.out.println("Black queen's side castle successful");
											makeMove(new Square(0,0), new Square(0, 3));
											if(board.board[0][3] != null)
												board.board[0][3].hasMoved = true;
											if(board.board[0][2] != null)
												board.board[0][2].hasMoved = true;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//White king's side castle
		else if(originRow == 7 && destRow == 7 && destColumn == 6){
			if(!board.board[originRow][originColumn].hasMoved){
				if(!isThreatened(origin,ChessPiece.Color.BLACK)){
					if(board.board[originRow][originColumn+1] == null && !isThreatened(new Square(originRow,originColumn+1),ChessPiece.Color.BLACK)){
						if(board.board[originRow][originColumn+2] == null && !isThreatened(new Square(originRow,originColumn+2),ChessPiece.Color.BLACK)){
							if(board.board[originRow][originColumn+3] != null){
								if(board.board[originRow][originColumn+3].getType() == ChessPiece.Type.ROOK){
									if(!board.board[originRow][originColumn+3].hasMoved){
										//System.out.println("White king's side castle successful");
										makeMove(new Square(7,7), new Square(7, 5));
										if(board.board[7][6] != null)
											board.board[7][6].hasMoved = true;
										if(board.board[7][5] != null)
											board.board[7][5].hasMoved = true;
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		//Black king's side castle
		else if(originRow == 0 && destRow == 0 && destColumn == 6){
			if(!board.board[originRow][originColumn].hasMoved){
				if(!isThreatened(origin,ChessPiece.Color.WHITE)){
					if(board.board[originRow][originColumn+1] == null && !isThreatened(new Square(originRow,originColumn+1),ChessPiece.Color.WHITE)){
						if(board.board[originRow][originColumn+2] == null && !isThreatened(new Square(originRow,originColumn+2),ChessPiece.Color.WHITE)){
							if(board.board[originRow][originColumn+3] != null){
								if(board.board[originRow][originColumn+3].getType() == ChessPiece.Type.ROOK){
									if(!board.board[originRow][originColumn+3].hasMoved){
										//System.out.println("Black king's side castle successful");
										makeMove(new Square(0,7), new Square(0, 5));
										if(board.board[0][6] != null)
											board.board[0][6].hasMoved = true;
										if(board.board[0][5] != null)
											board.board[0][5].hasMoved = true;
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		if(Math.abs(destColumn - originColumn) > 1){
			return false;
		}

		return true;
	}

	/**
	 * Moves the piece on Square origin to Square destination.
	 * @param origin, the square holding the piece to be moved
	 * @param destination, the square holding the destination of the piece to be moved
	 */
	public static void makeMove(Square origin, Square destination){
		board.board[destination.getRow()][destination.getColumn()] = board.board[origin.getRow()][origin.getColumn()];
		//board.board[destination.getRow()][destination.getColumn()].hasMoved = true;
		board.board[origin.getRow()][origin.getColumn()] = null;
	}

	/**
	 * Checks if the Square target is currently under attack by the opposing side
	 * @param target, the Square that might be threatened
	 * @param attackerColor, the Color that might be attacking the target
	 * @return boolean telling whether the target is under attack
	 */
	public static boolean isThreatened(Square target, ChessPiece.Color attackerColor){
		Square possibleThreat;

		for(int row = 0; row < 8; row++){
			for(int column = 0; column < 8; column++){
				if(board.board[row][column] != null){
					if(board.board[row][column].getColor() == attackerColor){
						possibleThreat = new Square(row,column);
						if(checkLegality(possibleThreat,target)){
							//System.out.println("Possible threat on square " + possibleThreat.getRow() +", " + possibleThreat.getColumn());
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Checks whether the game is currently in Stalemate
	 * @param targetColor, the side whose turn it is
	 * @return boolean telling whether the game is currently in Stalemate
	 */
	public static boolean isInStaleMate(ChessPiece.Color targetColor){
		if(isInCheck(targetColor)){
			return false;
		}

		boolean stalemate = true;
		Square origin = new Square(0,0);
		Square destination = new Square(0,0);
		ChessPiece takenPiece = null;

		for(int row = 0; row < 8; row++){
			for(int column = 0; column < 8; column++){
				if(board.board[row][column] != null){
					if(board.board[row][column].getColor() == targetColor){
						origin.setRow(row);
						origin.setColumn(column);
						//Found piece of target color, is there anywhere to move that does not result in check?
						for(int possibleRow = 0; possibleRow < 8; possibleRow++){
							for(int possibleColumn = 0; possibleColumn < 8; possibleColumn++){
								destination.setRow(possibleRow);
								destination.setColumn(possibleColumn);
								if(checkLegality(origin, destination)){
									takenPiece = pieceAt(destination);
									makeMove(origin,destination);
									if(!isInCheck(targetColor)){
										stalemate = false;
									}

									makeMove(destination,origin);
									board.board[destination.getRow()][destination.getColumn()] = takenPiece;
									if(!stalemate){
										return false;
									}
								}

							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Checks whether the given side is in Check
	 * @param targetColor, the color of the side who may be in check
	 * @return boolean telling whether the given side is in check
	 */
	public static boolean isInCheck(ChessPiece.Color targetColor){
		ChessPiece.Color attackingColor;
		if(targetColor == ChessPiece.Color.WHITE){
			attackingColor = ChessPiece.Color.BLACK;
		}
		else{
			attackingColor = ChessPiece.Color.WHITE;
		}

		for(int row = 0; row < 8; row++){
			for(int column = 0; column < 8; column++){
				if(board.board[row][column] != null){
					if(board.board[row][column].getColor() == targetColor && board.board[row][column].getType() == ChessPiece.Type.KING){
						if(isThreatened(new Square(row,column),attackingColor)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether the given side is in Checkmate
	 * @param targetColor, the color of the side who may be in Checkmate
	 * @return boolean telling whether the given side is in Checkmate
	 */
	public static boolean isInCheckMate(ChessPiece.Color targetColor){
		boolean checkMate = true;

		Square origin = new Square(0,0);
		Square destination = new Square(0,0);
		ChessPiece takenPiece = null;

		for(int row = 0; row < 8; row++){
			for(int column = 0; column < 8; column++){
				if(board.board[row][column] != null){
					if(board.board[row][column].getColor() == targetColor){
						origin.setRow(row);
						origin.setColumn(column);
						//Found a piece of target color, is there anywhere to move it to prevent checkmate?
						for(int possibleRow = 0; possibleRow < 8; possibleRow++){
							for(int possibleColumn = 0; possibleColumn < 8; possibleColumn++){
								destination.setRow(possibleRow);
								destination.setColumn(possibleColumn);
								if(checkLegality(origin, destination)){
									takenPiece = pieceAt(destination);
									makeMove(origin,destination);
									if(!isInCheck(targetColor)){
										//System.out.println("Check mate can be prevented by moving " + origin.getRow() + "," + origin.getColumn() + " to " + destination.getRow() + "," + destination.getColumn());
										checkMate = false;
									}

									makeMove(destination,origin);
									board.board[destination.getRow()][destination.getColumn()] = takenPiece;
									if(!checkMate){
										return false;
									}
								}

							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Prints "Illegal move, try again"
	 */
	public static void printIllegalMove(){
		System.out.println("Illegal move, try again");
		return;
	}

	/**
	 * Prints "Check"
	 */
	public static void printCheck(){
		System.out.println("Check");
	}

	/**
	 * Prints "Checkmate"
	 */
	public static void printCheckMate(){
		System.out.println("Checkmate");
	}

	/**
	 * Prints "Stalemate"
	 */
	public static void printStaleMate(){
		System.out.println("Stalemate");
	}

	/**
	 * Tells whether the given array indices are in bounds
	 * @param row, the row index
	 * @param column, the column index
	 * @return boolean telling whether the given array indices are in bounds
	 */
	public static boolean isInBounds(int row, int column){
		if(row < 0 || row > 7 || column < 0 || column > 7){
			return false;
		}
		return true;
	}

	/**
	 * Returns the Color associated with the current turn
	 * @param whiteToMove, boolean describing whose turn it is.
	 * @return Color associated with the current turn
	 */
	public static ChessPiece.Color colorToMove(boolean whiteToMove){
		if(whiteToMove){
			return ChessPiece.Color.WHITE;
		}
		else{
			return ChessPiece.Color.BLACK;
		}
	}

	/**
	 * Returns the piece at the given square
	 * @param destination, the Square holding the given piece
	 * @return ChessPiece, the piece at the square
	 */
	public static ChessPiece pieceAt(Square destination){
		if(board.board[destination.getRow()][destination.getColumn()] != null){
			return board.board[destination.getRow()][destination.getColumn()];
		}
		return null;
	}

	/**
	 * Converts the abbreviation of a given Chess piece to its piece type enum
	 * @param abbr, the abbreviation of a given Chess piece
	 * @return ChessPiece, the enum associated with the given abbreviation.
	 */
	public static ChessPiece.Type abbreviationToType(char abbr){
		if(abbr == 'b' || abbr == 'B'){
			return ChessPiece.Type.BISHOP;
		}
		if(abbr == 'r' || abbr == 'R'){
			return ChessPiece.Type.ROOK;
		}
		if(abbr == 'n' || abbr == 'N'){
			return ChessPiece.Type.KNIGHT;
		}
		if(abbr == 'q' || abbr == 'Q'){
			return ChessPiece.Type.QUEEN;
		}

		return null;
	}
}
