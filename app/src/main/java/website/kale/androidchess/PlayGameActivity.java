package website.kale.androidchess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

public class PlayGameActivity extends AppCompatActivity {
    GridLayout chessGrid;
    ImageView origin;
    ChessGame chessGame;
    String moveString = "";

    private final class ChessPieceListener implements View.OnClickListener {
        private final int row, column;

        ChessPieceListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void onClick(View view) {
            ImageView thisView = (ImageView) view;
            if (origin == null) {
                if (thisView.getDrawable() == null)
                    return;

                thisView.setBackgroundColor(Color.RED);
                origin = thisView;
                moveString += "" + ChessBoard.columnToChessFile(column) + ChessBoard.rowToChessRank(row);
            } else {
                moveString +=  " " + ChessBoard.columnToChessFile(column) + ChessBoard.rowToChessRank(row);
                makeMove(moveString);
                moveString = "";
                origin.setBackgroundColor(0);
                origin = null;
            }
        }
    }

    class PieceDragListener implements View.OnDragListener {
        private final int row, column;

        PieceDragListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            ImageView thisView = (ImageView) v;
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(thisView.getDrawable());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(thisView.getDrawable());
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(thisView.getDrawable());
                default:
                    break;
            }
            return true;
        }
    }

    private class ConfirmDrawDialogFragment extends android.app.DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_draw)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            makeMove("draw");
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            return builder.create();
        }
    }

    private class PromptToSaveGameDialogFragment extends android.app.DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_draw)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            makeMove("draw");
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        origin = null;
        setContentView(R.layout.activity_play_game);
        chessGrid = (GridLayout) findViewById(R.id.chessboard);
        chessGame = new ChessGame(getApplicationContext());
        drawBoard();
        drawPieces();
    }

    public void callForUndo(View v) {
        makeMove("undo");
    }

    public void callForResign(View v) {
        makeMove("resign");
    }

    public void callForDraw(View v) {
        new ConfirmDrawDialogFragment().show(getFragmentManager(), "Confirm Draw");
    }

    public void callForAI(View v) {
        makeMove("ai");
    }

    private void makeMove(String move) {
        String response = chessGame.androidMove(move);
        chessGrid.removeAllViews();
        drawBoard();
        drawPieces();
        if (response == null)
            return;

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        switch (response) {
            case "White wins":
            case "Black wins":
            case "Draw":
                handleEndGame();
                break;
            default:
                break;
        }
    }

    private void handleEndGame() {
        chessGame.serialize("testgame");
        finish();
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

                newSpace.setOnClickListener(new ChessPieceListener(r, c));
//                newSpace.setOnDragListener(new PieceDragListener());
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
