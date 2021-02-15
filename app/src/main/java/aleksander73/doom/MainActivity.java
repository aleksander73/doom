package aleksander73.doom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import aleksander73.vector.core.GameEngine;

public class MainActivity extends AppCompatActivity {
    private final GameEngine gameEngine = new GameEngine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameEngine.getOnInitialized().queueRunnable(() -> {
            Doom doom = new Doom();
            gameEngine.startGame(doom);
        });
        gameEngine.initialize(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.shutdown();
        System.exit(0);
    }
}