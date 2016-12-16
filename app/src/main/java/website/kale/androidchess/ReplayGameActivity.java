package website.kale.androidchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;

public class ReplayGameActivity extends AppCompatActivity {
    GridLayout chessGrid;
    ListIterator<ChessBoard> chessBoardIterator;
    ChessBoard currentBoard;
    ChessGame chessGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        chessGrid = (GridLayout) findViewById(R.id.chessboard);
        chessGame = new ChessGame(getApplicationContext());
        List<ChessBoard> chessBoardList = chessGame.deserialize("testgame");

        if (chessBoardList == null)
            finish();
        else {
            chessBoardIterator = chessBoardList.listIterator();
            drawBoard();
            drawPieces();
        }
    }

    private void nextMove(View view) {
        if (chessBoardIterator.hasNext()) {
            currentBoard = chessBoardIterator.next();
            drawBoard();
            drawPieces();
        } else {
            Toast.makeText(this, "No next move", Toast.LENGTH_SHORT).show();
        }
    }

    private void previousMove(View view) {
        if (chessBoardIterator.hasPrevious()) {
            currentBoard = chessBoardIterator.previous();
            drawBoard();
            drawPieces();
        } else {
            Toast.makeText(this, "No previous move", Toast.LENGTH_SHORT).show();
        }
    }

    private void drawBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ImageView newSpace = new ImageView(this);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = 88;
                param.width = 88;
                param.topMargin = 0;
                param.setGravity(Gravity.FILL_HORIZONTAL);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                newSpace.setLayoutParams(param);
                if ((r + c) % 2 == 0)
                    newSpace.setImageResource(R.drawable.light_space);
                else
                    newSpace.setImageResource(R.drawable.dark_space);

                chessGrid.addView(newSpace);
            }
        }
    }

    private void drawPieces() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ImageView newSpace = new ImageView(this);
                String imageResourceString;
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = 88;
                param.width = 88;
                param.topMargin = 0;
                param.setGravity(Gravity.FILL_HORIZONTAL);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                newSpace.setLayoutParams(param);

                imageResourceString = buildPieceImageStringByCoordinates(r, c);
                if (imageResourceString != null) {
                    try {
                        Class res = R.drawable.class;
                        Field field = res.getField(imageResourceString);
                        int drawableId = field.getInt(null);
                        newSpace.setImageResource(drawableId);
                    }
                    catch (Exception e) {
                        finish();
                    }
                }
                chessGrid.addView(newSpace);
            }
        }
    }

    private String buildPieceImageStringByCoordinates(int row, int column) {
        Square coordinates = new Square(row, column);
        ChessPiece piece = chessGame.pieceAt(coordinates);
        String imageIdString = "";

        if (piece == null)
            return null;

        switch (piece.getColor()) {
            case BLACK:
                imageIdString += "black_";
                break;

            case WHITE:
                imageIdString += "white_";
                break;

            default:
                return null;
        }

        switch (piece.getType()) {
            case PAWN:
                imageIdString += "pawn";
                break;

            case BISHOP:
                imageIdString += "bishop";
                break;

            case KNIGHT:
                imageIdString += "knight";
                break;

            case ROOK:
                imageIdString += "rook";
                break;

            case QUEEN:
                imageIdString += "queen";
                break;

            case KING:
                imageIdString += "king";
                break;

            default:
                return null;
        }

        return imageIdString;
    }
}