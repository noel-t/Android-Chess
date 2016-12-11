package website.kale.androidchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridLayout;

public class PlayGame extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_game);

        getBoard();
    }

    private void getBoard() {
        GridLayout chessBoard = (GridLayout) findViewById(R.id.chessBoardGrid);




    }
}
