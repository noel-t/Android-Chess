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

import java.util.List;

public class PlayGameActivity extends AppCompatActivity {
    GridLayout chessGrid;
    ArrayAdapter chessGridAdapter;
    List<String> chessPieces;
    ImageView origin;
    ChessGame chessGame;

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
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r ++) {
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
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                ImageView newSpace = new ImageView(this);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = 88;
                param.width = 88;
                param.topMargin = 0;
                param.setGravity(Gravity.FILL_HORIZONTAL);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                newSpace.setLayoutParams(param);
                newSpace.setImageResource(R.drawable.white_pawn);
                newSpace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView thisView = (ImageView) view;
                        if (origin == null) {
                            thisView.setBackgroundColor(Color.RED);
                            origin = thisView;
                        } else {
                            origin.setBackgroundColor(0);
                            origin = null;
                        }
                    }
                });
                chessGrid.addView(newSpace);
            }
        }
    }
}
