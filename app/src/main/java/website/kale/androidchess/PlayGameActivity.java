package website.kale.androidchess;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

public class PlayGameActivity extends AppCompatActivity {
    GridLayout chessGrid;
    ArrayAdapter chessGridAdapter;
    List<String> chessPieces;
    ImageView origin;
    ChessGame chessGame;
    String moveString = "";
    boolean needsPiece = true;

    class ChessPieceListener implements View.OnClickListener {
        private final int row, column;

        public ChessPieceListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void onClick(View view) {
            ImageView thisView = (ImageView) view;
            if (origin == null) {
                thisView.setBackgroundColor(Color.RED);
                origin = thisView;
                moveString += ChessBoard.columnToChessFile(column) + ChessBoard.rowToChessRank(row);
            } else {
                moveString += " " + ChessBoard.columnToChessFile(column) + ChessBoard.rowToChessRank(row);
                Toast.makeText(PlayGameActivity.this, moveString, Toast.LENGTH_SHORT).show();
                moveString = "";
                origin.setBackgroundColor(0);
                origin = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        origin = null;
        setContentView(R.layout.activity_play_game);
        chessGrid = (GridLayout) findViewById(R.id.chessboard);
        chessGame = new ChessGame();
        drawBoard();
        drawPieces();
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
                String imageResourceString = null;
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
                    }
                }

                newSpace.setOnClickListener(new ChessPieceListener(r, c));
                chessGrid.addView(newSpace);
            }
        }
    }

    private String buildPieceImageStringByCoordinates(int row, int column) {
        if (needsPiece) {
            needsPiece = false;
            return "black_bishop";
        } else {
            needsPiece = true;
            return null;
        }


//        Square coordinates = new Square(row, column);
//        ChessPiece piece = chessGame.pieceAt(coordinates);
//        String imageIdString = null;
//
//        switch (piece.getColor()) {
//            case BLACK:
//                imageIdString += "black_";
//                break;
//
//            case WHITE:
//                imageIdString += "white_";
//                break;
//
//            default:
//                return null;
//        }
//
//        switch (piece.getType()) {
//            case PAWN:
//                imageIdString += "pawn";
//                break;
//
//            case BISHOP:
//                imageIdString += "bishop";
//                break;
//
//            case KNIGHT:
//                imageIdString += "knight";
//                break;
//
//            case ROOK:
//                imageIdString += "rook";
//                break;
//
//            case QUEEN:
//                imageIdString += "queen";
//                break;
//
//            case KING:
//                imageIdString += "king";
//                break;
//
//            default:
//                return null;
//        }
//
//        return imageIdString;
    }
}
