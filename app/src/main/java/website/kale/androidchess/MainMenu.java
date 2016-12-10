package website.kale.androidchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void newGame(View v) {
        Intent switchToNewGameScreen = new Intent(v.getContext(), PlayGame.class);
        startActivityForResult(switchToNewGameScreen, 0);
    }

    public void playbackGame(View v) {

    }
}
