package website.kale.androidchess;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import java.util.List;

public class PlayGameActivity extends AppCompatActivity {
    GridLayout chessGrid;
    ArrayAdapter chessGridAdapter;
    List<String> chessPieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        chessGrid = (GridLayout) findViewById(R.id.activity_play_game);
//        drawBoard();
    }

//    private void drawBoard() {
//        for (int c = 0; c < 8; c++) {
//            for (int r = 0; r < 8; r ++) {
//                Button newSpace = new Button(this);
//                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
//                param.height = LayoutParams.WRAP_CONTENT;
//                param.width = LayoutParams.WRAP_CONTENT;
//                param.rightMargin = 0;
//                param.topMargin = 0;
//                param.setGravity(Gravity.CENTER);
//                param.columnSpec = GridLayout.spec(c);
//                param.rowSpec = GridLayout.spec(r);
//                newSpace.setLayoutParams (param);
//                if ((r + c) % 2 == 0)
//                    newSpace.setBackgroundColor(getResources().getColor(R.color.chessTan));
//                else
//                    newSpace.setBackgroundColor(getResources().getColor(R.color.chessBrown));
//
//                chessGrid.addView(newSpace);
//            }
//        }
//
//    }
}
