package website.kale.androidchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void newGame(View v) {
        Intent switchToNewGameScreen = new Intent(v.getContext(), PlayGameActivity.class);
        startActivityForResult(switchToNewGameScreen, 0);
    }

    public void playbackGame(View v) {
        Intent switchToNewGameScreen = new Intent(v.getContext(), ReplayGameActivity.class);
        startActivityForResult(switchToNewGameScreen, 0);
    }
}
